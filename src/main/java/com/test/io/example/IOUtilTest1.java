package com.test.io.example;

import java.io.IOException;

import org.junit.Test;

public class IOUtilTest1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			IOUtil.printHex("C:\\Users\\cuihc\\git\\myfirst\\src\\main\\java\\com\\test\\io\\example\\test.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void test() {
		try {
			
			IOUtil.printHex("abcd你好".getBytes("gbk"));
			System.out.println();
			IOUtil.printHex("abcd你好".getBytes("utf-8"));
			System.out.println();
			IOUtil.printHex("abcd你好".getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
