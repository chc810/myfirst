package com.test.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.io.example.IOUtil;

public class NioClient {
	
	private InetSocketAddress inetSocketAddress;
	
	private static Logger logger = null; 
	
	private SocketChannel socketChannel = null;
	
	private Selector selector = null;
	
	static {
		PropertyConfigurator.configure("D:\\log4j.properties");
		logger = LoggerFactory.getLogger(NioServer.class);
	}
	
	public NioClient(String hostName, int port) throws IOException {
		inetSocketAddress = new InetSocketAddress(hostName, port);
		socketChannel = SocketChannel.open(inetSocketAddress);
		socketChannel.configureBlocking(false);  
		this.selector = Selector.open();  
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
		socketChannel.register(this.selector, SelectionKey.OP_READ);  
	}
	
	public void listen() throws IOException {
		// 轮询访问selector  
        while (true) {  
            selector.select();  
            // 获得selector中选中的项的迭代器  
            Iterator ite = this.selector.selectedKeys().iterator();  
            while (ite.hasNext()) {  
                SelectionKey key = (SelectionKey) ite.next();  
                // 删除已选的key,以防重复处理  
                ite.remove();  
                // 连接事件发生  
                if (key.isConnectable()) {  
                	logger.info("isConnectable。。。");
                    SocketChannel channel = (SocketChannel) key  
                            .channel();  
                    // 如果正在连接，则完成连接  
                    if(channel.isConnectionPending()){  
                        channel.finishConnect();  
                          
                    }  
                    // 设置成非阻塞  
                    channel.configureBlocking(false);  
                    channel.register(this.selector, SelectionKey.OP_READ);  
                      
                    // 获得了可读的事件  
                } else if (key.isReadable()) {  
                		logger.info("isReadable.......");
                        read(key);  
                }  
            }
        }
	}
            
    public void read(SelectionKey key) throws IOException{  
        //和服务端的read方法一样  
    	ByteBuffer byteBuffer = ByteBuffer.allocate(5);
		SocketChannel socketChannel = (SocketChannel)key.channel();
		int readBytes = socketChannel.read(byteBuffer);
		String ret = "";
		while(readBytes != -1) {
			 byteBuffer.flip();
			 ret += new String(byteBuffer.array(), 0, readBytes,"utf-8");
			 byteBuffer.clear();
			 readBytes = socketChannel.read(byteBuffer);
		}
//		logger.info("client: receiver bytes numbers:  " + readBytes);
//		IOUtil.printHex(byteBuffer.array());
        logger.info("Server: data = " + ret);  
//		while (readBytes != -1) {
//			
//            byteBuffer.clear();
//            readBytes = socketChannel.read(byteBuffer);
//                byteBuffer.flip();  
//                socketChannel.write(byteBuffer);  
//		}
		//注意需要关闭
//		socketChannel.close();
//		key.cancel();
    } 
	
	public void send(String requestData) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);  
        socketChannel.write(ByteBuffer.wrap(requestData.getBytes("utf-8")));
        byteBuffer.clear();
        //注意要这么写
//        socketChannel.socket().shutdownOutput();
       /* while (true) {
        	byteBuffer.clear();  
            int readBytes = socketChannel.read(byteBuffer);  
            if (readBytes > 0) {  
                byteBuffer.flip();  
                logger.info("Client: readBytes = " + readBytes);  
                logger.info("Client: data = " + new String(byteBuffer.array(), 0, readBytes));  
//                socketChannel.close();  
                break;  
            }  
        }*/
	}
	
	
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		String hostname = "localhost";  
        String requestData = "Actions speak louder than words!";  
        int port = 8080;  
        NioClient client = new NioClient(hostname, port);
        client.listen();
//        client.send(requestData);  
//        client.send("hello world");
        //如果用一个channel发送的话，前两条信息组合成一个包过去 ，最后需要关闭
//        client.getSocketChannel().socket().shutdownOutput();
//        new NioClient(hostname, port).send("hello world");
	}

}
