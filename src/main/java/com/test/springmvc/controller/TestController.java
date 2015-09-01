package com.test.springmvc.controller;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@RequestMapping ( "/showView" )  
	public ModelAndView showView() {
		 ModelAndView modelAndView = new ModelAndView();  
		 modelAndView.setViewName("index");
		 modelAndView.addObject("kk", "default");
		 return modelAndView;
	}
	
	@RequestMapping("/showView/{variable}")
	public ModelAndView showView(@PathVariable String variable, HttpServletResponse response) {
		 ModelAndView modelAndView = new ModelAndView();  
		 modelAndView.setViewName("index");
		 modelAndView.addObject("kk", variable);
		 Cookie cookie = new Cookie("hello","cookiehello");
		 cookie.setPath("/");
		 response.addCookie(cookie);
		 response.setHeader("Access-Control-Allow-Origin", "*");
		 return modelAndView;
	}

	@RequestMapping("/cookie")
	public ModelAndView testCookie(@CookieValue("JSESSIONID") String cookieValue, /*@CookieValue String hello,*/HttpServletRequest request) throws Exception{
			Locale locale = request.getLocale();
			System.out.println(locale.getCountry());
		 ModelAndView modelAndView = new ModelAndView();  
		 System.out.println("cookieValue=" + cookieValue);
		 modelAndView.setViewName("index");
//		 modelAndView.addObject("kk", hello);
		Cookie[] cs = request.getCookies();
		for (Cookie c : cs) {
			System.out.println("cookiename=" + c.getName() + ",cookievalue=" + c.getValue());
		}
		 return modelAndView;
	}
	
	@RequestMapping(value="/params", params={"param1=value1","param2", "!param3"})
	public ModelAndView testParams(String param2) {
		System.out.println("test testParams()...");
		 ModelAndView modelAndView = new ModelAndView();  
		 modelAndView.setViewName("index");
		 modelAndView.addObject("kk", param2);
		 return modelAndView;
	}
	
	@RequestMapping(value="/method", method={RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT})
	public ModelAndView testMethod() {
		System.out.println("test testMethod()...");
		 ModelAndView modelAndView = new ModelAndView();  
		 modelAndView.setViewName("index");
		 modelAndView.addObject("kk", "testMethod");
		 return modelAndView;
	}
	
	@RequestMapping(value="/headers", headers={"host=localhost:8080", "Accept", "referer"})
	public ModelAndView testHeaders(@RequestHeader("referer") String referer) {
		System.out.println("test testHeaders()..." + referer);
		 ModelAndView modelAndView = new ModelAndView();  
		 modelAndView.setViewName("index");
		 modelAndView.addObject("kk", referer);
		 return modelAndView;
	}
}
