java Socket通信



通信类：

```java
package com.ruimin.ifs.common.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

public class SocketUcifFactory {
    private Logger log = LoggerFactory.getLogger(getClass());
    private SocketUcifFactory(){}
    private static SocketUcifFactory instance = null;
    private ConcurrentHashMap<String, Socket> socketMap=new ConcurrentHashMap<>();


    public static SocketUcifFactory newInstance()
    {

        if (instance == null)
        {
            synchronized (SocketUcifFactory.class)
            {
                if (instance == null)
                {
                    instance = new SocketUcifFactory();
                }
            }
        }

        return instance;
    }

    public String sendMsg(String IP,Integer port,String reqData,String reqCharset){
        OutputStream out = null;
        InputStream in =null;
        StringBuffer sb = new StringBuffer();
        byte[] buffer = new byte[1024*8];
        int len;
        byte[] bytes=null;
        Socket socket=null;
        try
        {
            socket=new Socket();
            socket.setSoTimeout(60000);
            socket.setSendBufferSize(1024*8);
            socket.setReceiveBufferSize(1024*8);
            socket.setKeepAlive(true);
            socket.connect(new InetSocketAddress(IP,port),60000);
            out = socket.getOutputStream();
            out.write(reqData.getBytes("GBK"));
             out.flush();
            int length=reqData.length();
            log.info("数据发送成功，正在等待返回数据,连接状态:{}",socket.isConnected());
            in = socket.getInputStream();

            len=in.read(buffer);
            sb.append(new String(buffer, 0, buffer.length, "GBK"));

            return sb.toString();

        } catch (Exception e)
        {
            log.info("socket异常",e);
            return null;
        }
        finally
        {
            try
            {
                if (in!=null)
                {
                    in.close();
                }
                if (out!=null)
                {
                    out.close();
                }
                if(socket!=null&&socket.isConnected()&&!socket.isClosed())
                {
                    socket.close();
                }
            } catch (IOException e) {
                log.info("关闭资源异常",e);
            }
            finally
            {
                in=null;
                out=null;
                socket=null;
            }
        }
    }
}

```

调用类：

```
String rspXml = SocketUcifFactory.newInstance().sendMsg(socketIp, Integer.parseInt(port), reqXml, "GBK");
```

