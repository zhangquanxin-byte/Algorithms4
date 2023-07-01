# 						IO和NIO

## 1、Java流的分类

Java中的流，可以从不同的角度进行分类。

按照数据流的方向不同可以分为：输入流和输出流。

按照处理数据单位不同可以分为：字节流和字符流。

按照实现功能不同可以分为：节点流和处理流。



字节流：一次读入或读出是8位二进制。

字符流：一次读入或读出是16位二进制。

字节流和字符流的原理是相同的，只不过处理的单位不同而已。后缀是Stream是字节流，而后缀是Reader，Writer是字符流。



## 2、IO流集合

![](D:\java oracle\学习资料\各种学习整理文档（重要）\java 基础\IO和NIO\photos\io流集合.png)

InputStream(字节输入流)，OutputStream（字节输出流），Reader（字符输入流），Writer（字符输出流）

是所有流的基类



## 3、基础IO流使用详解：

### 3.1、Writer:字符输出流的基类

`java.io.Writer`抽象类是**字符输出流**的所有类的**超类**（父类），将指定的字符信息写出到目的地。它定义了字符输出流的基本共性功能方法。

**字符输出流的基本共性功能方法**：

```
1、void write(int c) 写入单个字符。
2、void write(char[] cbuf)写入字符数组。
3、 abstract void write(char[] cbuf, int off, int len)写入字符数组的某一部分,off数组的开始索引,len写的字符个数。
4、 void write(String str)写入字符串。
5、void write(String str, int off, int len) 写入字符串的某一部分,off字符串的开始索引,len写的字符个数。
6、void flush()刷新该流的缓冲。
7、void close() 关闭此流，但要先刷新它。
```

**强调：
字符流，只能操作文本文件，不能操作图片，视频等非文本文件。当我们单纯读或者写文本文件时 使用字符流 其他情况使用字节流**



#### 3.1.1、FileWriter:

`java.io.FileWriter`类是写出字符到文件的便利类。构造时使用系统默认的字符编码和默认字节缓冲区。

构造方法：

```
FileWriter(File file)  给一个File对象构造一个FileWriter对象。  
FileWriter(File file, boolean append)  给一个File对象构造一个FileWriter对象。  
FileWriter(FileDescriptor fd)  构造与文件描述符关联的FileWriter对象。  
FileWriter(String fileName)  构造一个给定文件名的FileWriter对象。  
FileWriter(String fileName, boolean append)  构造一个FileWriter对象，给出一个带有布尔值的文件名，表示是否附加写入的数据。 
```



构造举例，代码如下：

```java
public class FileWriterConstructor {
    public static void main(String[] args) throws IOException {
   	 	// 第一种：使用File对象创建流对象
        File file = new File("a.txt");
        FileWriter fw = new FileWriter(file);
      
        // 第二种：使用文件名称创建流对象
        FileWriter fw = new FileWriter("b.txt");
    }
}
```



方法列表：

```
Writer append(CharSequence csq) 将指定的字符序列附加到此作者。 
Writer append(CharSequence csq,int start,int end) 将指定字符序列的子序列附加到此作者。 
void close() 关闭流，先刷新。 
void flush() 刷新流。 
String getEncoding() 返回此流使用的字符编码的名称。 
void write(char[] cbuf,int off,int len) 写入字符数组的一部分。 
void write(int c) 写一个字符 
void write(String str,int off,int len) 写一个字符串的一部分。 
```



示例：

例1：创建一个文件demo.txt，然后向里面写入字符串abcde

```
import java.io.*;
class  FileWriterDemo
{
    public static void main(String[] args)throws IOException
    {
        //创建一个FileWriter对象。该对象一被初始化就必须要明确被操作的文件。
        //而且该文件会被创建到指定目录下。如果该目录下已有同名文件，将被覆盖。
        //其实该步就是在明确数据要存放的目的地。
        //传递一个true参数，代表不覆盖已有的文件。并在已有文件的末尾处进行数据续写。
        FileWriter fw =new FileWriter("demo.txt",true);
 
        //调用write方法，将字符串写入到流中。
        fw.write("abcde");
 
        //刷新流对象中的缓冲中的数据。
        //将数据刷到目的地中。
        //fw.flush();
 
 
        //关闭流资源，但是关闭之前会刷新一次内部的缓冲中的数据。
        //将数据刷到目的地中。
        //和flush区别：flush刷新后，流可以继续使用，close刷新后，会将流关闭。
        fw.close();
    }
}
```

IO异常的处理方式。

```
import java.io.*;
 
class  FileWriterDemo2
{
    public static void main(String[] args)
    {
        FileWriter fw =null;
        try
        {
            fw =new FileWriter("demo.txt");//可能会有异常产生
            fw.write("abcdefg");
 
        }
        catch (IOException e)
        {
            System.out.println("catch:"+e.toString());
        }
        finally
        {
            try
            {
                if(fw!=null)
                    fw.close();//可能会有异常产生              
            }
            catch (IOException e)
            {
                System.out.println(e.toString());
            }
             
        }      
 
    }
}
```



FileReader和FileWriter类完成文本文件复制

```
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CopyFile {
    public static void main(String[] args) throws IOException {
        //创建输入流对象
        FileReader fr=new FileReader("F:\\新建文件夹\\aa.txt");//文件不存在会抛出java.io.FileNotFoundException
        //创建输出流对象
        FileWriter fw=new FileWriter("C:\\copyaa.txt");
        /*创建输出流做的工作：
         *      1、调用系统资源创建了一个文件
         *      2、创建输出流对象
         *      3、把输出流对象指向文件        
         * */
        //文本文件复制，一次读一个字符
        copyMethod1(fr, fw);
        //文本文件复制，一次读一个字符数组
        copyMethod2(fr, fw);
        
        fr.close();
        fw.close();
    }

    public static void copyMethod1(FileReader fr, FileWriter fw) throws IOException {
        int ch;
        while((ch=fr.read())!=-1) {//读数据
            fw.write(ch);//写数据
        }
        fw.flush();
    }

    public static void copyMethod2(FileReader fr, FileWriter fw) throws IOException {
        char chs[]=new char[1024];
        int len=0;
        while((len=fr.read(chs))!=-1) {//读数据
            fw.write(chs,0,len);//写数据
        }
        fw.flush();
    }
}

```



#### BufferFile:

构造方法：

```
BufferedWriter(Writer out)  创建使用默认大小的输出缓冲区的缓冲字符输出流。  
BufferedWriter(Writer out, int sz) 创建一个新的缓冲字符输出流，使用给定大小的输出缓冲区。  
```



方法：

```
void close()  关闭流，先刷新。  
void flush()  刷新流。  
void newLine()  写一行行分隔符。  
void write(char[] cbuf, int off, int len)  写入字符数组的一部分。  
void write(int c)  写一个字符  
void write(String s, int off, int len)  写一个字符串的一部分。  

```



示例：

