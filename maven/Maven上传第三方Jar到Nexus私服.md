
###Maven上传第三方Jar到Nexus私服

- 配置settings.xml

在settings.xml中配置私服地址和仓库

``` xml
<servers>
    <server>
      <id>sf-releases</id>
      <username>admin</username>
      <password>password</password>
    </server>
</servers>	
<mirrors>
    <mirror>
        <id>nexus</id>
        <mirrorOf>*</mirrorOf> 
        <url>http://192.168.0.111:8888/repository/maven-public/</url>
    </mirror>
</mirrors>
```

如果进行deploy时返回Return code is: 401错误，则需要进行用户验证或者你已经验证的信息有误。

- 执行mvn deploy 命令

``` shell script
mvn deploy:deploy-file -DgroupId=com.aspose -DartifactId=aspose-words -Dversion=19.5 -Dpackaging=jar -Dfile=e://aspose-words-19.5.jar -Durl=http://192.168.0.111:8888/repository/kangce-snapshots/ -DrepositoryId=sf-releases
```

- 如果想直接把Jar安装到本地仓库可以执行下面命令
``` shell script
mvn install:install-file -Dfile=E:\aspose-words-19.5.jar -DgroupId=com.aspose -DartifactId=aspose-words -Dversion=19.5 -Dpackaging=jar
```