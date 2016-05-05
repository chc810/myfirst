package com.test.jotm;

import org.springframework.jdbc.core.JdbcTemplate;

public class PersonImpl {
	
	private JdbcTemplate jdbcTemplateA;
	private JdbcTemplate jdbcTemplateB;
	public void setJdbcTemplateA(JdbcTemplate jdbcTemplateA) {
		this.jdbcTemplateA = jdbcTemplateA;
	}
	public void setJdbcTemplateB(JdbcTemplate jdbcTemplateB) {
		this.jdbcTemplateB = jdbcTemplateB;
	}
	
	public void savePerson(String id, String name, int age) {
		String sql = "insert into person values ('" + id + "','" + name + "'," + age + ")";
		int a = jdbcTemplateA.update(sql);
		System.out.println("a=" + a);
		int b = jdbcTemplateB.update(sql);
		System.out.println("b=" + b);
	}

}