```
import java.io.*;
class BufferWriterDemo{
    public static void main(String[] args)throws IOException {
        //创建一个字符写入流对象。
        FileWriter fw =new FileWriter("buf.txt");
        //为了提高字符写入流效率。加入了缓冲技术。
    //只要将需要被提高效率的流对象作为参数传递给缓冲区的构造函数即可。
        BufferedWriter bw=new BufferedWriter(fw);
        char[] c={'a','b','c','d','e'};
        bw.write(c,0,4);
 
        //换行
        bw.newLine();
 
        //再次写入
        bw.write(c,2,2);
 
        //刷新流
        bw.flush();
        //其实关闭缓冲区，就是在关闭缓冲区中的流对象。
        bw.close();
    }
}
```





#### CharArrayWriter：

构造方法：

```
CharArrayWriter()  创建一个新的CharArrayWriter。  
CharArrayWriter(int initialSize)  用指定的初始大小创建一个新的CharArrayWriter。  

```



方法：

```
CharArrayWriter append(char c)  将指定的字符附加到此writer。  
CharArrayWriter append(CharSequence csq)  将指定的字符序列附加到此writer。  
CharArrayWriter append(CharSequence csq, int start, int end)  将指定字符序列的子序列附加到此writer。  
void close()  关闭流。  
void flush() 冲洗流。  
void reset()  重置缓冲区，以便您可以再次使用它，而不会丢弃已经分配的缓冲区。  
int size()  返回缓冲区的当前大小。  
char[] toCharArray()  返回输入数据的副本。  
String toString()  将输入数据转换为字符串。  
void write(char[] c, int off, int len)  将字符写入缓冲区。  
void write(int c)  将一个字符写入缓冲区。  
void write(String str, int off, int len)  将一部分字符串写入缓冲区。  
void writeTo(Writer out)  将缓冲区的内容写入另一个字符流。  

```



#### FilterWriter

#### PrintWriter

 是字符类型的打印输出流，它继承于Writer





### 3.2、Reader:字符输入流的基类

#### FileReader：

构造方法：

```
FileReader(File file)  创建一个新的 FileReader ，给出 File读取。  
FileReader(FileDescriptor fd)  创建一个新的 FileReader ，给定 FileDescriptor读取。  
FileReader(String fileName)  创建一个新的 FileReader ，给定要读取的文件的名称。  
```



构造方法示例：

```
public class FileReaderConstructor throws IOException{
    public static void main(String[] args) {
   	 	// 使用File对象创建流对象
        File file = new File("a.txt");
        FileReader fr = new FileReader(file);
      
        // 使用文件名称创建流对象
        FileReader fr = new FileReader("b.txt");
    }
}
```



**FileReader读取字符数据**

```
读取字符：read方法，每次可以读取一个字符的数据，提升为int类型，读取到文件末尾，返回-1，循环读取，代码使用演示：

public class FRRead {
    public static void main(String[] args) throws IOException {
      	// 使用文件名称创建流对象
       	FileReader fr = new FileReader("a.txt");
      	// 定义变量，保存数据
        int b ；
        // 循环读取
        while ((b = fr.read())!=-1) {
            System.out.println((char)b);
        }
		// 关闭资源
        fr.close();
    }
}
```



示例：

```
import java.io.*;
 
class  FileReaderDemo
{
    public static void main(String[] args)throws IOException
    {
        //创建一个文件读取流对象，和指定名称的文件相关联。
        //要保证该文件是已经存在的，如果不存在，会发生异常FileNotFoundException
        FileReader fr =new FileReader("demo.txt");
 
        //调用读取流对象的read方法。
        //read():一次读一个字符。而且会自动往下读。
         
        int ch =0;
 
        while((ch=fr.read())!=-1)
        {
            System.out.println("ch="+(char)ch);
        }
 
        //关闭流
        fr.close();
 
    }
}
```



```
/*
第二种方式：通过字符数组进行读取。
*/
 
import java.io.*;
 
class FileReaderDemo2
{
    public static void main(String[] args)throws IOException
    {
        FileReader fr =new FileReader("demo.txt");
         
 
        //定义一个字符数组。用于存储读到字符。
        //该read(char[])返回的是读到字符个数。
        char[] buf =new char[1024];
 
        int num =0;
        while((num=fr.read(buf))!=-1)
        {
            System.out.println(new String(buf,0,num));
        }
         
 
        fr.close();
    }
}
```





#### BufferedReader：

构造方法：

```
BufferedReader(Reader in)  创建使用默认大小的输入缓冲区的缓冲字符输入流。  
BufferedReader(Reader in, int sz)  创建使用指定大小的输入缓冲区的缓冲字符输入流。  

```



方法摘要：

```
void close()  关闭流并释放与之相关联的任何系统资源。  
Stream<String> lines()  返回一个 Stream ，其元素是从这个 BufferedReader读取的行。  
void mark(int readAheadLimit)  标记流中的当前位置。  
boolean markSupported()  告诉这个流是否支持mark（）操作。  
int read()  读一个字符  
int read(char[] cbuf, int off, int len)  将字符读入数组的一部分。  
String readLine()  读一行文字。  
boolean ready()  告诉这个流是否准备好被读取。  
void reset()  将流重置为最近的标记。  
long skip(long n)  跳过字符  

```



示例：

```
/*
字符读取流缓冲区：
该缓冲区提供了一个一次读一行的方法 readLine，方便于对文本数据的获取。
当返回null时，表示读到文件末尾。
readLine方法返回的时候只返回回车符之前的数据内容。并不返回回车符。
*/
 
import java.io.*; 

class  BufferedReaderDemo
{
    public static void main(String[] args)throws IOException
    {
        //创建一个读取流对象和文件相关联。
        FileReader fr =new FileReader("buf.txt");
 
        //为了提高效率。加入缓冲技术。将字符读取流对象作为参数传递给缓冲对象的构造函数。
        BufferedReader bufr =new BufferedReader(fr);
         
        String line =null;
 
        while((line=bufr.readLine())!=null)
        {
            System.out.print(line);
        }
 
　　　　　　　　　//关闭流
        bufr.close();
    }
 
}
```



```
/*
通过缓冲区复制一个.java文件。
 
*/
import java.io.*;
 
class  CopyTextByBuf
{
    public static void main(String[] args)
    {
        BufferedReader bufr =null;
        BufferedWriter bufw =null;
 
        try
        {
            bufr =new BufferedReader(new FileReader("BufferedWriterDemo.java"));
            bufw =new BufferedWriter(new FileWriter("bufWriter_Copy.txt"));
 
            String line =null;
 
            while((line=bufr.readLine())!=null)
            {
                bufw.write(line);
                bufw.newLine();
                bufw.flush();
 
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("读写失败");
        }
        finally
        {
            try
            {
                if(bufr!=null)
                    bufr.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException("读取关闭失败");
            }
            try
            {
                if(bufw!=null)
                    bufw.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException("写入关闭失败");
            }
        }
    }
}
```





