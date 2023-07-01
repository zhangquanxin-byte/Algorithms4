# 										P6spy用法

1、依赖jar

```xml
<dependency>
    <groupId>p6spy</groupId>
    <artifactId>p6spy</artifactId>
    <version>3.8.7</version>
</dependency>
```



2、配置文件spy.properties

```
dateformat=yyyy-MM-dd HH:mm:ss   --日期格式化
appender=com.p6spy.engine.spy.appender.FileLogger   --日志以文件形式打印
logfile=e:/Temp/spy.log    --打印日志为止
append=true  --每次打印日志，日志文件追加还是覆盖。true为追加
logMessageFormat=com.p6spy.engine.spy.appender.CustomLineFormat   --自定义日志文件打印类
customLogMessageFormat=%(currentTime)|%(executionTime)|%(sql)   --自定义选择日志文件需要的子项
--真实数据源
realdatasource=/test
realdatasourceclass=com.mysql.cj.jdbc.Driver
realdatasourceproperties=port;3306,serverName;localhost,databaseName;mysql,foo;bar
```





3、修改原数据源driverClass、jdbcUrl属性：





```
<!-- 如果要研究某个xml中可以设置哪些属性。找相关类的 属性 或者setXxx()-->
        <property name="user">root</property>
        <property name="password">1234</property>
        <!-- <property name="driverClass">com.mysql.cj.jdbc.Driver</property>-->
       <property name="driverClass">com.p6spy.engine.spy.P6SpyDriver</property>
       <property name="jdbcUrl">jdbc:p6spy:mysql://localhost:3306/test?useSSL=false&amp;serverTimezone=Hongkong&amp;characterEncoding=utf-8&amp;autoReconnect=true</property>
       <!-- 连接池参数 -->
        <!--  初始连接数量  -->
        <property name="initialPoolSize">5</property>
        <!--  最大连接数  -->
        <property name="maxPoolSize">10</property>
        <!--  超时时间  -->
        <property name="checkoutTimeout">3000</property>
```





4、完成。