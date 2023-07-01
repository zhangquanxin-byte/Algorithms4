# web.xml中servlet配置及其含义



每个javaEE工程中都有web.xml文件，那么它的作用是什么呢？它是每个web.xml工程都必须的吗？ 

一个web中可以没有web.xml文件，也就是说，web.xml文件并不是web工程必须的。 
web.xml文件是用来初始化配置信息：比如Welcome页面、servlet、servlet-mapping、filter、listener、启动加载级别等。

当你的web工程没用到这些时，你可以不用web.xml文件来配置你的Application。

 每个xml文件都有定义它书写规则的Schema文件，也就是说javaEE的定义web.xml所对应的xml Schema文件中定义了多少种标签元素，web.xml中就可以出现它所定义的标签元素，也就具备哪些特定的功能。web.xml的模式文件是由Sun 公司定义的，每个web.xml文件的根元素为<web-app>中，必须标明这个web.xml使用的是哪个模式文件。如： 

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
<?xml version="1.0" encoding="UTF-8"?> 
<web-app version="2.5" 
xmlns="http://java.sun.com/xml/ns/javaee" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:schemaLocation="http://java.sun.com/xml/ns/javaee 
http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"> 
</web-app> 
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

  web.xml的模式文件中定义的标签并不是定死的，模式文件也是可以改变的，一般来说，随着web.mxl模式文件的版本升级，里面定义的功能会越来越复杂，标签元素的种类肯定也会越来越多，但有些不是很常用的，我们只需记住一些常用的并知道怎么配置就可以了。

下面列出web.xml我们常用的一些标签元素及其功能：

1.指定欢迎页面：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
<welcome-file-list> 
  <welcome-file-list> 
    <welcome-file>index.jsp</welcome-file> 
    <welcome-file>index1.jsp</welcome-file> 
  </welcome-file-list> 
PS：指定了2个欢迎页面，显示时按顺序从第一个找起，如果第一个存在，就显示第一个，后面的不起作用。如果第一个不存在，就找第二个，以此类推。
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

2、命名与定制URL。我们可以为Servlet和JSP文件命名并定制URL,其中定制URL是依赖命名的，命名必须在定制URL前。下面拿serlet来举例：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
(1)、为Servlet命名： 
<servlet> 
    <servlet-name>servlet1</servlet-name> 
    <servlet-class>org.whatisjava.TestServlet</servlet-class> 
</servlet> 

(2)、为Servlet定制URL、 
<servlet-mapping> 
    <servlet-name>servlet1</servlet-name> 
    <url-pattern>*.do</url-pattern> 
</servlet-mapping>
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

3、定制初始化参数：可以定制servlet、JSP、Context的初始化参数，然后可以再servlet、JSP、Context中获取这些参数值。

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
<servlet> 
    <servlet-name>servlet1</servlet-name> 
    <servlet-class>org.whatisjava.TestServlet</servlet-class> 
    <init-param> 
          <param-name>userName</param-name> 
          <param-value>Daniel</param-value> 
    </init-param> 
    <init-param> 
          <param-name>E-mail</param-name> 
          <param-value>125485762@qq.com</param-value> 
    </init-param> 
</servlet> 
经过上面的配置，在servlet中能够调用getServletConfig().getInitParameter("param1")获得参数名对应的值。 
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

4、指定错误处理页面，可以通过“异常类型”或“错误码”来指定错误处理页面。

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
<error-page> 
    <error-code>404</error-code> 
    <location>/error404.jsp</location> 
</error-page> 
----------------------------- 
<error-page> 
    <exception-type>java.lang.Exception<exception-type> 
    <location>/exception.jsp<location> 
</error-page> 
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

5、设置过滤器：比如设置一个编码过滤器，过滤所有资源 

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
<filter> 
    <filter-name>XXXCharaSetFilter</filter-name> 
    <filter-class>net.test.CharSetFilter</filter-class> 
