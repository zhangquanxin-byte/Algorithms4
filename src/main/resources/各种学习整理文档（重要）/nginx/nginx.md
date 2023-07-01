# 正向代理和反向代理

## 正向代理

正向代理的对象是客户端

![image-20200224161905941](assets/image-20200224161905941.png)





所谓正向代理就是顺着请求的方向进行的代理，即代理服务器是 由你配置 为你服务，去请求目标服务器地址。

访问谷歌, 我们直接访问不通, 可以找一个代理服务器为我们服务, 我们通过代理服务器请求到谷歌网站

对于谷歌, 只知道有一个服务器访问了自己, 并不知道这件事你是访问不了他, 找了一个代理服务器访问自己



## 反向代理

反向代理的对象是服务端

![image-20200224163833188](assets/image-20200224163833188.png)





反向代理正好与正向代理相反, 代理服务器是为目标服务器服务的

比如 我们访问百度网站，百度的代理服务器对外的域名为 https://www.baidu.com 。具体内部的服务器节点我们不知道。现实中我们通过访问百度的代理服务器后，代理服务器给我们转发请求到他们N多的服务器节点中的一个给我们进行搜索后将结果返回。



# 常用Web服务器

apache http server、nginx、tomcat、jetty、iis、weblogic、websphere、jboss、glassfish 等

HTTP服务器本质上也是一种应用程序

它通常运行在服务器之上，绑定服务器的 IP地址并监听某一个TCP端口来接收并处理HTTP请求

客户端（一般来说是IE, Firefox，Chrome这样的浏览器）就能够通过HTTP协议来获取服务器上的网页、文档、音频、视频等资源

Apache HTTP server 和Nginx都能够将某一个文本文件的内容通过HTTP协议返回到客户端，但是这些文本文件的内容是固定的，也就是说什么情况下访问该文本的内容都是完全一样的，这样的资源我们称之为静态资源。

动态资源则相反，不同时间、不同客户端所得到的内容是不同的  

Apache Http Server是使用比较广泛也是资格最老的web服务器, 是Apache基金会下第一个开源的WEB服务器。

在Nginx出现之前，大部分企业使用的都是Apache, 在互联网发展初期，流量不是特别大的时候，使用Apache完全满足需求。但是随着互联网的飞速发展，网站的流量以指数及增长，这个时候除了提升硬件性能以外，Apache Http server也开始遇到瓶颈了

这个时候Nginx出现了，就是为了解决大型网站高并发设计的，所以对于高并发来说，Nginx有先天的优势。因此Nginx也在慢慢取代Apache Http server。 

Nginx另一个强大的功能就是反向代理，现在大型网站分工详细，哪些服务器处理数据流，哪些处理静态文件，这些谁指挥，一般都是用nginx反向代理到内网服务器，这样就起到了负载均衡分流的作用。

nginx高度模块化的设计，编写模块相对简单。



# NGINX安装

## 环境准备

	# 可选
	yum update 
	
	# gcc编译环境，所以，在mini centos中需要安装编译环境
	yum -y install gcc-c++
	
	# Nginx的http模块需要使用pcre来解析正则表达式
	yum -y install pcre pcre-devel
	
	# 依赖的解压包
	yum -y install zlib zlib-devel
	
	# openssl安装
	yum install -y openssl openssl-devel



## 设置nginx的安装目录

我安装到 /usr/local/nginx

  ```
cd /usr/local
  
mkdir nginx
  ```

## 上传源码包 

上传 nginx-1.14.2.tar.gz 到 虚拟机 /root 目录 (可以是其他目录)

## 解压

加压 nginx 文件到当前目录 (我的是 /root)

```
tar -zxvf nginx-1.14.2.tar.gz
```

## 编译安装

```
cd nginx-1.14.2
```

以下3条命令都在 /root/nginx-1.14.2 目录下执行

```
#1 指定安装目录
./configure  --prefix=/usr/local/nginx

#2 编译
make

#3 安装
make install
```

## 启动

