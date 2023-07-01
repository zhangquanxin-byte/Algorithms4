#### 安装vsftpd

	yum install -y vsftpd 

#### 启动vsftpd

	service vsftpd start
	
	systemctl stop vsftpd.service
	systemctl start vsftpd.service
	systemctl status vsftpd.service
	systemctl enable vsftpd.service
	systemctl restart vsftpd.service

#### 查看监听21端口

	netstat -nltp | grep 21

#### 此时, 访问 ftp://IPaddr 就可浏览主机上的 /var/ftp目录了

 

#### 配置 FTP 权限

	vim /etc/vsftpd/vsftpd.conf, 找到下面配置并修改:
	
		# 禁用匿名用户  YES 改为 NO
	
		anonymous_enable=NO


#### 编辑完成后保存配置, 重新启动 FTP 服务

	systemctl restart vsftpd.service

 






#### 创建 FTP 用户 

	useradd ftpuser

#### 设置密码

	passwd ftpuser

#### 限制用户 ftpuser 只能通过 FTP 访问服务器, 而不能直接登录服务器

	usermod -s /sbin/nologin ftpuser

 






#### 为用户分配主目录

	/data/ftp 为主目录
	
	/data/ftp/images 文件只能上传到该目录下

#### 在/data中创建相关的目录

	mkdir -p /data/ftp/images

#### 设置访问权限

	chmod 777 /data -R

#### 设置为用户的主目录

	usermod -d /data/ftp ftpuser

#### 如果需要使用root登录连接FTP服务
需要配置 /etc/vsftpd/user_list 和 /etc/vsftpd/ftpusers, 将文件中的root注释

```
vim /etc/pam.d/vsftpd

#auth       required	pam_shells.so
```



```
vim  /etc/selinux/config

设置 "SELINUX=disabled", 保存退出
```

