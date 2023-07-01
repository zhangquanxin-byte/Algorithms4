# 数据源和jndi



### 前言

我们在进行数据访问，声明模板和repository之前【JAVA】Spring对JDBC的支持，都需要配置数据源用来连接数据库。数据源又可以分为两大类：直连的数据源 和 连接池的数据源 ，其中连接池的数据源又有多种，接下来就让我们来学习一下这两种数据源。当然在学习之前，我们首先需要知道连接池、数据源、JNDI是什么，分别从当什么角色，有什么作用。

连接池、数据源、JNDI
连接池、数据源
Java中的数据源就是连接到数据库的一条路径，数据源中并无真正的数据，它仅仅记录的是你连接到哪个数据库，以及如何连接。DataSource的创建可以有不同的实现。DataSource通常被称为数据源，它包含连接池 和连接池管理 两部分，习惯上也经常把DataSource称为连接池。

连接池思想
在系统初始化的时候，将数据库连接对象（Connection） 存储在内存中，当用户需要访问数据库时候，并不是建立一个新的连接，而是从连接池中取出一个已经建立好的空闲连接对象。而连接池负责分配、管理、释放数据库连接对象。注意的是：连接池是由容器（比如tomcat） 提供的，同时容器也管理着连接池。

JNDI
JNDI（Java Naming and Directory Interface，Java命名和目录接口），JNDI是Java平台的一个标准扩展，提供一组接口、类和关于命名空间的概念。其功能通俗的来说，就是提供一个类似全局的map，key保存JNDI的名称，value保存你要放到里面的资源的引用（如Java对象），以后要想要获取value的资源即可通过lookup名称检索。
注意：需要区分开JNDI和通过JNDI查找的数据源的概念，不能将JNDI和连接池混为一谈。

这套API的主要作用在于：它可以将Java对象放在一个容器中（支持JNDI的容器例如Tomcat），并且为容器中的Java对象取一个名称，以后程序想要获得Java对象，只要通过名称检索即可。
其核心API为Context，它代表JNDI容器，其lookup方法为检索容器中对应名称的对象。
使用JNDI访问Tomcat内置连接池

将数据库驱动的包复制到Tomcat的安装目录/lib/中，这样Tomcat服务器才能找到数据库驱动
编写访问JNDI程序，运行在Tomcat内部，所以通常运行在servlet、jsp中
在Tomcat启动时，自动加载配置文件（context.xml），创建数据库连接池，该连接池由Tomcat管理。

直连的数据源
用户每次请求都需要向数据库获得连接，而数据库创建连接通常需要消耗相对较大的资源，创建的时间也较长。


连接池的数据源
连接池的思想
数据库连接是一种关键的有限的昂贵的资源，如果每次访问数据库的时候，都需要进行数据库连接，那么势必会造成性能低下；同时，如果用户失误忘记释放数据库连接，会导致资源的浪费等。而数据库连接池就是刚好可以解决这些问题，通过管理连接池中的多个连接对象（Connection），实现连接对象（connection）重复利用，从而大大提高了数据库连接方面的性能。

常用的数据库连接池
Tomcat内置的连接池（Tomcat Dbcp）
Tomcat在7.0以前的版本都是使用commons-dbcp作为连接池的实现，但是由于commons-dbcp饱受诟病。因此很多人会选择一些第三方的连接池组件，例如c3p0，bonecp等。为此，Tomcat从7.0开始引入了一个新的模块：Tomcat Jdbc Pool（Tomcat Dbcp）。关于更多请看这里
连接池的配置：

```
<Context>
     <Resource 
     	    name="jdbc/login_register" 
	    auth="Container" 
	    type="javax.sql.DataSource"
	    factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
           maxActive="100" maxIdle="30" maxWait="10000"
           username="root" password="root" 
           driverClassName="com.mysql.jdbc.Driver"
           url="jdbc:mysql://localhost:3306/db_user"/>
</Context>
```

属性的说明：

Resource：声明这个是资源
name=指定Resource（资源）的JNDI名称
auth=指定管理Resource的Manager(Container由容器创建和管理，Application由Web应用创建和管理)
factory：必需的属性，其值应为 org.apache.tomcat.jdbc.pool.DataSourceFactory
type=指定Resource的java类（即指定JNDI能拿到的是什么类型的数据，javax.sql.DataSource这里指定为连接池对象）。类型应为 javax.sql.DataSource 或 javax.sql.XADataSource。根据类型，将创org.apache.tomcat.jdbc.pool.DataSource 或 org.apache.tomcat.jdbc.pool.XADataSource。
maxActive=指定连接池中处于活动状态的数据库连接的最大数量
maxIdle=指定连接池中处于空闲状态的数据库的最大数量
maxWait=指定连接池中连接处于空闲的最长时间，超过这个时间会提示异常，取值为-1，表示可以无限期等待，单位为毫秒（ms）
我们需要注意的属性是factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
如果你没有声明以上的属性，当Tomcat读到type="javax.sql.DataSource"属性时也会自动安装DBCP，除非你指定不同的factory。

连接池的获取：

     public class DBUtil {
      public static Connection getConnection(){
          Connection conn = null;
          try {
              Context c = new InitialContext();
              DataSource dataSource = (DataSource) c.lookup("java:/comp/env/jdbc/login_register");//这里的jdbc/login_register和篇配置文件中的name属性一致
              conn = dataSource.getConnection();
              return conn;
          } catch (SQLException e) {
              e.printStackTrace();
          } catch (NamingException e) {
              e.printStackTrace();
          }
     return conn;

lookup方法用于查找指定JNDI名称的连接池，java:/comp/env/jdbc/login_registe的jdbc/login_registe和配置文件中JNDI的名称相对应。这样既可查找到数据库连接池，也就能获得到连接对象了。当然，连接池的获取方式不只一种，比如还有通过配置文件来获取的，感兴趣的可以查阅资料。

大概流程是这样的：通过配置文件Resource声明资源为连接池类型（javax.sql.DataSource）的对象
加载连接池factory，org.apache.tomcat.jdbc.pool.DataSourceFactory
通过JNDI找到连接池资源
通过这个例子我们看到了JNDI的作用，JNDI你可以理解成一个水池的门牌,连接池就相当于这个水池，应用要取到要访问数据库时,通过找到JNDI，然后再通过连接池和数据库所建立的连接来访问。
