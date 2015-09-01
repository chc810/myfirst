package com.test.reactor.springReactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.Environment;
import reactor.fn.Consumer;
import reactor.rx.broadcast.Broadcaster;

public class ReactorHelloworld {
	
	static Logger logger = LoggerFactory.getLogger(ReactorHelloworld.class);
	
	public static void main(String[] args) throws InterruptedException {
		Environment.initialize();
		
		Broadcaster<String> sink = Broadcaster.create(Environment.get());
		sink.dispatchOn(Environment.cachedDispatcher()).consume(new Consumer<String>() {

			@Override
			public void accept(String t) {
				logger.info(Thread.currentThread().getName());
				logger.info(t);
			}
		});
//			.map(String::toUpperCase)
//			.consume(s -> System.out.printf("s=%s%n", s)); 
		logger.debug(Thread.currentThread().getName());
		sink.onNext("Hello World!");
		Thread.sleep(500);
	}

}
