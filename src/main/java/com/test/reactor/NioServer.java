package com.test.reactor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.io.example.IOUtil;

public class NioServer {
	
	private static Logger logger = null; 
	
	static {
		PropertyConfigurator.configure("D:\\log4j.properties");
		logger = LoggerFactory.getLogger(NioServer.class);
	}
	
	private Selector selector;
	
	private Handler handler = new ServerHandler();
	SocketChannel outSocketChannel = null;
	
	public NioServer(int port) throws IOException {
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.socket().bind(new InetSocketAddress(port));
		this.selector = Selector.open();
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	public void listen() {
		logger.info("服务端启动成功");
		Thread t = new Thread(){
			@Override
			public void run() {
				while(true) {
					try {
						//阻塞，直到有信息过来
						logger.info("blocking here.....");
						selector.select();
						Set<SelectionKey> keys = selector.selectedKeys();

						Iterator<SelectionKey> iterator = keys.iterator();
						logger.info("SelectionKey size : " + keys.size());
						while(iterator.hasNext()) {
							SelectionKey key = iterator.next();
							logger.info("the SelectionKey : " + key);
							if (key.isAcceptable()) {
								logger.info("Server: SelectionKey is acceptable.");  
								ServerSocketChannel  serverSocketChannel = (ServerSocketChannel)key.channel();
								SocketChannel socketChannel = serverSocketChannel.accept();
								logger.info("Server: accept client socket " + socketChannel);  
								socketChannel.configureBlocking(false);  
								socketChannel.register(key.selector(), SelectionKey.OP_READ);
								outSocketChannel = socketChannel;
								handler.handleAccept(key);
							} else if (key.isReadable()) {
								logger.info("Server: SelectionKey is readable.");  
								handler.handleRead(key);
							} else if (key.isWritable()) {
								logger.info("Server: SelectionKey is writeable.");  
								handler.handleWrite(key);
							}
							//必须要删除，否则下次循环还在里面
							iterator.remove();  
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
		try {
			listenInput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	private void listenInput() throws IOException {
		BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
		String line = "";
		while((line = br.readLine()) != null) {
			System.out.println("字符串为：" + line);
			byte[] bs = line.getBytes("utf-8");
			System.out.println("内容为:" + new String(bs,"utf-8"));
			IOUtil.printHex(bs);
			outSocketChannel.write(ByteBuffer.wrap(bs));
		}
	}
	
	public static void main(String[] args) throws IOException {
		new NioServer(8080).listen();
	}

}

interface Handler {
	void handleAccept(SelectionKey key) throws IOException; 
	void handleRead(SelectionKey key) throws IOException;
	void handleWrite(SelectionKey key) throws IOException;
}

class ServerHandler implements Handler {
private static Logger logger = null; 
	
	static {
		PropertyConfigurator.configure("D:\\log4j.properties");
		logger = LoggerFactory.getLogger(NioServer.class);
	}

	@Override
	public void handleAccept(SelectionKey key) throws IOException {
		
	}

	@Override
	public void handleRead(SelectionKey key) throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocate(5);
		SocketChannel socketChannel = (SocketChannel)key.channel();
			int readBytes = socketChannel.read(byteBuffer);
			logger.info("Server: receiver bytes numbers:  " + readBytes);
			while (readBytes != -1) {
				logger.info("Server: readBytes = " + readBytes);  
                logger.info("Server: data = " + new String(byteBuffer.array(), 0, readBytes));  
                byteBuffer.clear();
                readBytes = socketChannel.read(byteBuffer);
//                byteBuffer.flip();  
//                socketChannel.write(byteBuffer);  
			}
			//注意需要关闭
			socketChannel.close();
			key.cancel();
	}

	@Override
	public void handleWrite(SelectionKey key) throws IOException {
//		SocketChannel socketChannel = (SocketChannel)key.channel();
		
	}
	
}
