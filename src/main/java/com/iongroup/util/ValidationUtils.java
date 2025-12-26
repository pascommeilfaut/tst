package com.iongroup.util;

import jakarta.validation.*;
import lombok.NonNull;

import java.util.Set;

public class ValidationUtils {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    public static void validate(@NonNull Object o) {
        Set<ConstraintViolation<@NonNull Object>> violations = VALIDATOR.validate(o);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
