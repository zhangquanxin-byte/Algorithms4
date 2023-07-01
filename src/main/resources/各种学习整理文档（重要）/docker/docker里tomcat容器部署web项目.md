# docker里tomcat容器部署web项目

## 1、基本流程：

**1、运行一个容器：**

docker run -d --name tomcat1 -p 8092:8080 tomcat

--name 为容器取一个名字

-p 容器要映射的端口号

-d:后台运行

-t：进入终端

-i：获得一个交互式的连接，通过获取container的输入

**2、修改容器内webapps目录（最近版tomcat首页不在webapps下，而在webapps.dist下）**

进入容器：docker exec -it tomcat1 /bin/bash

​                  cd    /usr/local/tomcat

改文件名： mv webapps webapps2   

​					mv webapps.dist webapps

exit退出容器

**3、docker restart tomcat1** 

**4、页面访问192.168.5.4：8092看是否能访问tomcat首页**

5、复制自己的项目到tomcat容器

docker cp /usr/local/tomcat9/schoolProject.war b6b1bfe0504f:/usr/local/tomcat/webapps





解决docker容器内不能编辑文件：

更新来源

```
apt-get update1
```

安装vim

```javascript
apt-get install -y 
```





## 2、基本命令：

**启动，关闭和重启docker**

1.启动docker,命令:systemctl start docker

2.验证docker是否启动成功,命令:dockers version

3.重启docker,命令:systemctl restart docker

4.关闭docker,命令:systemctl stop docker END



**启动，关闭和重启docker**：

1、查看全部容器：docker ps -a

2、停止容器：docker stop  container_id

3、启动容器：docker start container_id

4、强制停止容器：docker kill container_id

5、删除容器：docker rm container_id

## 3、docker 查看容器启动日志

$ docker logs [OPTIONS] CONTAINER
Options:
--details 显示更多的信息
-f, --follow 跟踪实时日志
--since string 显示自某个timestamp之后的日志，或相对时间，如40m（即40分钟）
--tail string 从日志末尾显示多少行日志， 默认是all
-t, --timestamps 显示时间戳
--until string 显示自某个timestamp之前的日志，或相对时间，如40m（即40分钟）
案例：
查看最近30分钟的日志:

$ docker logs --since 30m CONTAINER_ID

查看某时间之后的日志：

$ docker logs -t --since="2019-11-02T13:23:37" CONTAINER_ID

查看某时间段日志：

$ docker logs -t --since="2019-11-02T13:23:37" --until "2019-11-03T12:23:37" CONTAINER_ID
