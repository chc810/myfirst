package com.test.validate;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;

public class FirstTest {
	
	@Test
	public void first() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		PersonPo po =new PersonPo();
		Set<ConstraintViolation<PersonPo>> ret = validator.validate(po);
		for (ConstraintViolation<PersonPo> c : ret) {
			System.out.println(c.getMessage());
		}
	}

}