</filter> 
<filter-mapping> 
    <filter-name>XXXCharaSetFilter</filter-name> 
    <url-pattern>/*</url-pattern> 
</filter-mapping> 
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

6、设置监听器： 

```
<listener> 
<listener-class>net.test.XXXLisenet</listener-class> 
</listener> 
```

7、设置会话(Session)过期时间，其中时间以分钟为单位，假如设置60分钟超时： 

```
<session-config> 
<session-timeout>60</session-timeout> 
</session-config>
```

 

### Servlet配置及含义：

------

###  

这里其实最想说的是配置中各个节点含义：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
<!-- 配置一个servlet -->
<!-- servlet的配置 -->
<servlet>
    <!-- servlet的内部名称，自定义。尽量有意义 -->
    <servlet-name>ServletDemo</servlet-name>
    <!-- servlet的类全名： 包名+简单类名 -->
    <servlet-class>lm.practice.ServletDemo</servlet-class>
</servlet>
<!-- servlet的映射配置 -->
<servlet-mapping>
    <!-- servlet的内部名称，一定要和上面的内部名称保持一致！！ -->
    <servlet-name>ServletDemo</servlet-name>
    <!-- servlet的映射路径（访问servlet的名称） -->
    <url-pattern>/servlet</url-pattern>
</servlet-mapping>
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

可以看到，在配置Servlet时，有两个地方需要配置，一个是<servlet>,另一个是<servlet-Mapping>，这两个一个是配置Servlet，一个是配置其映射信息，其中<servlet>中的<servlet-name>可以随意指定，但要有一定的意义，一般取为类的名称，例如我的类名为ServletDemo，这里取名为ServletDemo,下面的<servlet-class>是类的全路径，package+calssname，一定要是全路径！

<servlet-Mapping>是映射信息，它也有一个<servlet-name>，里面的名字是对应的Servlet名，也就是我们上面配置的Servlet名字，这里是ServletDemo，下面的是映射路径，也就是访问Servlet的名称，这里也是以方便和有意义为前提的，是我们在访问Servlet在浏览器地址栏后面输入的那个信息，例如我的映射路径命名为/servlet，在地址栏中输入http://localhost/20170323/servlet

注意：这里的映射路径一定不是丢掉/，否则就会出错了，一定要写成/servlet，不能是servlet
![img](https://img2018.cnblogs.com/blog/1531673/201907/1531673-20190703192917817-979102973.png)

这里说一下在配置映射路径的时候，有以下两种：

|                                       | url-pattern                                | 浏览器输入                              |
| ------------------------------------- | ------------------------------------------ | --------------------------------------- |
| 精确匹配                              | /servlet                                   | http://localhost:8080/day10/servlet     |
| 模糊匹配                              | /*                                         | http://localhost:8080/20170323/任意路径 |
| /lm/*                                 | http://localhost:8080/20170323/lm/任意路径 |                                         |
| *.后缀名 *.do *.action *.html(伪静态) | http://localhost:8080/20170323/任意路径.do |                                         |



下面是项目中web.xml文件中springmvc的Servlet配置信息：

------



```
<!-- Spring MVC servlet -->
    <servlet>
        <servlet-name>SpringMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring-mvc.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
        <async-supported>true</async-supported>
    </servlet>
    <servlet-mapping>
        <servlet-name>SpringMVC</servlet-name>
        <!-- 此处可以可以配置成*.do，对应struts的后缀习惯 -->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
     　　<servlet>
        <servlet-name>jobDispatcherServlet</servlet-name>
        <servlet-class>com.suning.framework.uts.client.core.JobDispatcherServlet</servlet-class>
        <load-on-startup>3</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>jobDispatcherServlet</servlet-name>
        <url-pattern>/jobDispatcher</url-pattern>
    </servlet-mapping>
    <session-config>
   　　 <session-timeout>30</session-timeout>
    </session-config>
```



【注意】：

 **1）url-pattern要么以 / 开头，要么以\*开头。 绝对不能漏掉斜杠！！！！！！！！！**

 **2）不能同时使用两种模糊匹配，例如 /lm/\*.do是非法路径**

 **3）当有输入的URL有多个servlet同时被匹配的情况下：**

​          **3.1 精确匹配优先。（长的最像优先被匹配）**

​          **3.2 以后缀名结尾的模糊匹配先级最低！！！**

 