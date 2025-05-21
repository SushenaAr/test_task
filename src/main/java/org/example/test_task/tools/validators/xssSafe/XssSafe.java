package org.example.test_task.tools.validators.xssSafe;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = XssSafeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface XssSafe {
    String message() default "Потенциальная XSS атака обнаружена";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}