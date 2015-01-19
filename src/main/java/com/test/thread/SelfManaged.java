package com.test.thread;

import java.util.concurrent.TimeUnit;

/**
 * 自管理的线程，不用继承的方式
 * @author cuihc
 *
 */
public class SelfManaged implements Runnable{
	
	private int countDown = 5;
	
	private Thread thread = new Thread(this);
	
	public SelfManaged() {
		this.thread.start();
	}
	
	public String toString() {
		return Thread.currentThread().getName() + "(" + countDown + ")";
	}

	public void run() {
		while(true) {
			System.out.println(this);
			if ( --countDown == 0) {
				return; 
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i=0; i<5;i++) {
			new SelfManaged();
			
		}
	}
	
	

}