```
cd /usr/local/nginx/sbin

# 启动, 加载默认配置文件, NGINX_HOME/conf/nginx.conf
./nginx

# 启动, 加载指定配置文件
./nginx -c /xxx/xxx/nginx.conf

# 重加载, 配置文件修改后
./nginx -s reload

# 此方式停止步骤是待nginx进程处理任务完毕进行停止
./nginx -s quit

# 此方式相当于先查出nginx进程id再使用kill命令强制杀掉进程
./nginx -s stop

# 验证配置文件
./nginx -t
```



## 测试

浏览器中直接输入  http://虚拟机 ip : 80



## windows版

```
start nginx.exe						//启动

nginx.exe -s stop					//停止nginx

nginx.exe -s reload                 //重新加载nginx

nginx.exe -s quit                 	//退出nginx
```

直接双击nginx.exe，双击后一个黑色的弹窗一闪而过

## 启动nginx

有很多种方法启动nginx

(1)直接双击nginx.exe，双击后一个黑色的弹窗一闪而过

(2)打开cmd命令窗口，切换到nginx解压目录下，输入命令 nginx.exe 或者 `start nginx` ，回车即可



## 检查nginx是否启动成功

(1)直接在浏览器地址栏输入网址 [http://localhost:80，回车](http://localhost:80，回车/)

(2)可以在cmd命令窗口输入命令 `tasklist /fi "imagename eq nginx.exe"`

## 关闭nginx

如果使用cmd命令窗口启动nginx，关闭cmd窗口是不能结束nginx进程的，可使用两种方法关闭nginx

(1)输入nginx命令： `nginx -s stop`(快速停止nginx) 或 `nginx -s quit`(完整有序的停止nginx)

(2)使用taskkill： `taskkill /f /t /im nginx.exe`

# Nginx核心配置文件

nginx的核心配置文件, 主要包括三个段

Main、 Events 、 Http



```
worker_processes  1; 

# cpu
```



```
events {
    worker_connections  1024; #允许连接数
}
```



```
http {
    include       mime.types;
    default_type  application/octet-stream;

    sendfile        on;
    keepalive_timeout  65;
    
    //监听的端口和服务器名字
    server {
        listen       80;
        server_name  localhost;

	//这里/表示接收所有请求，请求的根目录再html目录，即所有请求的页面从html目录下找
	//index表示访问/时显示的根目录下首页
        location / {
            root   html;
            index  index.html index.htm;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }

}
```

文件查找

```nginx
    server {
        listen       80;
        server_name  localhost;

        # http://192.168.5.55/pord/abc.html
        # html/prod/abc.html
        location  /  {
            root   html;
            index  index.html index.htm;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

    }
```



# 虚拟主机配置

## 基于域名

```nginx
    # 修改 hosts 文件：这里端口相同，优先匹配同域名的server
    server {
        listen       80;
        server_name  www.skeye.com;

        location  /  {
            root   /usr/local/nginx/htmlcom;
            index  index.html;
        }
    }

    server {
        listen       80;
        server_name  www.skeye2.com;

        location  /  {
            root   /usr/local/nginx/htmlcom2;
            index  index.html;
        }
    }
```

## 基于端口

```nginx
//可以多个server,使用多个端口    
server {
        listen       81;
        server_name  localhost;

        location  /  {
            root   /usr/local/nginx/html81;
            index  index.html;
        }
    }
```

## 基于 IP

```nginx
	server {
        listen       8088;
        server_name  客户端ip;

        location / {
            root   html/bbb;
            index  index.html index.htm;
        }
    }
```



# Nginx的访问日志配置

```nginx
http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;
}
```

```nginx
log_format  myformat  '$remote_addr - $remote_user';

access_log  logs/my.log  myformat;

access_log  off;
```



# location的语法和匹配规则

## 正则表达式

```
.     :匹配除换行符以外的任意字符
?     :重复0次或1次
+     :重复1次或更多次
*     :重复0次或更多次
\d    :匹配数字
^     :匹配字符串的开始字符
$     :匹配字符串的结束字符
{n}   :重复n次
[c]   :匹配单个字符c
[a-z] :匹配a-z小写字母的任意一个
[A-Z] :匹配A-Z小写字母的任意一个
[0-9] :匹配数字
```



## 1.精准匹配

```nginx
    server {
        listen       82;
        server_name  localhost;

        location = /me.html  {
            root   html82a;
        }

        location   /me.html  {
            root   html82b;
        }
    
    	location / {
            root   html;
        }

    }
```

## 2.正则匹配

```
=      # 精确匹配

~      # 执行一个正则匹配，区分大小写
~*     # 执行一个正则匹配，不区分大小写
^~     # 匹配普通字符，如果该选项匹配，只匹配该选项，不匹配别的选项

/      # 通用匹配, 如果没有其它匹配, 任何请求都会匹配到
```

```nginx
# 只匹配"/"
location  = / {
  [ config a ]
}

# 匹配任何请求，因为所有请求都是以"/"开始
# 但是更长字符匹配或者正则表达式匹配会优先匹配
location  / {
  [ config b ]
}

# 匹配任何以 /images/ 开始的请求，并停止匹配其它location
location ^~ /images/ {  
  [ config c ]
}

# localhost/images/a.png
# 匹配以 gif, jpg, png, js, css 结尾的请求
# 但是所有 /images/ 目录的请求将由 [config c]处理
location ~* \.(gif|jpg|png|css|js)$ {  
  [ config d ]
}
```

##  3.普通匹配

```nginx
	
	server {
        listen       80;
        server_name  localhost;

    	# http://ip:port/api/get/a.html	
        location /api/get {
            root   html;
        }
        
    	# http://ip:port/api/a.html	
        location /api {
            root   html;
        }
    
    	# http://ip:port/xxx/a.html?id=1
        location / {
            root   html;
        }
    }
```

前缀相同, 按照最大长度来做匹配

## 匹配顺序

多个正则location直接按书写顺序匹配，成功后就不会继续往后面匹配

普通（非正则）location会一直往下，直到找到匹配度最高的（最大前缀匹配）

当普通location与正则location同时存在，如果正则匹配成功, 则不会再执行普通匹配

所有类型location存在时，“=”   >  “^~”  > 正则  > 普通（最大前缀匹配）

顺序：

(=) > (完整路径) > (^~ 路径) > (~,~* 正则顺序) > (部分路径) > (/)



# Rewrite的使用

**URL 重写**

编程语言的功能

ngx_http_rewrite_module 模块

支持url重写, 支持if判断, 但不支持else

可以使用nginx提供的全局变量或自己设置的变量, 结合正则表达式和标志位实现url重写以及重定向

只能放在server{},location{},if{}中, 并且只能对(URI)域名后边的除去传递的参数外的字符串起作用

**nginx重写规则说起来简单，做起来难，重点在于正则表达式，还需要考虑到nginx执行顺序**

## 全局变量

```
# localhost/data?id=1&name=2
# $args_id
$arg_PARAMETER    	#这个变量包含GET请求中，如果有变量PARAMETER时的值
$args				#这个变量等于请求行中的参数，同$query_string
$content_length		#请求头中的Content-length字段
$content_type		#请求头中的Content-Type字段
$document_root		#当前请求在root指令中指定的值
$host				#请求主机头字段，否则为服务器名称
$http_user_agent	#客户端agent信息
$http_cookie		#客户端cookie信息
$limit_rate			#这个变量可以限制连接速率
$request_method		#客户端请求的动作，通常为GET或POST
$remote_addr		#客户端的IP地址
$remote_port		#客户端的端口
$remote_user		#已经经过Auth Basic Module验证的用户名
$request_filename	#当前请求的文件路径，由root或alias指令与URI请求生成
$scheme				#HTTP方法（如http，https）
$server_protocol	#请求使用的协议，通常是HTTP/1.0或HTTP/1.1
$server_addr		#服务器地址，在完成一次系统调用后可以确定这个值
$server_name		#服务器名称
$server_port		#请求到达服务器的端口号
$request_uri		#包含请求参数的原始URI，不包含主机名，如：”/foo/bar.php?arg=baz”
$uri				#不带请求参数的当前URI，$uri不包含主机名，如”/foo/bar.html”
$document_uri		#与$uri相同
```

```
例：http://localhost:88/test1/test2/test.html
$host				localhost
$server_port		88
$request_uri		http://localhost:88/test1/test2/test.html
$uri				/test1/test2/test.php
$document_root		/var/www/html
$request_filename	/var/www/html/test1/test2/test.html
```



## 常用指令

### set

```nginx
set variable value; 
```

定义一个变量并赋值，值可以是文本、变量或者文本变量混合体

### return

```nginx
return code;
```

```nginx
if ($request_uri ~ *\.sh ){
 	return 403;
}
```

### if

```
if (条件) { 
	进行重写 
}
```

```
1. 当表达式只是一个变量时，如果值为空或任何以0开头的字符串都会当做false

2. 使用“=”和“!=”比较一个变量和字符串

3. 使用“~”做正则表达式匹配，“~*”做不区分大小写的正则匹配，“!~”做区分大小写的正则不匹配

4. 使用“-f”和“!-f” 检查一个文件是否存在

5. 使用“-d”和“!-d” 检查一个目录是否存在

6. 使用“-e”和“!-e” 检查一个文件、目录、是否存在

7. 使用“-x”和“!-x” 检查一个文件是否可执行
```

```nginx
//给某个访问IP返回403
if ($remote_addr = "202\.38\.78\.85") {
    return 403;
}
 
//如果提交方法为POST，则返回状态405（Method not allowed）。return不能返回301,302
if ($request_method = POST) {
    return 405;
}
 
//如果请求的文件名不存在，则反向代理到localhost
if (!-f $request_filename){
    proxy_pass http://127.0.0.1;
}
```



### rewrite

```nginx
rewrite regex replacement [flag]
```



```nginx
# http://xxx.com/a.html
# http://www.aaa.com/a.html
server {　　　　　　　　　　　　　　　　　　　　　　　　　　
    listen     80;
    server_name  xxx.com;
    rewrite ^/(.*) http://www.aaa.com/$1  permanent;
}
```



```nginx
server {
	listen       80;
	server_name  xxx.com;
    
    if ( $http_host ~* "^(.*)" ) {
        rewrite  ^(.*) http://www.baidu.com  break;
    }

    location / {
		root   html;
		index  index.html index.htm;
	}
}
```



```nginx
# /images/ab/cd123/test.png的请求，重写到 /data?file=test.png
# 匹配到location /data，先看/data/images/test.png文件存不存在，如果存在则正常响应
# 如果不存在就到 image404.html，直接返回404状态码。

    server {
        listen       83;
        server_name  localhost;
    
        # /images/ab/c12d3/test.png 的请求，重写到 /data?file=test.png
        location / {
            # 注意这里要用''单引号引起来
            rewrite 
            	'^/images/([a-z]{2})/([a-z0-9]{5})/(.+)\.(png|jpg|gif)$' 
            	/data?file=$3.$4;
            # 
            set $filename $3;
            set $filetype $4;
        }

        # /data/a.png
        # /data/images/a.png
        location /data {
            root /data/images;
            # 判断文件是否存在，不存在就跳转
            try_files /$arg_file /image404.html;
        }
    
        location = /image404.html {
            # 图片不存在返回特定的信息
            return 404 "[$filename.$filetype] image not found";
        }
    }
```

flag 可以是如下参数

```
last 		完成该rewrite规则的执行后, 停止处理后续rewrite指令集, 然后查找匹配改变后URI的新location

break   	完成该rewrite规则的执行后，停止处理后续rewrite指令集，并不再重新查找
			但是当前location内剩余非rewrite语句和location外的的非rewrite语句可以执行

redirect 	返回302临时重定向，地址栏会显示跳转后的地址

permanent	返回301永久重定向，地址栏会显示跳转后的地址
			如果客户端不清理浏览器缓存, 那么返回的结果将永久保存在客户端浏览器中


因为301和302不能简单的只返回状态码，还必须有重定向的URL，所以return指令无法简单的返回301,302


last和break区别:

break和last都能阻止继续执行后面的rewrite指令

last不终止重写后的url匹配，即新的url会再从server location走一遍匹配流程，而break终止重写后的匹配
```



# 反向代理

```nginx
	server {
        listen       80;
        server_name  localhost;
		
        location / {
        	# 设置一些请求头
			proxy_pass  http://www.baidu.com;
        	# proxy_pass  http://192.168.5.11:8080
        }
    }
```

```
proxy_pass 既可以是ip地址，也可以是域名，同时还可以指定端口
```

**proxy_pass用法解析：**

 项目中要使用nginx作为前端服务的负载均衡，在进行nginx配置过程中，对于在负载均衡配置反向代理proxy_pass参数，proxy_pass配置url参数，在url后面有加"/"或者没有加"/"，在经过反向代理后，代理路径结果是不一样的。示例说明：

假如前端访问地址：http://172.16.10.110/nginx/index.html

在location配置如下：

```
location ^~/nginx/{



      proxy_pass:http://172.16.10.120:8080/;



}
```

经过nginx反向代理后，后台实际访问的服务路径是 http://172.16.10.120:8080/index.html，也就是说在有加"/"的情况下，代理路径是不会把location匹配的部分路径加到代理路径后面。

```
location ^~/nginx/{



      proxy_pass:http://172.16.10.120:8080;



}
```

经过nginx反向代理后，后台实际访问的服务路径是 http://172.16.10.120:8080/nginx/index.html，也就是说在没有加"/"的情况下，代理路径会把location匹配的部分路径加到代理路径后面。



# 负载均衡

upstream模块

```nginx
	# 轮询  这边我的项目的根路径为thirdWeek  做负载均衡的项目都是一摸一样的
	upstream tomcatserver{
		server 192.168.1.55:8080;
		server 192.168.1.55:8081;
	}
	
	server {
        listen       89;
        server_name  localhost;
        location / {
            proxy_pass	http://tomcatserver/thirdWeek/;
        }
    }

	# 权重 weight：指定轮询几率 
	upstream tomcatserver{
		server 192.168.1.55:8080 weight=3;
		server 192.168.1.55:8081 weight=1;
	}

	# ip_hash
    upstream tomcatserver{
        ip_hash;
		server 192.168.1.55:8080;
		server 192.168.1.55:8081;
	}	
```

## 策略

| 轮询       | 每个请求会按时间顺序逐一分配到不同的后端服务器               |
| ---------- | ------------------------------------------------------------ |
| weight     | weight参数用于指定轮询几率，weight的默认值为1, weight的数值与访问比率成正比, 权重越高分配到需要处理的请求越多 |
| ip_hash    | 基于客户端IP的分配方式，确保了相同的客户端的请求一直发送到相同的服务器，以保证session会话。每个访问都固定访问一个后端服务器，可以解决session不能跨服务器的问题 |
| least_conn | 把请求转发给连接数较少的服务器                               |
| fair(三方)   | 按照服务器端的响应时间来分配请求，响应时间短的优先分配       |
| url_hash   | 按访问url的hash结果来分配请求，使每个url定向到同一个后端服务器 |



# 使用建议

实际使用中, 至少有三个匹配规则定义:

```nginx
# 直接匹配网站根，通过域名访问网站首页比较频繁，使用这个会加速处理，官网如是说。
# 这里是直接转发给后端应用服务器了，也可以是一个静态首页
# 第一个必选规则
location = / {
  	proxy_pass http://tomcat:8080/index
}

# 第二个必选规则是处理静态文件请求，这是nginx作为http服务器的强项
# 有两种配置模式，目录匹配或后缀匹配, 任选其一或搭配使用
location ^~ /static/ {
  	root /webroot/static/;
}
location ~* \.(gif|jpg|jpeg|png|css|js|ico)$ {
  	root /webroot/res/;
}

# 第三个规则就是通用规则，用来转发动态请求到后端应用服务器
# 非静态文件请求就默认是动态请求，自己根据实际把握
# 目前带.php,.jsp后缀的情况很少了
location / {
  	proxy_pass http://tomcat:8080/
}
```



# windows的hosts文件

C:\Windows\System32\drivers\etc\hosts

```
192.168.5.55     xxx.xxx.com
```



# 详细配置参考

```
https://blog.csdn.net/wangbin_0729/article/details/82109693
```

```
https://blog.csdn.net/tjcyjd/article/details/50695922
```

