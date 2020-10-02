/**
 * 
 */
package com.frost.documentservice.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jobin
 *
 */
@Slf4j
public class AgeValidator implements ConstraintValidator<Age, String> {

	protected long minAge;

	@Override
	public void initialize(Age age) {
		this.minAge = age.value();
	}

	@Override
	public boolean isValid(String bday, ConstraintValidatorContext validatorCtx) {

		LocalDate bdate;

		try {
			bdate = LocalDate.parse(bday);
		} catch (Exception e) {
			log.error("Failed to parse the dob : {}", e);
			return false;
		}

		LocalDate today = LocalDate.now();
		return ChronoUnit.YEARS.between(bdate, today) >= minAge;

	}
}