### 3.3、InputStream：字节输入流的父类

InputStream的常用方法：

```
int available() 从下一次调用此输入流的方法返回可从该输入流读取（或跳过）的字节数，而不会阻塞。 
void close() 关闭此输入流并释放与流相关联的任何系统资源。 
void mark(int readlimit) 标记此输入流中的当前位置。 
boolean markSupported​() 测试此输入流是否支持 mark和 reset方法。 
abstract int read() 从输入流读取数据的下一个字节。 
int read(byte[] b) 从输入流中读取一些字节数，并将它们存储到缓冲器阵列 b 。 
int read(byte[] b,int off,int len) 从输入流读取最多 len个字节的数据到字节数组。 
byte[] readAllBytes() 从输入流读取所有剩余字节。 
int readNBytes(byte[] b,int off,int len) 将所请求的字节数从输入流读入给定的字节数组。 
void reset() 将此流重新定位到最后在此输入流上调用 mark方法时的位置。 
long skip(long n) 跳过并丢弃来自此输入流的 n字节的数据。 
long transferTo(OutputStream out) 从该输入流中读取所有字节，并按读取的顺序将字节写入给定的输出流。
```



#### 3.2.1、FileInputStream

构造方法：

```
FileInputStream(File file)  通过打开与实际文件的连接创建一个 FileInputStream ，该文件由文件系统中的 File对象 file命名。  
FileInputStream(FileDescriptor fdObj)  创建 FileInputStream通过使用文件描述符 fdObj ，其表示在文件系统中的现有连接到一个实际的文件。  
FileInputStream(String name)  通过打开与实际文件的连接来创建一个 FileInputStream ，该文件由文件系统中的路径名 name命名。  
```



构造举例

```
public class FileInputStreamConstructor throws IOException{
    public static void main(String[] args) {
   	 	// 使用File对象创建流对象
        File file = new File("a.txt");
        FileInputStream fos = new FileInputStream(file);
      
        // 使用文件名称创建流对象
        FileInputStream fos = new FileInputStream("b.txt");
    }
}
```



**方法摘要**：

```
int available()  返回从此输入流中可以读取（或跳过）的剩余字节数的估计值，而不会被下一次调用此输入流的方法阻塞。  
void close()  关闭此文件输入流并释放与流相关联的任何系统资源。  
protected void finalize()  确保当这个文件输入流的 close方法没有更多的引用时被调用。  
FileChannel getChannel()  返回与此文件输入流相关联的唯一的FileChannel对象。  
FileDescriptor getFD()  返回表示与此 FileInputStream正在使用的文件系统中实际文件的连接的 FileDescriptor对象。  
int read()  从该输入流读取一个字节的数据。  
int read(byte[] b)  从该输入流读取最多 b.length个字节的数据为字节数组。  
int read(byte[] b, int off, int len)  从该输入流读取最多 len字节的数据为字节数组。  
long skip(long n)  跳过并从输入流中丢弃 n字节的数据。  
```



**FileInputStream读取字节数据**

```
public static void main(String[] args) throws IOException{
      	// 使用文件名称创建流对象
       	FileInputStream fis = new FileInputStream("read.txt");
      	// 定义变量，保存数据
        int b ；
        // 循环读取
        while ((b = fis.read())!=-1) {
            System.out.println((char)b);
        }
		// 关闭资源
        fis.close();
    }
}
输出结果：
a
b
c
d
e
```

**使用字节数组读取**

```
public class FISRead {
    public static void main(String[] args) throws IOException{
      	// 使用文件名称创建流对象.
       	FileInputStream fis = new FileInputStream("read.txt"); // read.txt文件中内容为abcde
      	// 定义变量，作为有效个数
        int len ；
        // 定义字节数组，作为装字节数据的容器   
        byte[] b = new byte[2];
        // 循环读取
        while (( len= fis.read(b))!=-1) {
           	// 每次读取后,把数组变成字符串打印
            System.out.println(new String(b));
        }
		// 关闭资源
        fis.close();
    }
}

输出结果：
ab
cd
ed

备注：由于read.txt文件中内容为abcde，而错误数据d，是由于最后一次读取时，只读取一个字节e，数组中，上次读取的数据没有被完全替换，所以要通过len ，获取有效的字节

public class FISRead {
    public static void main(String[] args) throws IOException{
      	// 使用文件名称创建流对象.
       	FileInputStream fis = new FileInputStream("read.txt"); // 文件中为abcde
      	// 定义变量，作为有效个数
        int len ；
        // 定义字节数组，作为装字节数据的容器   
        byte[] b = new byte[2];
        // 循环读取
        while (( len= fis.read(b))!=-1) {
           	// 每次读取后,把数组的有效字节部分，变成字符串打印
            System.out.println(new String(b，0，len));//  len 每次读取的有效字节个数
        }
		// 关闭资源
        fis.close();
    }
}

输出结果：
ab
cd
e
```



**复制图片文件**

```
public class Copy {
    public static void main(String[] args) throws IOException {
        // 1.创建流对象
        // 1.1 指定数据源
        FileInputStream fis = new FileInputStream("D:\\test.jpg");
        // 1.2 指定目的地
        FileOutputStream fos = new FileOutputStream("test_copy.jpg");

        // 2.读写数据
        // 2.1 定义数组
        byte[] b = new byte[1024];
        // 2.2 定义长度
        int len;
        // 2.3 循环读取
        while ((len = fis.read(b))!=-1) {
            // 2.4 写出数据
            fos.write(b, 0 , len);
        }

        // 3.关闭资源
        fos.close();
        fis.close();
    }
}

备注：复制文本、图片、mp3、视频等的方式一样。
```





### 3.4、OutputStream：字节输出流父类

常用方法

```
void close​() 关闭此输出流并释放与此流相关联的任何系统资源。 
void flush​() 刷新此输出流并强制任何缓冲的输出字节被写出。 
void write​(byte[] b) 将 b.length字节从指定的字节数组写入此输出流。 
void write​(byte[] b,int off,int len) 从指定的字节数组写入 len字节，从偏移量 off开始输出到此输出流。 
abstract void write​(int b) 将指定的字节写入此输出流。 
```



#### 3.4.1、FileOutputStream

构造方法：

```
FileOutputStream(File file)  创建文件输出流以写入由指定的 File对象表示的文件。  
FileOutputStream(File file, boolean append)  创建文件输出流以写入由指定的 File对象表示的文件。  
FileOutputStream(FileDescriptor fdObj)  创建文件输出流以写入指定的文件描述符，表示与文件系统中实际文件的现有连接。  
FileOutputStream(String name)  创建文件输出流以指定的名称写入文件。  
FileOutputStream(String name, boolean append)  创建文件输出流以指定的名称写入文件。  

```

```javascript
FileOutputStream outputStream = new FileOutputStream("abc.txt");
```

