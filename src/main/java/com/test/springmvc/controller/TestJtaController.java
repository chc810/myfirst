package com.test.springmvc.controller;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.jotm.PersonImpl;

@Controller
@RequestMapping("/jta")
public class TestJtaController {
	
	@Autowired
	private PersonImpl personService;
	
	@RequestMapping(value = "/save")
	@ResponseBody  
	public void showView() {
		personService.savePerson("2","lisi",20);
	}
	
	@RequestMapping(value = "/getJndi")
	@ResponseBody  
	public void showView1() {
		
		try {
			Context ctx = new InitialContext();
			ctx.lookup("java:comp/UserTransaction");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
