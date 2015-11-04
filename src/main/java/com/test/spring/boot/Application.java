package com.test.spring.boot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {
	
	@RequestMapping("/")
	public Map<String, Object> home() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("helwo", "ddd");
		map.put("dd", 111);
		long i= 0;
		while(i<500000000) {
			i++;
		}
		System.out.println(Thread.currentThread().getName() + "," + Thread.currentThread().getId());
		return map;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
