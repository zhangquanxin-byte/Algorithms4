## 1、堆栈中各自存放的数据

### 存储器：

\1.寄存器：最快的存储区, 由编译器根据需求进行分配,我们在程序中无法控制.

\2. 栈：存放基本类型的变量数据和对象的引用，但对象本身不存放在栈中，而是存放在堆（new 出来的对象）或者常量池中（字符串常量对象存放在常量池中。）
\3. 堆：存放所有new出来的对象。
\4. 静态域：存放静态成员（static定义的）
\5. 常量池：存放字符串常量和基本类型常量（public static final）。
\6. 非RAM存储：硬盘等永久存储空间



### 字符串:

这里我们主要关心栈，堆和常量池，对于栈和常量池中的对象可以共享，对于堆中的对象不可以共享。栈中的数据大小和生命周期是可以确定的，当没有引用指向数据时，这个数据就会消失。堆中的对象的由垃圾回收器负责回收，因此大小和生命周期不需要确定，具有很大的灵活性。
对于字符串：其对象的引用都是存储在栈中的，如果是编译期已经创建好(直接用双引号定义的)的就存储在常量池中，如果是运行期（new出来的）才能确定的就存储在堆中。对于equals相等的字符串，在常量池中永远只有一份，在堆中有多份。
如以下代码：

