package com.test.io.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

public class LoadResourceTest {

	@Test
	public void test() throws IOException {
		InputStream is = Student.class.getResourceAsStream("test.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String sr = "";
		while((sr = br.readLine()) != null) {
			System.out.println(sr);
		}
		is.close();
	}
	
	@Test
	public void test1() throws IOException {
		InputStream is = Student.class.getResourceAsStream("/test.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String sr = "";
		while((sr = br.readLine()) != null) {
			System.out.println(sr);
		}
		is.close();
	}
}
