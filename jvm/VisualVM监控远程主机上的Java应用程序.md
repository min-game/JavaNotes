### VisualVM监控远程主机上的Java应用程序
- 工具 jvisualvm

在jdk目录bin下jvisualvm，jdk9已移除 https://visualvm.github.io/

- 远程服务器配置

在jar启动的时候增加jvm启动参数

-Dcom.sun.management.jmxremote    开启支持jmx远程连接

-Dcom.sun.management.jmxremote.rmi.port=1199 JMX在远程连接时，会随机开启一个RMI端口作为连接的数据端口，很有可能这个端口会被防火墙给阻止，以至于连接超时失败,所以用这个来定死这个端口

-Dcom.sun.management.jmxremote.port=1199  jmx连接端口

-Dcom.sun.management.jmxremote.authenticate=false     无需验证

-Dcom.sun.management.jmxremote.ssl=false

-Djava.rmi.server.hostname=xx.xx.xx.xx  ip为云服务器的外围ip

- visualVM使用

打开VisualVM -> 添加远程主机 ->添加JMX连接 -> 输入上面配置的端口号