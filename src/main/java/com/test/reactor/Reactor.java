package com.test.reactor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

public class Reactor implements Runnable {
	
	public final Selector selector;
	public final ServerSocketChannel serverSocketChannel; 
	
	public Reactor(int port) throws IOException {
		selector=Selector.open();  
        serverSocketChannel=ServerSocketChannel.open();  
        InetSocketAddress inetSocketAddress=new InetSocketAddress(InetAddress.getLocalHost(),port);  
        serverSocketChannel.socket().bind(inetSocketAddress);  
        serverSocketChannel.configureBlocking(false);  
          
        //向selector注册该channel    
        SelectionKey selectionKey=serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  
  
        //利用selectionKey的attache功能绑定Acceptor 如果有事情，触发Acceptor   
        selectionKey.attach(new Acceptor(this));  
        
	}

	@Override
	public void run() {
		ByteBuffer bf = ByteBuffer.allocate(512);
		
		while(true) {
			try {
				if (selector.select() > 0) {
					Set<SelectionKey> keys = selector.selectedKeys();
				} else {
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
//		serverSocketChannel.accept().read(bf);
//		bf.flip();
	}

}
