package com.test;

import org.junit.Test;

public class OneTest {
	
	@Test
	public void test() {
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		System.out.println("系统类装载器:" + systemClassLoader);
		ClassLoader extClassLoader = systemClassLoader.getParent();
		System.out.println("系统类装载器的父类加载器——扩展类加载器:" + extClassLoader);
		ClassLoader bootClassLoader = extClassLoader.getParent();
		System.out.println("扩展类加载器的父类加载器——引导类加载器:" + bootClassLoader);
		
	}

}
