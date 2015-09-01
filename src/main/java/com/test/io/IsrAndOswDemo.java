package com.test.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.test.io.example.IOUtil;

public class IsrAndOswDemo {
	
	@Test
	public void first() throws Exception {
		String file = "C:\\Users\\cuihc\\git\\myfirst\\src\\main\\java\\com\\test\\io\\file\\utf-8.txt";
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file),"utf-8");
		int c;
		int i = 0;
		while((c = isr.read()) != -1) {
			i++;
			System.out.print((char)c);
		}
		isr.close();
		IOUtil.printHex(file);
		
	}
	
	@Test
	public void second() throws Exception {
//		String file = "C:\\Users\\cuihc\\git\\myfirst\\src\\main\\java\\com\\test\\io\\file\\utf-8.txt";
//		String fileo = "C:\\Users\\cuihc\\git\\myfirst\\src\\main\\java\\com\\test\\io\\file\\gbkout.txt";
		String file = "com/test/io/file/utf-8.txt";
		String fileo = "com\\test\\io\\file\\gbkout.txt";
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file),"utf-8");
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fileo), "gbk");
		char[] buffer = new char[8*1024];
		int c;
		while ((c = isr.read(buffer, 0, buffer.length)) != -1) {
			String s = new String(buffer,0,c);
			System.out.println(s);
			osw.write(buffer, 0, c);
			osw.flush();
		}
		isr.close();
		osw.close();
		
	}
	
	@Test
	public void test() throws UnsupportedEncodingException {
		char c = '\u0067';      //unicode码，是以开头 后面是两个字节的16进制表述
		System.out.println(c);
		System.out.println((int)c);   //转换成int就是16进制对应的unicode码
		String s = "g";
		byte[] ss = s.getBytes("utf-8");
		for (byte s1 : ss) {
			System.out.println(s1);
		}
		
	}

}
