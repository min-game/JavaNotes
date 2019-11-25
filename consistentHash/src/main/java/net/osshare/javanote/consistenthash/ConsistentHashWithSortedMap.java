package net.osshare.javanote.consistenthash;

import java.util.*;
import java.util.zip.CRC32;

/**
 * 虚拟节点一致性Hash 红黑树
 */
public class ConsistentHashWithSortedMap {
    /**
     * 待添加入Hash环的服务器列表
     */
    private static String[] servers = {"192.168.0.0:111", "192.168.0.1:111", "192.168.0.2:111",
            "192.168.0.3:111", "192.168.0.4:111"};

    /**
     * 真实结点列表,考虑到服务器上线、下线的场景，即添加、删除的场景会比较频繁，这里使用LinkedList会更好
     */
    private static List<String> realNodes = new LinkedList<String>();

    /**
     * 虚拟节点，key表示虚拟节点的hash值，value表示虚拟节点的名称
     */
    private static SortedMap<Long, String> virtualNodes =
            new TreeMap<Long, String>();

    /**
     * 虚拟节点的数目，这里写死，为了演示需要，一个真实结点对应5个虚拟节点
     */
    private static final int VIRTUAL_NODES = 5;

    static {
        // 先把原始的服务器添加到真实结点列表中
        for (int i = 0; i < servers.length; i++)
            realNodes.add(servers[i]);

        // 再添加虚拟节点，遍历LinkedList使用foreach循环效率会比较高
        for (String str : realNodes) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String virtualNodeName = str + "&&VN" + String.valueOf(i);
                long hash = getHash(virtualNodeName);
                System.out.println("虚拟节点[" + virtualNodeName + "]被添加, hash值为" + hash);
                virtualNodes.put(hash, virtualNodeName);
            }
        }
        System.out.println();
    }

    /**
     * 使用FNV1_32_HASH算法计算服务器的Hash值,这里不使用重写hashCode的方法，最终效果没区别
     */
    private static long getHash(String serverNodeName) {
//        final int p = 16777619;
//        int hash = (int) 2166136261L;
//        for (int i = 0; i < str.length(); i++)
//            hash = (hash ^ str.charAt(i)) * p;
//        hash += hash << 13;
//        hash ^= hash >> 7;
//        hash += hash << 3;
//        hash ^= hash >> 17;
//        hash += hash << 5;
//
//        // 如果算出来的值为负数则取其绝对值
//        if (hash < 0)
//            hash = Math.abs(hash);
//        return hash;
        CRC32 crc32 = new CRC32();
        crc32.update(serverNodeName.getBytes());
        return crc32.getValue();
    }

    /**
     * 得到应当路由到的结点
     */
    private static String getServer(String node) {
        // 得到带路由的结点的Hash值
        long hash = getHash(node);
        // 得到大于该Hash值的所有Map
        SortedMap<Long, String> subMap =
                virtualNodes.tailMap(hash);
        // 第一个Key就是顺时针过去离node最近的那个结点
        Long i = subMap.firstKey();
        // 返回对应的虚拟节点名称，这里字符串稍微截取一下
        String virtualNode = subMap.get(i);
        return virtualNode.substring(0, virtualNode.indexOf("&&"));
    }

    public static void main(String[] args) {
        ConsistentHashWithListSort ch = new ConsistentHashWithListSort();
        //添加一系列的服务器节点
        String[] servers = {"192.168.0.0:111", "192.168.0.1:111", "192.168.0.2:111",
                "192.168.0.3:111", "192.168.0.4:111"};
        for (String server : servers) {
            ch.addServerNode(server);
        }
        //打印输出一下服务器节点
        ch.printServerNodes();

        //看看下面的客户端节点会被路由到哪个服务器节点
        String[] nodes = {"127.0.0.1:1111", "221.226.0.1:2222", "10.211.0.1:3333"};
        System.out.println("此时，各个客户端的路由情况如下：");
        for (String node : nodes) {
            VirtualServerNode virtualServerNode = ch.getServerNode(node);
            System.out.println(node + "," + ch.getHash(node) + "------->" +
                    virtualServerNode.getServerNodeName() + "," + virtualServerNode.getVirtualServerNodeHash());
        }

        //如果由一个服务器节点宕机，即需要将这个节点从服务器集群中移除
        String deleteNodeName = "192.168.0.2:111";
        ch.deleteServerNode(deleteNodeName);

        System.out.println("删除节点" + deleteNodeName + "后，再看看同样的客户端的路由情况，如下：");
        for (String node : nodes) {
            VirtualServerNode virtualServerNode = ch.getServerNode(node);
            System.out.println(node + "," + ch.getHash(node) + "------->" +
                    virtualServerNode.getServerNodeName() + "," + virtualServerNode.getVirtualServerNodeHash());
        }
    }
}