# 							SVN

## 1.svn常见操作：

a.发布项目(share project)：  项目组长 将本机的项目 第一次发布的中央仓库
b.下载项目（检出项目、check out）：组员  将中央仓库的项目 第一次想在到本机
c.提交(commit)：将本地修改的内容，同步到服务器中 （本地->服务器）
	-编写完某一个小功能、每天下班前 提交
d.更新(update)：将服务器中的最新代码  同步到本地，服务器->本地
	-编写功能之前、每天上班前更新

--编写之前先更新、写完之后立刻提交。

提交、更新：及时

![](D:\java oracle\学习资料\各种学习整理文档（重要）\svn_git\svn\asserts\svn.png)





## 2.svn安装

配置Path (bin)
验证：
svn --version



## 3.设置中央仓库

将本地目录设置为 中央仓库（保存项目的 各个历史版本）
svnadmin create D:\\svn



## 4.启动svn服务

### a.命令行方式

​	svnserve -d -r D:\\svn

### b.注册系统方式（推荐）

以管理员方式运行cmd
sc create svn binpath="D:\IdeaExtendsTools\TortoiseServer\bin\svnserve.exe --service -r D:\SVNServerRepo" start=auto depend=Tcpip

启动：  sc start 服务名
关闭： sc stop 服务名
删除：  sc delete 服务名



## 5.访问项目

### a.匿名访问

​	仓库\..\conf\svnserve.conf
开始匿名访问： 19行附近
​	anon-access = read注释打开 （注意，一定要顶格写，不要留空格）
anon-access = read 只读
anon-access = write可读可写
anon-access = none无权

### b.授权访问

svnserve.conf
20行附近 auth-access = write 注释打开
27行附近 password-db = passwd注释打开（表示 授权人的用户名密码 存放在 passwd 文件中）
36行附近 authz-db = authz 注释打开（表示 权限文件是 authz）

svnserve.conf
编写用户文件：
passwd:
[users]
用户名=密码

编写授权文件authz：
分组：[groups]
dev=zs,ls
权限：
[/]
@dev=rw
*=



## 6.Eclipse中使用SVN

在eclipse中安装svn插件
a.离线方式
eclipse_svn_site-1.10.5.zip 解压到 eclipse\dropins
b.在线方式
help->Eclipse Marketplace 搜：subversion /subeclipse

使用：
项目组长：发布项目
	右键要发布的项目-team- share project - svn - ....输入发布的地址  svn://ip地址 ...
	真正的发布/提交项目

组员：检出项目（下载）
	file-import-搜svn



更新：右键待更新的文件/项目： team-更新
提交：右键待提交的文件/项目： team-提交



黄色圆柱：本地无未提交代码
*/灰色箭头： 本地有未提交的代码
红色叹号：冲突

蓝色箭头：服务端有最新代码，本地还没有更新

修改svn用户名密码：
删除C:\Users\YANQUN\AppData\Roaming\Subversion\auth

冲突：

右键项目-与资源库同步
选中 有红色标识的文件，右键-编辑冲突 ->修改->右键->team->编辑为解决

冲突：  更新时或提交时 发现冲突->右键编辑冲突-> 更新提交



## 7.恢复/查看历史版本

选中需要恢复/查看的 文件- team- 如果要恢复成历史版本 （获取内容），如果此操作报错：解决方法：
svnserve.conf  文件中的 anon-access=none



## 8.svn发布到外网

a.   nat123等软件 将内网映射成外网
b.   租一台互联网服务器（新网、万网、阿里云），将项目发布到服务器中
c.  svn托管网站  http://www.svnchina.com/



## 9、如何在IDEA发布项目

### 9.1、将已有项目改为SVN管理

**在【属性】中选择【Version Control】 点击下方【加号 + 】**

![](D:\java oracle\学习资料\各种学习整理文档（重要）\svn_git\svn\asserts\setting-vcs.png)

**【目录】中选择当前项目，VCS 选择 【您的版本控制器，我这里是Subversion】**

![](D:\java oracle\学习资料\各种学习整理文档（重要）\svn_git\svn\asserts\setting-vcs2.png)

**点击【apply】 按钮**

![](D:\java oracle\学习资料\各种学习整理文档（重要）\svn_git\svn\asserts\settting-vcs3.png)

**变为SVN管理的项目，里面部分文件会变为深红色，如下图**

![](D:\java oracle\学习资料\各种学习整理文档（重要）\svn_git\svn\asserts\刚刚SVN管理的项目.png)



### 9.2、 将变为SVN管理的项目，提交到服务器

**鼠标右键 - 【Subversion】 - 【share Directory...】**

![](D:\java oracle\学习资料\各种学习整理文档（重要）\svn_git\svn\asserts\刚刚提交到服务器.png)

**选择将项目提交到哪个SVN下的哪个目录，如下图**

![](D:\java oracle\学习资料\各种学习整理文档（重要）\svn_git\svn\asserts\选择svn地址.png)

**和SVN建立完联系，项目文件的颜色又会发生变化哦~**

![](D:\java oracle\学习资料\各种学习整理文档（重要）\svn_git\svn\asserts\svn建立联系完毕.png)



### 9.3、 提交整个项目到SVN

![](D:\java oracle\学习资料\各种学习整理文档（重要）\svn_git\svn\asserts\整个项目提交.png)