package com.test.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

import org.junit.Test;

public class First {
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new BaseVerticle());
	}
	
//	@Test
//	public void test1() {
//		Vertx vertx = Vertx.vertx();
//		vertx.deployVerticle(new BaseVerticle());
//	}

}

class BaseVerticle extends AbstractVerticle {
	 @Override
	    public void start() throws Exception {
	        System.out.println("BasicVerticle started");
	    }
	 
	 @Override
	    public void stop() throws Exception {
	        System.out.println("BasicVerticle stopped");
	    }
}
