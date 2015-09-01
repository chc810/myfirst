package com.test.validate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PersonPo {
	
	@NotNull
	@Size(min=2,max=19)
	private String idCard;
	
	@NotNull
	@Min(value=10)
	private int age;
	@NotNull
	private String name;
	
	
	public PersonPo() {
		super();
	}
	public PersonPo(String idCard, int age, String name) {
		super();
		this.idCard = idCard;
		this.age = age;
		this.name = name;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
