package com.test.io;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

public class FileInputStreamDemo {
	
	@Test
	public void fileInputStream1() throws IOException {
		String file = "C:\\Users\\cuihc\\git\\myfirst\\src\\main\\java\\com\\test\\io\\file\\test.txt";
		FileInputStream fis = new FileInputStream(file);
		int b;
		int i = 1;
		while((b = fis.read()) != -1) {
			if (b <= 0xf) {
				System.out.print("0");
			}
			System.out.print(Integer.toHexString(b) + "  ");
			if (i++ % 10 ==0) {
				System.out.println();
			}
		}
		System.out.println("=====================================");
		DataInputStream dis = new DataInputStream(fis);
		System.out.println(dis.readInt());
		fis.close();
	}
}