就以上面这句代码来讲，类似这样创建字节输出流对象都做了**三件事情**：
1、调用系统功能去创建文件【输出流对象才会自动创建】
2、创建outputStream对象
3、把foutputStream对象指向这个文件



**使用File对象创建流对象，写出数据**

```
 		 File file = new File("G:\\自动创建的文件夹\\a.txt");
 		// 使用文件名称创建流对象
        FileOutputStream fos = new FileOutputStream(file);     
      	// 写出数据
      	fos.write(97); // 写出第1个字节
      	fos.write(98); // 写出第2个字节
      	fos.write(99); // 写出第3个字节
      	// 关闭资源
        fos.close();
        
 输出结果：
abc       

备注：虽然参数为int类型四个字节，但是只会保留一个字节的信息写出。
流操作完毕后，必须释放系统资源，调用close方法，千万记得。
```



**使用名称创建流对象，写出字节数组**

```
 // 使用文件名称创建流对象
        FileOutputStream fos = new FileOutputStream("fos.txt");     
      	// 字符串转换为字节数组
      	byte[] b = "麻麻我想吃烤山药".getBytes();
      	// 写出字节数组数据
      	fos.write(b);
      	// 关闭资源
        fos.close();
    }

输出结果：
麻麻我想吃烤山药
```



#### FileOutputStream实现数据追加续写、换行

```

        // 使用文件名称创建流对象
        FileOutputStream fos = new FileOutputStream("fos.txt"，true);     
        // 定义字节数组
      	byte[] words = {97,98,99,100,101};
      	// 遍历数组
        for (int i = 0; i < words.length; i++) {
          	// 写出一个字节
            fos.write(words[i]);
          	// 写出一个换行, 换行符号转成数组写出
            fos.write("\r\n".getBytes());
        }
      	// 关闭资源
        fos.close();

输出结果：
a
b
c
d
e
```



### 3.5、File

##### 3.5.1、概述：

File 是“**文件**”和“**目录路径名**”的抽象表示形式。

`java.io.File` 类是专门对文件进行操作的类，只能对文件本身进行操作，不能对文件内容进行操作。
`java.io.File` 类是文件和目录路径名的抽象表示，主要用于文件和目录的创建、查找和删除等操作。

File 直接继承于Object，实现了Serializable接口和Comparable接口。实现Serializable接口，意味着File对象支持序列化操作。而实现Comparable接口，意味着File对象之间可以比较大小；File能直接被存储在有序集合(如TreeSet、TreeMap中)。



##### 3.5.2、构造方法：

```
File(File parent, String child)  从父抽象路径名和子路径名字符串创建新的 File实例。  
File(String pathname)  通过将给定的路径名字符串转换为抽象路径名来创建新的 File实例。  
File(String parent, String child)  从父路径名字符串和子路径名字符串创建新的 File实例。  
File(URI uri)  通过将给定的 file: URI转换为抽象路径名来创建新的 File实例。 


1. 一个File对象代表硬盘中实际存在的一个文件或者目录。
2.  File类构造方法不会给你检验这个文件或文件夹是否真实存在，因此无论该路径下是否存在文件或者目录，都不影响File对象的创建。
// 文件路径名 
String path = "D:\\123.txt";
File file1 = new File(path); 

// 文件路径名
String path2 = "D:\\1\\2.txt";
File file2 = new File(path2);     -------------相当于D:\\1\\2.txt

// 通过父路径和子路径字符串
 String parent = "F:\\aaa";
 String child = "bbb.txt";
 File file3 = new File(parent, child);  --------相当于F:\\aaa\\bbb.txt

// 通过父级File对象和子路径字符串
File parentDir = new File("F:\\aaa");
String child = "bbb.txt";
File file4 = new File(parentDir, child); --------相当于F:\\aaa\\bbb.txt

```



##### 3.5.3、示例：

```
import java.io.*;
 
/*
File类常见方法：
1，创建。
    boolean createNewFile():在指定位置创建文件，如果该文件已经存在，则不创建，返回false。
                        和输出流不一样，输出流对象一建立创建文件。而且文件已经存在，会覆盖。
 
    boolean mkdir():创建文件夹。
    boolean mkdirs():创建多级文件夹。
2，删除。
    boolean delete()：删除失败返回false。如果文件正在被使用，则删除不了返回falsel。
    void deleteOnExit();在程序退出时删除指定文件。
 
 
3，判断。
    boolean exists() :文件是否存在.
    isFile():
    isDirectory();
    isHidden();
    isAbsolute();
 
4，获取信息。
    getName():
    getPath():
    getParent():
 
    getAbsolutePath()
    long lastModified()
    long length()
 
*/
 
 
class FileDemo
{
    public static void main(String[] args)throws IOException
    {
        method_5();
    }
 
    public static void method_5()
    {
        File f1 =new File("c:\\Test.java");
        File f2 =new File("d:\\hahah.java");
 
        sop("rename:"+f2.renameTo(f1));
 
    }
 
    public static void method_4()
    {
        File f =new File("file.txt");
 
        sop("path:"+f.getPath());
        sop("abspath:"+f.getAbsolutePath());
        //该方法返回的是绝对路径中的父目录。如果获取的是相对路径，返回null。
        //如果相对路径中有上一层目录那么该目录就是返回结果。
        sop("parent:"+f.getParent());                      
 
 
    }
     
    public static void method_3()throws IOException
    {
        File f =new File("d:\\java1223\\day20\\file2.txt");
        //f.createNewFile();
 
        //f.mkdir();
 
        //记住在判断文件对象是否是文件或者目的时，必须要先判断该文件对象封装的内容是否存在。
        //通过exists判断。
        sop("dir:"+f.isDirectory());
        sop("file:"+f.isFile());
 
        sop(f.isAbsolute());
    }
 
 
    public static void method_2()
    {
        File f =new File("file.txt");
 
        //sop("exists:"+f.exists());
 
        //sop("execute:"+f.canExecute());
 
        //创建文件夹
        File dir =new File("abc\\kkk\\a\\a\\dd\\ee\\qq\\aaa");
 
        sop("mkdir:"+dir.mkdirs());
    }
     
 
    public static void method_1()throws IOException
    {
        File f =new File("file.txt");
        sop("create:"+f.createNewFile());
        //sop("delete:"+f.delete());
         
    }
 
 
    //创建File对象
    public static void consMethod()
    {
        //将a.txt封装成file对象。可以将已有的和为出现的文件或者文件夹封装成对象。
        File f1 =new File("a.txt");
 
        File f2 =new File("c:\\abc","b.txt");
 
        File d =new File("c:\\abc");
        File f3 =new File(d,"c.txt");
 
        sop("f1:"+f1);
        sop("f2:"+f2);
        sop("f3:"+f3);
 
        File f4 =new File("c:"+File.separator+"abc"+File.separator+"zzz"+File.separator+"a.txt");
 
 
    }
 
    public static void sop(Object obj)
    {
        System.out.println(obj);
    }
}
```



