package com.test.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.test.io.example.IOUtil;

public class FileOutputStreamDemo {
	
	@Test
	public void first() throws IOException{
//		String file = "C:\\Users\\cuihc\\git\\myfirst\\src\\main\\java\\com\\test\\io\\file\\test.dat";
//		FileOutputStream fos = new FileOutputStream(file);
//		fos.write('A');
//		fos.write('B');
//		int i = 100;
//		fos.write(i >>> 24);
//		fos.write(i >>> 16);
//		fos.write(i>>> 8);
//		fos.write(i);
//		byte[] bs = "汉".getBytes("unicode");
//		fos.write(bs);
//		fos.close();
		String file = "D:\\1.txt";
		IOUtil.printHex(file);
		
	}
	
	@Test
	public void second() throws IOException {
		String file = "D:\\1.txt";
		FileInputStream fis = new FileInputStream(file);
		int i;
		List<Byte> l = new ArrayList<Byte>();
		while ((i =  fis.read()) != -1) {
			l.add((byte)i);
		}
		byte[] bs = new byte[l.size()];
		for (i=0;i<l.size();i++) {
			bs[i] = l.get(i);
		}
		System.out.println(new String(bs, "utf-8"));
		fis.close();
		IOUtil.printHex(file);
	}
	
	@Test
	public void thread() throws Exception {
		String str = "你好hello";  
		IOUtil.printHex(str.getBytes("utf-8"));
        int byte_len = str.getBytes("utf-8").length;  
        int len = str.length();  
        System.out.println("字节长度为：" + byte_len);  
		    System.out.println("字符长度为：" + len);  
		    System.out.println("系统默认编码方式：" + System.getProperty("file.encoding")); 
	}

}
