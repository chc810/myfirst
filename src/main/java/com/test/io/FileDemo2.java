package com.test.io;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.Test;

public class FileDemo2 {
	
	@Test
	public void list() {
		File file = new File("C:\\Users\\cuihc\\git\\myfirst\\src\\main\\java\\com\\test");
		System.out.println(file.hashCode());
		String[]  list = file.list();
		for (String s : list) {
			System.out.println(s);
		}
		System.out.println();
		String[] list1 = file.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				System.out.println(dir.hashCode());
				System.out.println("dir======" + dir.getAbsolutePath() + ",name=" + name);
				return false;
			}
		});
	}
	
	@Test
	public void list1() {
		File file = new File("C:\\Users\\cuihc\\git\\myfirst\\src\\main\\java\\com\\test");
		String[] list1 = file.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if ("spring".equals(name)) {
					return true;
				}
				return false;
			}
		});
		for (String s : list1) {
			System.out.println(s);
		}
	}

}