例2：递归列出指定目录下的文件或者文件夹

```
/*
列出指定目录下文件或者文件夹，包含子目录中的内容。
也就是列出指定目录下所有内容。
 
递归要注意：
1，限定条件。
2，要注意递归的次数。尽量避免内存溢出。
*/
 
import java.io.*;
 
class FileDemo3
{
    public static void main(String[] args)
    {
        File dir =new File("E:\\Book");
        showDir(dir,0);
         
    }
    public static String getLevel(int level)
    {
        StringBuilder sb =new StringBuilder();
        sb.append("|--");
        for(int x=0; x<level; x++)
        {
            //sb.append("|--");
            sb.insert(0,"|  ");
 
        }
        return sb.toString();
    }
    public static void showDir(File dir,int level)
    {
         
        System.out.println(getLevel(level)+dir.getName());
 
        level++;
        File[] files = dir.listFiles();
        for(int x=0; x<files.length; x++)
        {
            if(files[x].isDirectory())
                showDir(files[x],level);
            else
                System.out.println(getLevel(level)+files[x]);
        }
    }
 
 
 
     
}
```





## 4、缓冲流

缓冲流,也叫高效流，是对4个`FileXxx` 流的“增强流”。

**缓冲流的基本原理**：

```
1、使用了底层流对象从具体设备上获取数据，并将数据存储到缓冲区的数组内。
2、通过缓冲区的read()方法从缓冲区获取具体的字符数据，这样就提高了效率。
3、如果用read方法读取字符数据，并存储到另一个容器中，直到读取到了换行符时，将另一个容器临时存储的数据转成字符串返回，就形成了readLine()功能。
```

也就是说在创建流对象时，会创建一个内置的默认大小的缓冲区数组，通过缓冲区读写，减少系统IO次数，从而提高读写的效率。

缓冲书写格式为`BufferedXxx`，按照数据类型分类：

- **字节缓冲流**：`BufferedInputStream`，`BufferedOutputStream`
- **字符缓冲流**：`BufferedReader`，`BufferedWriter`



### 4.1、 字节缓冲流

构造方法

```
public BufferedInputStream(InputStream in) ：创建一个新的缓冲输入流，注意参数类型为InputStream。
public BufferedOutputStream(OutputStream out)： 创建一个新的缓冲输出流，注意参数类型为OutputStream。
```



构造举例代码如下：

```java
//构造方式一： 创建字节缓冲输入流【但是开发中一般常用下面的格式申明】
FileInputStream fps = new FileInputStream(b.txt);
BufferedInputStream bis = new BufferedInputStream(fps)

//构造方式一： 创建字节缓冲输入流
BufferedInputStream bis = new BufferedInputStream(new FileInputStream("b.txt"));

///构造方式二： 创建字节缓冲输出流
BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("b.txt"));
```



### 缓冲流和基础流速度对比

1. 基本流，代码如下：

```java
public class BufferedDemo {
    public static void main(String[] args) throws FileNotFoundException {
        // 记录开始时间
      	long start = System.currentTimeMillis();
		// 创建流对象
        try (
        	FileInputStream fis = new FileInputStream("py.exe");//exe文件够大
        	FileOutputStream fos = new FileOutputStream("copyPy.exe")
        ){
        	// 读写数据
            int b;
            while ((b = fis.read()) != -1) {
                fos.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		// 记录结束时间
        long end = System.currentTimeMillis();
        System.out.println("普通流复制时间:"+(end - start)+" 毫秒");
    }
}
不好意思十分钟过去了还在玩命复制中...
```

1. 缓冲流，代码如下：

```java
public class BufferedDemo {
    public static void main(String[] args) throws FileNotFoundException {
        // 记录开始时间
      	long start = System.currentTimeMillis();
		// 创建流对象
        try (
         BufferedInputStream bis = new BufferedInputStream(new FileInputStream("py.exe"));
	     BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("copyPy.exe"));
        ){
        // 读写数据
            int b;
            while ((b = bis.read()) != -1) {
                bos.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		// 记录结束时间
        long end = System.currentTimeMillis();
        System.out.println("缓冲流复制时间:"+(end - start)+" 毫秒");
    }
}

缓冲流复制时间:8016 毫秒
```

有的童鞋就要说了，我要更快的速度！最近看速度与激情7有点上头，能不能再快些？答案是当然可以

想要更快可以使用数组的方式，代码如下：

```java
public class BufferedDemo {
    public static void main(String[] args) throws FileNotFoundException {
      	// 记录开始时间
        long start = System.currentTimeMillis();
		// 创建流对象
        try (
		 BufferedInputStream bis = new BufferedInputStream(new FileInputStream("py.exe"));
		 BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("copyPy.exe"));
        ){
          	// 读写数据
            int len;
            byte[] bytes = new byte[8*1024];
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0 , len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		// 记录结束时间
        long end = System.currentTimeMillis();
        System.out.println("缓冲流使用数组复制时间:"+(end - start)+" 毫秒");
    }
}
缓冲流使用数组复制时间:521 毫秒  
```





### 4.2、字符缓冲流

#### 4.2.1、构造方法

相同的来看看其构造，其格式以及原理和字节缓冲流是一样一样的！

构造方法：

```
public BufferedReader(Reader in) ：创建一个新的缓冲输入流，注意参数类型为Reader。
public BufferedWriter(Writer out)： 创建一个新的缓冲输出流，注意参数类型为Writer。
```



构造方法举例：

```
// 创建字符缓冲输入流
BufferedReader br = new BufferedReader(new FileReader("b.txt"));
// 创建字符缓冲输出流
BufferedWriter bw = new BufferedWriter(new FileWriter("b.txt"));
```



### 字符缓冲流特有方法

字符缓冲流的基本方法与普通字符流调用方式一致，这里不再阐述，我们来看字符缓冲流具备的**特有**方法。

- BufferedReader：`public String readLine()`: **读一行数据**。 读取到最后返回null
- BufferedWriter：`public void newLine()`: **换行**,由系统属性定义符号。



`readLine`方法演示代码如下：

```java
public class BufferedReaderDemo {
    public static void main(String[] args) throws IOException {
      	 // 创建流对象
        BufferedReader br = new BufferedReader(new FileReader("a.txt"));
		// 定义字符串,保存读取的一行文字
        String line  = null;
      	// 循环读取,读取到最后返回null
        while ((line = br.readLine())!=null) {
            System.out.print(line);
            System.out.println("------");
        }
		// 释放资源
        br.close();
    }
}
```

`newLine`方法演示代码如下：

```java
public class BufferedWriterDemo throws IOException {
  public static void main(String[] args) throws IOException  {
    	// 创建流对象
  	BufferedWriter bw = new BufferedWriter(new FileWriter("b.txt"));
    	// 写出数据
      bw.write("哥");
    	// 写出换行
      bw.newLine();
      bw.write("敢");
      bw.newLine();
      bw.write("摸屎");
      bw.newLine();
      bw.write("你敢吗？");
      bw.newLine();
  	// 释放资源
      bw.close();
  }
}
输出效果:
哥
敢
摸屎
你敢吗？
```



