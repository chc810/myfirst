package com.test.guava;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CacheTest {
	
	@Test
	public void first() throws ExecutionException {
		LoadingCache<String, Object> g = CacheBuilder.newBuilder()
				.expireAfterWrite(10, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Object>() {
		             public Object load(String key) throws Exception {
		            	return "1" + key;
		               }
		             });
		System.out.println("大小===" + g.size());
		g.put("1", "hellow");
		System.out.println("大小===" + g.size());
		System.out.println(g.get("2"));
		System.out.println("大小===" + g.size());
		System.out.println(g.get("2"));
		System.out.println("大小===" + g.size());
		
	}
	
	@Test
	public void second() {
		Cache<String, Object> g = CacheBuilder.newBuilder()
				.expireAfterWrite(10, TimeUnit.MINUTES)
				.build();
	}
	

}
