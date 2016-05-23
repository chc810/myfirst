package com.test.jotm;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JtomTest {
	
	@Test
	public void test() {
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring-jotm.xml");
		PersonImpl pi = (PersonImpl)app.getBean("personService");
		pi.savePerson("2","lisi",20);
	}
	
	@Test
	public void testnoJtom() {
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring-nojotm.xml");
		PersonImpl pi = (PersonImpl)app.getBean("personService");
		pi.savePerson("2","lisi",20);
	}

}
