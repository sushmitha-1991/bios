package com.dsp.bios.dto.newbillentry;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConditionalFieldsValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidConditionalFields {
    String message() default "Conditional field validation error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}