### 4.3、 字符缓冲流练习

字符缓冲流练习啥捏？先放松一下吧各位，先欣赏欣赏我写的下面的诗篇

> 6.你说你的程序叫简单，我说我的代码叫诗篇
> 1.一想到你我就哦豁豁豁豁豁豁豁豁豁豁....哦nima个头啊，完全不理人家受得了受不了
> 8.Just 简单你和我 ，Just 简单程序员
> 3.约了地点却忘了见面 ，懂得寂寞才明白浩瀚
> 5.沉默是最大的发言权
> 2.总是喜欢坐在电脑前， 总是喜欢工作到很晚
> 7.向左走 又向右走，我们转了好多的弯
> 4.你从来就不问我，你还是不是那个程序员

欣赏完了咩？没错咋们就练习如何使用缓冲流的技术把上面的诗篇归顺序，都编过号了就是前面的1到8的编号

分析：首先用字符输入缓冲流创建个源，里面放没有排过序的文字，之后用字符输出缓冲流创建个目标接收，排序的过程就要自己写方法了哦，可以从每条诗词的共同点“.”符号下手！

#### 代码实现

```java
public class BufferedTest {
    public static void main(String[] args) throws IOException {
        // 创建map集合,保存文本数据,键为序号,值为文字
        HashMap<String, String> lineMap = new HashMap<>();

        // 创建流对象  源
        BufferedReader br = new BufferedReader(new FileReader("a.txt"));
        //目标
        BufferedWriter bw = new BufferedWriter(new FileWriter("b.txt"));

        // 读取数据
        String line  = null;
        while ((line = br.readLine())!=null) {
            // 解析文本
            String[] split = line.split("\\.");
            // 保存到集合
            lineMap.put(split[0],split[1]);
        }
        // 释放资源
        br.close();

        // 遍历map集合
        for (int i = 1; i <= lineMap.size(); i++) {
            String key = String.valueOf(i);
            // 获取map中文本
            String value = lineMap.get(key);
          	// 写出拼接文本
            bw.write(key+"."+value);
          	// 写出换行
            bw.newLine();
        }
		// 释放资源
        bw.close();
    }
}
```

运行效果

```javascript
1.一想到你我就哦豁豁豁豁豁豁豁豁豁豁…哦nima个头啊，完全不理人家受得了受不了
2.总是喜欢坐在电脑前， 总是喜欢工作到很晚
3.约了地点却忘了见面 ，懂得寂寞才明白浩瀚
4.你从来就不问我，你还是不是那个程序员
5.沉默是最大的发言权
6.你说你的程序叫简单，我说我的代码叫诗篇
7.向左走 又向右走，我们转了好多的弯
8.Just 简单你和我 ，Just 简单程序员
```





## 5、转换流

何谓转换流？为何由来？暂时带着问题让我们先来了解了解字符编码和字符集！

![](D:\java oracle\学习资料\各种学习整理文档（重要）\java 基础\IO和NIO\photos\何谓转换流.png)



### 5.1 字符编码与解码

众所周知，计算机中储存的信息都是用二进制数表示的，而我们在屏幕上看到的数字、英文、标点符号、汉字等字符是二进制数转换之后的结果。按照某种规则，将字符存储到计算机中，称为**编码** 。反之，将存储在计算机中的二进制数按照某种规则解析显示出来，称为**解码** 。比如说，按照`A`规则存储，同样按照`A`规则解析，那么就能显示正确的文本符号。反之，按照`A`规则存储，再按照`B`规则解析，就会导致乱码现象。

简单一点的说就是：

> 编码:字符(能看懂的)--字节(看不懂的)
>
> 解码:字节(看不懂的)-->字符(能看懂的)

代码解释则是

```java
String(byte[] bytes, String charsetName):通过指定的字符集解码字节数组
byte[] getBytes(String charsetName):使用指定的字符集合把字符串编码为字节数组

编码:把看得懂的变成看不懂的
String -- byte[]

解码:把看不懂的变成看得懂的
byte[] -- String
```

- **字符编码** `Character Encoding`: 就是一套自然语言的字符与二进制数之间的对应规则。

  而**编码表**则是生活中文字和计算机中二进制的对应规则

字符集

- **字符集** `Charset`：也叫**编码表**。是一个系统支持的所有字符的集合，包括各国家文字、标点符号、图形符号、数字等。

计算机要准确的存储和识别各种字符集符号，需要进行字符编码，一套字符集必然至少有一套字符编码。常见字符集有`ASCII`字符集、`GBK`字符集、`Unicode`字符集等。

![](D:\java oracle\学习资料\各种学习整理文档（重要）\java 基础\IO和NIO\photos\编码和字符集对应关系.png)





### 5.2 编码问题导致乱码

在java开发工具IDEA中，使用`FileReader` 读取项目中的文本文件。由于IDEA的设置，都是默认的`UTF-8`编码，所以没有任何问题。但是，当读取Windows系统中创建的文本文件时，由于Windows系统的默认是GBK编码，就会出现乱码。

```java
public class ReaderDemo {
    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader("C:\\a.txt");
        int read;
        while ((read = fileReader.read()) != -1) {
            System.out.print((char)read);
        }
        fileReader.close();
    }
}
输出结果：���
```

那么如何读取GBK编码的文件呢？ 这个时候就得讲讲转换流了！

从另一角度来讲：**字符流=字节流+编码表**



### 5.3 InputStreamReader-----(字节流到字符流的桥梁)

转换流`java.io.InputStreamReader`，是`Reader`的子类，从字面意思可以看出它是从字节流到字符流的桥梁。它读取字节，并使用指定的字符集将其解码为字符。它的字符集可以由名称指定，也可以接受平台的默认字符集。



### 构造方法

`InputStreamReader(InputStream in)`: 创建一个使用默认字符集的字符流。
`InputStreamReader(InputStream in, String charsetName)`: 创建一个指定字符集的字符流。



构造代码如下：

```
InputStreamReader isr = new InputStreamReader(new FileInputStream("in.txt"));
InputStreamReader isr2 = new InputStreamReader(new FileInputStream("in.txt") , "GBK");
```



#### 使用转换流解决编码问题

```
public class ReaderDemo2 {
    public static void main(String[] args) throws IOException {
      	// 定义文件路径,文件为gbk编码
        String FileName = "C:\\A.txt";
      	// 创建流对象,默认UTF8编码
        InputStreamReader isr = new InputStreamReader(new FileInputStream(FileName));
      	// 创建流对象,指定GBK编码
        InputStreamReader isr2 = new InputStreamReader(new FileInputStream(FileName) , "GBK");
		// 定义变量,保存字符
        int read;
      	// 使用默认编码字符流读取,乱码
        while ((read = isr.read()) != -1) {
            System.out.print((char)read); // �����ʺ      
        }
        isr.close();
      
      	// 使用指定编码字符流读取,正常解析
        while ((read = isr2.read()) != -1) {
            System.out.print((char)read);// 哥敢摸屎
        }
        isr2.close();
    }
}
```



