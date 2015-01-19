package com.test.thread;

/**
 * 自管理的线程写法，可以不让线程实现Thread类
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
		for (int i=0; i<5;i++) {
			new SelfManaged();
		}
	}
	
	

}
