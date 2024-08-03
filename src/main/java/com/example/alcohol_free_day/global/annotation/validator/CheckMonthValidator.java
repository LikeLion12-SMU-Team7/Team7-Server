package com.example.alcohol_free_day.global.annotation.validator;

import com.example.alcohol_free_day.global.annotation.CheckMonth;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckMonthValidator implements ConstraintValidator<CheckMonth, Integer> {

    @Override
    public void initialize(CheckMonth constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer page, ConstraintValidatorContext context) {
        if (page == null || page < 1 || page > 12) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("월은 1 이상 12 이하 이어야 합니다.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
