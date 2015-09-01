package com.test.io;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class TestFirst {
	
	@Test
	public void fileDemo() {
		File file = new File("C:\\Users\\cuihc\\git\\myfirst\\src\\main\\java\\com\\test\\io\\file\\test1.txt");
		System.out.println("file exist " + file.exists());
		System.out.println(file.isDirectory());
		System.out.println(file.isFile());
		
	}
	
	@Test
	public void fileDemo1() throws IOException {
		File file = new File("C:\\Users\\cuihc\\git\\myfirst\\src\\main\\java\\com\\test\\io\\file\\test.txt");
		if (file.exists()) {
			file.delete();
		} else {
			file.createNewFile();
		}
	}
	
	@Test
	public void fileDemo2() throws IOException {
		File file = new File("C:\\Users\\cuihc\\git\\myfirst\\src\\main\\java\\com\\test\\io\\file\\", "test.txt");
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getCanonicalPath());
		System.out.println(file.getName());
		System.out.println(file.getPath());
		System.out.println(file.getParent());
		System.out.println(file.getParentFile().getAbsolutePath());
	}
	
	@Test
	public void stringEncode() throws UnsupportedEncodingException {
		String msg=new String("不改".getBytes("gbk"));
		String s_iso88591 = new String("中ni".getBytes("UTF-8"),"ISO8859-1");
		System.out.println(s_iso88591);
		String s_utf8 = new String(s_iso88591.getBytes("ISO8859-1"),"utf-8");
		System.out.println(s_utf8);
	}

}