### 5.4 OutputStreamWriter-----(字符流到字节流的桥梁)

转换流`java.io.OutputStreamWriter` ，是Writer的子类，字面看容易混淆会误以为是转为字符流，其实不然，OutputStreamWriter为从字符流到字节流的桥梁。使用指定的字符集将字符编码为字节。它的字符集可以由名称指定，也可以接受平台的默认字符集。



构造方法

```
OutputStreamWriter(OutputStream in): 创建一个使用默认字符集的字符流。
OutputStreamWriter(OutputStream in, String charsetName): 创建一个指定字符集的字符流。
```

构造举例，代码如下：

```java
OutputStreamWriter isr = new OutputStreamWriter(new FileOutputStream("a.txt"));
OutputStreamWriter isr2 = new OutputStreamWriter(new FileOutputStream("b.txt") , "GBK");
```



指定编码构造代码

```
public class OutputDemo {
    public static void main(String[] args) throws IOException {
      	// 定义文件路径
        String FileName = "C:\\s.txt";
      	// 创建流对象,默认UTF8编码
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(FileName));
        // 写出数据
      	osw.write("哥敢"); // 保存为6个字节
        osw.close();
      	
		// 定义文件路径
		String FileName2 = "D:\\A.txt";
     	// 创建流对象,指定GBK编码
        OutputStreamWriter osw2 = new OutputStreamWriter(new FileOutputStream(FileName2),"GBK");
        // 写出数据
      	osw2.write("摸屎");// 保存为4个字节
        osw2.close();
    }
}
```



![](D:\java oracle\学习资料\各种学习整理文档（重要）\java 基础\IO和NIO\photos\转换流.png)

为了达到**最高效率**，可以考虑在 `BufferedReader` 内包装 `InputStreamReader`

```javascript
BufferedReader in = new BufferedReader(new InputStreamReader(System.in))；
```



## 6、序列化流【理解】

(1)可以把对象写入文本文件或者在网络中传输
(2)如何实现序列化呢?
让被序列化的对象所属类实现序列化接口。
该接口是一个标记接口。没有功能需要实现。
(3)注意问题：
把数据写到文件后，在去修改类会产生一个问题。
如何解决该问题呢?
在类文件中，给出一个固定的序列化id值。
而且，这样也可以解决黄色警告线问题
(4)面试题：
什么时候序列化?
如何实现序列化?
什么是反序列化?



### 6.1 何谓序列化

Java 提供了一种对象**序列化**的机制。用一个字节序列可以表示一个对象，该字节序列包含该`对象的数据`、`对象的类型`和`对象中存储的属性`等信息。字节序列写出到文件之后，相当于文件中**持久保存**了一个对象的信息。

反之，该字节序列还可以从文件中读取回来，重构对象，对它进行**反序列化**。`对象的数据`、`对象的类型`和`对象中存储的数据`信息，都可以用来在内存中创建对象。看图理解序列化：

![](D:\java oracle\学习资料\各种学习整理文档（重要）\java 基础\IO和NIO\photos\对象序列化和反序列化图解.png)





### 6.2 ObjectOutputStream类

`java.io.ObjectOutputStream` 类，将Java对象的原始数据类型写出到文件,实现对象的持久存储。

#### 构造方法

`public ObjectOutputStream(OutputStream out)`： 创建一个指定OutputStream的ObjectOutputStream。

构造代码如下：

```java
FileOutputStream fileOut = new FileOutputStream("aa.txt");
ObjectOutputStream out = new ObjectOutputStream(fileOut);
```

#### 序列化操作

1. 一个对象要想序列化，必须满足两个条件:

该类必须实现`java.io.Serializable` 接口，`Serializable` 是一个标记接口，不实现此接口的类将不会使任何状态序列化或反序列化，会抛出`NotSerializableException` 。

该类的所有属性必须是可序列化的。如果有一个属性不需要可序列化的，则该属性必须注明是瞬态的，使用`transient` 关键字修饰。

```java
public class Employee implements java.io.Serializable {
    public String name;
    public String address;
    public transient int age; // transient瞬态修饰成员,不会被序列化
    public void addressCheck() {
      	System.out.println("Address  check : " + name + " -- " + address);
    }
}
```

2.写出对象方法

`public final void writeObject (Object obj)` : 将指定的对象写出。

```java
public class SerializeDemo{
   	public static void main(String [] args)   {
    	Employee e = new Employee();
    	e.name = "zhangsan";
    	e.address = "beiqinglu";
    	e.age = 20; 
    	try {
      		// 创建序列化流对象
          ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("employee.txt"));
        	// 写出对象
        	out.writeObject(e);
        	// 释放资源
        	out.close();
        	fileOut.close();
        	System.out.println("Serialized data is saved"); // 姓名，地址被序列化，年龄没有被序列化。
        } catch(IOException i)   {
            i.printStackTrace();
        }
   	}
}
输出结果：
Serialized data is saved
```



### 6.3 ObjectInputStream类

ObjectInputStream反序列化流，将之前使用ObjectOutputStream序列化的原始数据恢复为对象。

#### 构造方法

`public ObjectInputStream(InputStream in)`： 创建一个指定InputStream的ObjectInputStream。

#### 反序列化操作1

如果能找到一个对象的class文件，我们可以进行反序列化操作，调用`ObjectInputStream`读取对象的方法：

- `public final Object readObject ()` : 读取一个对象。

```java
public class DeserializeDemo {
   public static void main(String [] args)   {
        Employee e = null;
        try {		
             // 创建反序列化流
             FileInputStream fileIn = new FileInputStream("employee.txt");
             ObjectInputStream in = new ObjectInputStream(fileIn);
             // 读取一个对象
             e = (Employee) in.readObject();
             // 释放资源
             in.close();
             fileIn.close();
        }catch(IOException i) {
             // 捕获其他异常
             i.printStackTrace();
             return;
        }catch(ClassNotFoundException c)  {
        	// 捕获类找不到异常
             System.out.println("Employee class not found");
             c.printStackTrace();
             return;
        }
        // 无异常,直接打印输出
        System.out.println("Name: " + e.name);	// zhangsan
        System.out.println("Address: " + e.address); // beiqinglu
        System.out.println("age: " + e.age); // 0
    }
}
```

**对于JVM可以反序列化对象，它必须是能够找到class文件的类。如果找不到该类的class文件，则抛出一个 `ClassNotFoundException` 异常。**



#### 反序列化操作2

另外，当JVM反序列化对象时，能找到class文件，但是class文件在序列化对象之后发生了修改，那么反序列化操作也会失败，抛出一个`InvalidClassException`异常。发生这个异常的原因如下：