Java代码 ![收藏代码](http://www.iteye.com/images/icon_star.png)

1. String s1 = "china"; 
2. String s2 = "china"; 
3. String s3 = "china"; 
4. String ss1 = new String("china"); 
5. String ss2 = new String("china"); 
6. String ss3 = new String("china"); 


![img](http://dl.iteye.com/upload/attachment/331454/3588b3c6-f37b-3d63-a48f-59134ea691d2.png)


这里解释一下黄色这3个箭头，对于通过new产生一个字符串（假设为”china”）时，会先去常量池中查找是否已经有了”china”对象，如果没有则在常量池中创建一个此字符串对象，然后堆中再创建一个常量池中此”china”对象的拷贝对象。这也就是有道面试题：String s = new String(“xyz”);产生几个对象？一个或两个，如果常量池中原来没有”xyz”,就是两个。



### 基础类型的变量和常量:

基础类型的变量和值都存在栈。常量：引用存储在栈中，常量存储在常量池中。
如以下代码：

Java代码 ![收藏代码](http://www.iteye.com/images/icon_star.png)

1. int i1 = 9; 
2. int i2 = 9; 
3. int i3 = 9;  
4. public static final int INT1 = 9; 
5. public static final int INT2 = 9; 
6. public static final int INT3 = 9; 



![img](http://dl.iteye.com/upload/attachment/229942/3968b51b-0a56-3ad6-a54e-b2b19e671526.png)



### 成员变量和局部变量

对于成员变量和局部变量：成员变量就是方法外部，类的内部定义的变量；局部变量就是方法或语句块内部定义的变量。局部变量必须初始化。
形式参数是局部变量，局部变量的数据存在于栈内存中。栈内存中的局部变量随着方法的消失而消失。
成员变量存储在堆中的对象里面，由垃圾回收器负责回收。
如以下代码：

Java代码 ![收藏代码](http://www.iteye.com/images/icon_star.png)

1. class BirthDate { 
2.   private int day; 
3.   private int month; 
4.   private int year;   
5.   public BirthDate(int d, int m, int y) { 
6. ​    day = d;  
7. ​    month = m;  
8. ​    year = y; 
9.   } 
10.   省略get,set方法……… 
11. } 
12.  
13. public class Test{ 
14.   public static void main(String args[]){ 
15. int date = 9; 
16. ​    Test test = new Test();    
17. ​      test.change(date);  
18. ​    BirthDate d1= new BirthDate(7,7,1970);     
19.   }  
20.  
21.   public void change1(int i){ 
22. ​    i = 1234; 
23.   } 



}

![img](http://dl.iteye.com/upload/attachment/229944/5d8dee1f-ceb9-3705-8924-161dd7599f73.png)
对于以上这段代码，date为局部变量，i,d,m,y都是形参为局部变量，day，month，year为成员变量。下面分析一下代码执行时候的变化：
\1. main方法开始执行：int date = 9;
date局部变量，基础类型，引用和值都存在栈中。
\2. Test test = new Test();
test为对象引用，存在栈中，对象(new Test())存在堆中。
\3. test.change(date);
i为局部变量，引用和值存在栈中。当方法change执行完成后，i就会从栈中消失。
\4. BirthDate d1= new BirthDate(7,7,1970); 
d1为对象引用，存在栈中，对象(new BirthDate())存在堆中，其中d，m，y为局部变量存储在栈中，且它们的类型为基础类型，因此它们的数据也存储在栈中。day,month,year为成员变量，它们存储在堆中(new BirthDate()里面)。当BirthDate构造方法执行完之后，d,m,y将从栈中消失。
5.main方法执行完之后，date变量，test，d1引用将从栈中消失，new Test(),new BirthDate()将等待垃圾回收。



## 2、session对象及其常用方法

### session基本概念

session对象用于在会话范围内，记录每个客户端的访问状态，以便于跟踪每个客户端的操作状态，在会话存储的信息，在浏览器发出后续请求时可以获取这些会话的有效数据。 

在jsp页面中可以直接使用session对象（jsp的内置对象），也可以通过pageContext.getSession()或者request.getSession重新回去session对象。 

session可以保存用户的信息和实现购物车等功能。 

HTTP协议是一种无状态协议，客户向服务器发出的请求request，然后服务器返回响应response，连接就被关闭了，在服务器不保存连接的有关信息，因此在下一次连接时，服务器已经没有以前的连接信息了，无法判断这一次连接和上一次连接时同一个客户信息，因此，必须使用会话记录有关连接的信息。 

从客户打开浏览器连接到服务器，到客户关闭浏览器离开这个服务器，称做一个会话。当客户访问服务器是，可能会反复连接这个服务器上的几个页面、反复刷新一个页面或不断地向一个页面提交信息等，服务器应当通过某种方法知道这是同一个客户，这时就需要session对象。 

### session的工作原理： 

1、客户首次访问服务器的一个页面时，服务器就会为该用户分配一个session对象，同时为这个session指定唯一的ID，并且将该ID发送到客户端并写入到cookie中，使得客户端与服务器的session建立一一对应的关系； 

2、当客户端继续访问服务器端的其它资源时，服务器不再为该客户分配新的session对象，直到客户端浏览器关闭、超时或调用session的invalidate（）方法使其失效，客户端与服务器的会话结束。 

3、当客户重新打开浏览器访问网站时，服务器会重新为客户分配一个session对象，并重新分配sessionID。 

### session对象主要用于属性操作和会话管理，常用方法： 

1、public void setAttribute(String name,String value)设定指定名字的属性的值，并将它添加到session会话范围内，如果这个属性是会话范围内存在，则更改该属性的值。 

2、public Object getAttribute(String name)在会话范围内获取指定名字的属性的值，返回值类型为object，如果该属性不存在，则返回null。 

3、public void removeAttribute(String name)，删除指定名字的session属性，若该属性不存在，则出现异常。 

4、public void invalidate（），使session失效。可以立即使当前会话失效，原来会话中存储的所有对象都不能再被访问。 

5、public String getId( )，获取当前的会话ID。每个会话在服务器端都存在一个唯一的标示sessionID，session对象发送到浏览器的唯一数据就是sessionID，它一般存储在cookie中。 

6、public void setMaxInactiveInterval(int interval) 设置会话的最大持续时间，单位是秒，负数表明会话永不失效。 

7、public int getMaxInActiveInterval（）,获取会话的最大持续时间。 

8、使用session对象的getCreationTime()和getLastAccessedTime()方法可以获取会话创建的时间和最后访问的时间，但其返回值是毫秒，一般需要使用下面的转换来获取具体日期和时间。 

   Date creationTime = new Date(session.getCreationTime()); 

  Date accessedTime = new Date(session.getLastAccessedTime()); 

获取session  HttpServletRequest request ， HttpSession session = request.getSession ();

9、session.invalidate ();//清空session



## 3、关于java锁的详解:

https://www.cnblogs.com/jyroy/p/11365935.html



## 4、线程池：

### Java线程池中线程的生命周期

**设：我们有一个coreSize=10，maxSize=20，keepAliveTime=60s，queue=40**
1、池初始化时里面没有任何线程。
2、当有一个任务提交到池就创建第一个线程。
3、若继续提交任务，有空闲线程就调拨空闲线程来处理任务？若没有线程空闲则再新建一个线程来处理，如此直到coreSize。【**预热阶段**】
4、若继续提交任务，有空闲线程就调拨空闲线程来处理任务，如果没有空闲线程（10个）则将任务缓存到queue中排队等待。
5、若继续提交任务，而已有线程不空闲，且queue也满了，则新建线程，并将**最新的**任务优先提交给新线程处理。
6、若继续提交任务，且所有线程（20个）仍不空闲，queue也是满的，此时就会触发池的拒绝机制。
8、一旦有任何线程空闲下来就会从queue中消费任务，直到queue中任务被消费完。
9、当总忙碌线程个数不超过coreSize时，闲暇线程休息keepAliveTime过后会被销毁。
10、而池中一直保留coreSize个线程存活。



## 5、廖雪峰的java 讲解：

https://www.liaoxuefeng.com/wiki/1252599548343744/1309138673991714



![img](https:////upload-images.jianshu.io/upload_images/1870221-f658a0eaa4e1a6e0.png?imageMogr2/auto-orient/strip|imageView2/2/w/982/format/webp)

image.png



## 6、什么是面向对象

#### 面向对象的三大特性：

1、封装
 隐藏对象的属性和实现细节，仅对外提供公共访问方式，将变化隔离，便于使用，提高复用性和安全性。
 2、继承
 提高代码复用性；继承是多态的前提。
 3、多态
 父类或接口定义的引用变量可以指向子类或具体实现类的实例对象。提高了程序的拓展性。

#### 五大基本原则：

1、单一职责原则SRP(Single Responsibility Principle)
 类的功能要单一，不能包罗万象，跟杂货铺似的。
 2、开放封闭原则OCP(Open－Close Principle)
 一个模块对于拓展是开放的，对于修改是封闭的，想要增加功能热烈欢迎，想要修改，哼，一万个不乐意。
 3、里式替换原则LSP(the Liskov Substitution Principle LSP)
 子类可以替换父类出现在父类能够出现的任何地方。比如你能代表你爸去你姥姥家干活。哈哈~~
 4、依赖倒置原则DIP(the Dependency Inversion Principle DIP)
 高层次的模块不应该依赖于低层次的模块，他们都应该依赖于抽象。抽象不应该依赖于具体实现，具体实现应该依赖于抽象。就是你出国要说你是中国人，而不能说你是哪个村子的。比如说中国人是抽象的，下面有具体的xx省，xx市，xx县。你要依赖的是抽象的中国人，而不是你是xx村的。
 5、接口分离原则ISP(the Interface Segregation Principle ISP)
 设计时采用多个与特定客户类有关的接口比采用一个通用的接口要好。就比如一个手机拥有打电话，看视频，玩游戏等功能，把这几个功能拆分成不同的接口，比在一个接口里要好的多。



## 7、正则表达式预编译功能的正确使用

在使用正则表达式时，利用好其预编译功能，可以有效加快正则匹配速度。

同时，Pattern要定义为static final静态变量，以避免执行多次预编译。

下面，我们列举两类使用正则的场景，来具体说明预编译该如何使用：

【错误用法】

// 没有使用预编译
private void func(...) {
    if (Pattern.matches(regexRule, content)) {
        ...
    }
}
// 多次预编译
private void func(...) {
    Pattern pattern = Pattern.compile(regexRule);
    Matcher m = pattern.matcher(content);
    if (m.matches()) {
        ...
    }
}
【正确用法】

private static final Pattern pattern = Pattern.compile(regexRule);

private void func(...) {
    Matcher m = pattern.matcher(content);
    if (m.matches()) {
        ...
    }
}
大家理解了吗，正则的预编译主要注意两点：

1.  Pattern 表达式要提前定义，不要再需要的地方临时定义；

2. Pattern 表达式要定义为 static final 静态变量，以避免执行多次预编译。

## 8、与（&）、非（~）、或（|）、异或（^）

如果以开关开灯论：
有这样两个开关，0为开关关闭，1为开关打开。

与（&）运算
与运算进行的是这样的算法：

0&0=0,0&1=0,1&0=0,1&1=1
1
在与运算中两个开关是串联的，如果我们要开灯，需要两个开关都打开灯才会打开。
理解为A与B都打开，则开灯，所以是1&1=1
任意一个开关没打开，都不开灯，所以其他运算都是0

通俗理解为A（与）&B都开则开，否则关

非（~）运算
非运算即取反运算，在二进制中1变0,0变1
110101进行非运算后为
001010即1010

或（|）运算
或运算进行的是这样的算法：

0|0=0,0|1=1,1|0=1,1|1=1
1
在或运算中两个开关是并联的，即一个开关开，则灯开。
如果任意一个开关开了，灯都会亮。
只有当两个开关都是关的，灯才不开。

理解为A（或）|B任意开则开

异或（^）运算
异或运算通俗地讲就是一句话
同为假，异为真
所以它是这样的算法:

0^0=0,0^1=1,1^0=1,1^1=0

## 9、阻塞IO 和 非阻塞IO ||同步IO 和 非同步IO

如果操作系统没有发现有套接字从指定的端口X来，那么操作系统就会等待。这样serverSocket.accept()方法就会一直等待。这就是为什么accept()方法为什么会阻塞：它内部的实现是使用的操作系统级别的同步IO。

阻塞IO 和 非阻塞IO 这两个概念是程序级别的。主要描述的是程序请求操作系统IO操作后，如果IO资源没有准备好，那么程序该如何处理的问题：前者等待；后者继续执行（并且使用线程一直轮询，直到有IO资源准备好了）

同步IO 和 非同步IO，这两个概念是操作系统级别的。主要描述的是操作系统在收到程序请求IO操作后，如果IO资源没有准备好，该如何相应程序的问题：前者不响应，直到IO资源准备好以后；后者返回一个标记（好让程序和自己知道以后的数据往哪里通知），当IO资源准备好以后，再用事件机制返回给程序。



## 10、java编译和反编译方法

编译：

javac  -encoding utf-8    java文件路径

编译时缺少依赖包的解决方法

1、可以将缺少的jar包放到JDK目录下的lib目录，再重新编译(如果缺的是自己编写的class文件，处理方法相同)。

2、如果还是识别不到，则需要配置下环境变量。右键我的电脑→属性→高级系统设置→高级→环境变量→系统变量，在classpath的值最后面加上对应包的路径，即刚放在lib目录的路径，再重新编译即可。如下：



反编译：

Java Decompiler工具





## 11、jar压缩和还原

1. jar包压缩

jar包解压之后 修改文件之后 在不依靠开发工具的情况下重新压缩jar包 打包命令如下：

**jar -cvf0M xxx.jar BOOT-INF/ META-INF/ org/**



2、jar解压

winrar直接解压





## 12、Java volatile关键字

### 1、并发编程的3个基本概念

1.原子性
     定义： 即一个操作或者多个操作 要么全部执行并且执行的过程不会被任何因素打断，要么就都不执行。

原子性是拒绝多线程操作的，不论是多核还是单核，具有原子性的量，同一时刻只能有一个线程来对它进行操作。简而言之，在整个操作过程中不会被线程调度器中断的操作，都可认为是原子性。例如 a=1是原子性操作，但是a++和a +=1就不是原子性操作。Java中的原子性操作包括：

（1）基本类型的读取和赋值操作，且赋值必须是值赋给变量，变量之间的相互赋值不是原子性操作。

（2）所有引用reference的赋值操作

（3）java.concurrent.Atomic.* 包中所有类的一切操作

2.可见性
   定义：指当多个线程访问同一个变量时，一个线程修改了这个变量的值，其他线程能够立即看得到修改的值。

   在多线程环境下，一个线程对共享变量的操作对其他线程是不可见的。Java提供了volatile来保证可见性，当一个变量被volatile修饰后，表示着线程本地内存无效，当一个线程修改共享变量后他会立即被更新到主内存中，其他线程读取共享变量时，会直接从主内存中读取。当然，synchronize和Lock都可以保证可见性。synchronized和Lock能保证同一时刻只有一个线程获取锁然后执行同步代码，并且在释放锁之前会将对变量的修改刷新到主存当中。因此可以保证可见性。

3.有序性
   定义：即程序执行的顺序按照代码的先后顺序执行。

   Java内存模型中的有序性可以总结为：如果在本线程内观察，所有操作都是有序的；如果在一个线程中观察另一个线程，所有操作都是无序的。前半句是指“线程内表现为串行语义”，后半句是指“指令重排序”现象和“工作内存主主内存同步延迟”现象。

   在Java内存模型中，为了效率是允许编译器和处理器对指令进行重排序，当然重排序不会影响单线程的运行结果，但是对多线程会有影响。Java提供volatile来保证一定的有序性。最著名的例子就是单例模式里面的DCL（双重检查锁）。另外，可以通过synchronized和Lock来保证有序性，synchronized和Lock保证每个时刻是有一个线程执行同步代码，相当于是让线程顺序执行同步代码，自然就保证了有序性。


### 2、volatile变量的特性

 1.保证可见性，不保证原子性
  （1）当写一个volatile变量时，JMM会把该线程本地内存中的变量强制刷新到主内存中去；

  （2）这个写会操作会导致其他线程中的volatile变量缓存无效。

 2.禁止指令重排
    重排序是指编译器和处理器为了优化程序性能而对指令序列进行排序的一种手段。重排序需要遵守一定规则：

 （1）重排序操作不会对存在数据依赖关系的操作进行重排序。

　 比如：a=1;b=a; 这个指令序列，由于第二个操作依赖于第一个操作，所以在编译时和处理器运行时这两个操作不会被重排序。

 （2）重排序是为了优化性能，但是不管怎么重排序，单线程下程序的执行结果不能被改变

　 	比如：a=1;b=2;c=a+b这三个操作，第一步（a=1)和第二步(b=2)由于不存在数据依赖关系， 所以可能会发生重排序，但是c=a+b这个操作是不会被重排序的，因为需要保证最终的结果一定是c=a+b=3。

​		重排序在单线程下一定能保证结果的正确性，但是在多线程环境下，可能发生重排序，影响结果，下例中的1和2由于不存在数据依赖关系，则有可能会被重排序，先执行status=true再执行a=2。而此时线程B会顺利到达4处，而线程A中a=2这个操作还未被执行，所以b=a+1的结果也有可能依然等于2。



	public class TestVolatile{
		int a = 1;
		boolean status = false;
	
	//状态切换为true
	public void changeStatus{
		a = 2;   //1
		status = true;  //2
	}
	
	//若状态为true，则为running
	public void run(){
		if(status){   //3
			int b = a + 1;  //4
			System.out.println(b);
		}
	}

}
     使用volatile关键字修饰共享变量便可以禁止这种重排序。若用volatile修饰共享变量，在编译时，会在指令序列中插入内存屏障来禁止特定类型的处理器重排序,volatile禁止指令重排序也有一些规则：

     a.当程序执行到volatile变量的读操作或者写操作时，在其前面的操作的更改肯定全部已经进行，且结果已经对后面的操作可见；在其后面的操作肯定还没有进行；
    
     b.在进行指令优化时，不能将对volatile变量访问的语句放在其后面执行，也不能把volatile变量后面的语句放到其前面执行。
    
     即执行到volatile变量时，其前面的所有语句都执行完，后面所有语句都未执行。且前面语句的结果对volatile变量及其后面语句可见。
### 3、volatile不适用的场景

**1.volatile不适合复合操作**

那为啥Volatile修饰的变量i++却会有并发问题呢？

因为i++并不是原子操作，

i++是有两步操作的，比如 i=0; i++

1.读取i=0

2.计算i+1,然后赋值给i

那么可能存在2个线程同时读取到i=0，并计算出结果i=1然后赋值给I

那么就得不到预期结果i=2。

就是说虽然Volatile修饰的变量的变化可以被其他线程看到，但是如果同时去读这个变量，然后进行写操作，则仍会导致线程安全问题。

**2**.**解决方法**

 （1）采用synchronized

 （2）采用Lock

（3）采用java并发包中的原子操作类，原子操作类是通过CAS循环的方式来保证其原子性的



### 4、volatile原理

  volatile可以保证线程可见性且提供了一定的有序性，但是无法保证原子性。在JVM底层volatile是采用“内存屏障”来实现的。观察加入volatile关键字和没有加入volatile关键字时所生成的汇编代码发现，加入volatile关键字时，会多出一个lock前缀指令，lock前缀指令实际上相当于一个内存屏障（也成内存栅栏），内存屏障会提供3个功能：

（1）它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，也不会把前面的指令排到内存屏障的后面；即在执行到内存屏障这句指令时，在它前面的操作已经全部完成；

（2）它会强制将对缓存的修改操作立即写入主存；

（3）如果是写操作，它会导致其他CPU中对应的缓存行无效。



### 5、单例模式的双重锁为什么要加volatile

	
	public class TestInstance{
		private volatile static TestInstance instance;
		public static TestInstance getInstance(){        //1
			if(instance == null){                        //2
				synchronized(TestInstance.class){        //3
					if(instance == null){                //4
						instance = new TestInstance();   //5
					}
				}
			}
			return instance;                             //6
		}
	}



​         需要volatile关键字的原因是，在并发情况下，如果没有volatile关键字，在第5行会出现问题。instance = new TestInstance();可以分解为3行伪代码

a. memory = allocate() //分配内存

b. ctorInstanc(memory) //初始化对象

c. instance = memory //设置instance指向刚分配的地址
   上面的代码在编译运行时，可能会出现重排序从a-b-c排序为a-c-b。在多线程的情况下会出现以下问题。当线程A在执行第5行代码时，B线程进来执行到第2行代码。假设此时A执行的过程中发生了指令重排序，即先执行了a和c，没有执行b。那么由于A线程执行了c导致instance指向了一段地址，所以B线程判断instance不为null，会直接跳到第6行并返回一个未初始化的对象。

volatile禁止对象创建时指令之间重排序，所以其他线程不会访问到一个未初始化的对象，从而保证安全性