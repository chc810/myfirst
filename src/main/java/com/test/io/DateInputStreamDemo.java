package com.test.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.test.io.example.IOUtil;

public class DateInputStreamDemo {

	@Test
	public void dos() throws IOException {
		String file = "C:\\Users\\cuihc\\git\\myfirst\\src\\main\\java\\com\\test\\io\\file\\test.dat";
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
		dos.writeInt(10);
		dos.writeInt(-10);
		dos.writeLong(10l);
		dos.writeDouble(10.5);
		dos.writeUTF("中国");
		dos.writeChars("中国");
		dos.close();
	}
	
	@Test
	public void dis() throws IOException {
		String file = "C:\\Users\\cuihc\\git\\myfirst\\src\\main\\java\\com\\test\\io\\file\\test.dat";
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
//		System.out.println(dis.readInt());
//		System.out.println(dis.readInt());
//		System.out.println(dis.readLong());
//		System.out.println(dis.readDouble());
//		System.out.println(dis.readUTF());
//		System.out.println(dis.readChar());
//		System.out.println(dis.readChar());
//		dis.close();
		IOUtil.printHex(file);
		System.out.println(IOUtil.class.getClassLoader());
		
	}
}