> 1、该类的序列版本号与从流中读取的类描述符的版本号不匹配
> 2、该类包含未知数据类型
> 2、该类没有可访问的无参数构造方法

`Serializable` 接口给需要序列化的类，提供了一个序列版本号。`serialVersionUID` 该版本号的目的在于验证序列化的对象和对应类是否版本匹配。

```java
public class Employee implements java.io.Serializable {
     // 加入序列版本号
     private static final long serialVersionUID = 1L;
     public String name;
     public String address;
     // 添加新的属性 ,重新编译, 可以反序列化,该属性赋为默认值.
     public int eid; 

     public void addressCheck() {
         System.out.println("Address  check : " + name + " -- " + address);
     }
}
```



### 6.4 序列化集合练习

1. 将存有多个自定义对象的集合序列化操作，保存到`list.txt`文件中。
2. 反序列化`list.txt` ，并遍历集合，打印对象信息。

#### 案例分析

1. 把若干学生对象 ，保存到集合中。
2. 把集合序列化。
3. 反序列化读取时，只需要读取一次，转换为集合类型。
4. 遍历集合，可以打印所有的学生信息

#### 案例代码实现

```java
public class SerTest {
	public static void main(String[] args) throws Exception {
		// 创建 学生对象
		Student student = new Student("老王", "laow");
		Student student2 = new Student("老张", "laoz");
		Student student3 = new Student("老李", "laol");

		ArrayList<Student> arrayList = new ArrayList<>();
		arrayList.add(student);
		arrayList.add(student2);
		arrayList.add(student3);
		// 序列化操作
		// serializ(arrayList);
		
		// 反序列化  
		ObjectInputStream ois  = new ObjectInputStream(new FileInputStream("list.txt"));
		// 读取对象,强转为ArrayList类型
		ArrayList<Student> list  = (ArrayList<Student>)ois.readObject();
		
      	for (int i = 0; i < list.size(); i++ ){
          	Student s = list.get(i);
        	System.out.println(s.getName()+"--"+ s.getPwd());
      	}
	}

	private static void serializ(ArrayList<Student> arrayList) throws Exception {
		// 创建 序列化流 
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("list.txt"));
		// 写出对象
		oos.writeObject(arrayList);
		// 释放资源
		oos.close();
	}
}
```



## 7、打印流

### 7.1 何谓打印流

平时我们在控制台打印输出，是调用`print`方法和`println`方法完成的，各位用了这么久的输出语句肯定没想过这两个方法都来自于`java.io.PrintStream`

**打印流分类**：

```
字节打印流PrintStream，字符打印流PrintWriter
```

**打印流特点**：

> A:只操作目的地,不操作数据源
>
> B:可以操作任意类型的数据
>
> C:如果启用了自动刷新，在调用println()方法的时候，能够换行并刷新
>
> D:可以直接操作文件



### 7.2 字节输出打印流PrintStream复制文本文件

```javascript
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class PrintStreamDemo {
    public static void main(String[] args) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader("copy.txt"));
        PrintStream ps=new PrintStream("printcopy.txt");
        String line;
        while((line=br.readLine())!=null) {
            ps.println(line);
        }
        br.close();
        ps.close();
    }
}
```

### 7.3 字符输出打印流PrintWriter复制文本文件

```javascript
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * 使用打印流复制文本文件
 */
public class PrintWriterDemo {
    public static void main(String[] args) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader("aa.txt"));
        PrintWriter pw=new PrintWriter("printcopyaa.txt");
        String line;
        while((line=br.readLine())!=null) {
            pw.println(line);
        }
        br.close();
        pw.close();
    }
}
```





## 8、Properties属性类

我想各位对这个Properties类多多少少也接触过了，首先Properties类并不在IO包下，那为啥要和IO流一起讲呢？原因很简单因为properties类经常和io流的联合一起使用。

> ```
> (1)是一个集合类，Hashtable的子类
> (2)特有功能
> A:public Object setProperty(String key,String value)
> B:public String getProperty(String key)
> C:public Set stringPropertyNames()
> (3)和IO流结合的方法
> 把键值对形式的文本文件内容加载到集合中
> public void load(Reader reader)
> public void load(InputStream inStream)
> 把集合中的数据存储到文本文件中
> public void store(Writer writer,String comments)
> public void store(OutputStream out,String comments)
> ```

### 8.1 Properties概述

`java.util.Properties` 继承于`Hashtable` ，来表示一个持久的属性集。它使用键值结构存储数据，每个键及其对应值都是一个字符串。该类也被许多Java类使用，比如获取系统属性时，`System.getProperties` 方法就是返回一个`Properties`对象。

### 8.2 Properties类

#### 8.2.1、构造方法

`public Properties()` :创建一个空的属性列表。

#### 8.2.2、基本的存储方法

- `public Object setProperty(String key, String value)` ： 保存一对属性。
- `public String getProperty(String key)` ：使用此属性列表中指定的键搜索属性值。
- `public Set stringPropertyNames()` ：所有键的名称的集合。

```java
public class ProDemo {
    public static void main(String[] args) throws FileNotFoundException {
        // 创建属性集对象
        Properties properties = new Properties();
        // 添加键值对元素
        properties.setProperty("filename", "a.txt");
        properties.setProperty("length", "209385038");
        properties.setProperty("location", "D:\\a.txt");
        // 打印属性集对象
        System.out.println(properties);
        // 通过键,获取属性值
        System.out.println(properties.getProperty("filename"));
        System.out.println(properties.getProperty("length"));
        System.out.println(properties.getProperty("location"));

        // 遍历属性集,获取所有键的集合
        Set<String> strings = properties.stringPropertyNames();
        // 打印键值对
        for (String key : strings ) {
          	System.out.println(key+" -- "+properties.getProperty(key));
        }
    }
}
输出结果：
{filename=a.txt, length=209385038, location=D:\a.txt}
a.txt
209385038
D:\a.txt
filename -- a.txt
length -- 209385038
location -- D:\a.txt
```



#### 8.2.3、properties类加载文件

与流相关的方法

`public void load(InputStream inStream)`： 从字节输入流中读取键值对。

参数中使用了字节输入流，通过流对象，可以关联到某文件上，这样就能够加载文本中的数据了。现在文本数据格式如下:

```java
filename=Properties.txt
length=123
location=C:\Properties.txt
```

使用properties类加载文件：

```java
public class ProDemo {
    public static void main(String[] args) throws FileNotFoundException {
        // 创建属性集对象
        Properties pro = new Properties();
        // 加载文本中信息到属性集
        pro.load(new FileInputStream("Properties.txt"));
        // 遍历集合并打印
        Set<String> strings = pro.stringPropertyNames();
        for (String key : strings ) {
          	System.out.println(key+" -- "+pro.getProperty(key));
        }
     }
}
输出结果：
filename -- Properties.txt
length -- 123
location -- C:\Properties.txt
```

文本中的数据，必须是键值对形式，可以使用空格、等号、冒号等符号分隔。



