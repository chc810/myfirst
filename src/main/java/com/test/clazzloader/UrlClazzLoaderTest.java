package com.test.clazzloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;

public class UrlClazzLoaderTest {
	
	@Test
	public void test() throws MalformedURLException {
		String url = "http://";
		ClassLoader c1 = new URLClassLoader(new URL[]{new URL(url)}, null);
	}

}
