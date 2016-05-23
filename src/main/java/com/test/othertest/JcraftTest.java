package com.test.othertest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.test.io.example.IOUtil;

public class JcraftTest {
	
	@Test
	public void testReconnect() throws Exception {
		final JschClient client = new JschClient("root", "qnsoft", "10.130.29.182", 22);
		client.afterPropertiesSet();
		
		new Runnable(){

			@Override
			public void run() {
				
				BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
				String line = "";
				System.out.println("阻塞在。。。等待输入...");
					try {
						while((line = br.readLine()) != null) {
							try {
								System.out.println("字符串为：" + line);
									if ("a".equals(line)) {
										Vector<ChannelSftp.LsEntry> v = null;
										try {
//											v = client.getChannel().ls(".");
											client.getChannel().cd("/root/ee");
										} catch (SftpException e) {
											if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
												System.out.println("没有发现该文件夹");
											}
											System.out.println("ooooooooooooooooooooooooooooo");
											for (int i=0;i<10;i++) {
												if (reConnect(i)) {
													break;
												}
											}
										}
//										if (v != null) {
//											for(ChannelSftp.LsEntry oListItem : v){ 
//												if (!oListItem.getAttrs().isDir()) {
//													System.out.println(oListItem.getFilename());
//												}
//											}
//										}
										
								}
							} catch (Exception e) {
								System.out.println("aaaaaaaaaaaaaaaaaa.........");
								for (int i=0;i<10;i++) {
									if (reConnect(i)) {
										break;
									}
								}
								
							}
						}
					} catch (IOException e) {
						System.out.println("ddddddddddddddddddd.........");
						e.printStackTrace();
					}
				
				
			}
			
			public boolean reConnect(int i) {
					try {
						Thread.sleep(5000);
						System.out.println("进行第" + i + "次");
						client.afterPropertiesSet();
					} catch (Exception e) {
						return false;
					}
					return true;
			}
			
		}.run();
	}

}
