package org.example.test_task.tools.validators.xssSafe;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.owasp.encoder.Encode;

public class XssSafeValidator implements ConstraintValidator<XssSafe, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        // Если после кодирования строка изменилась - значит были опасные символы
        return value.equals(Encode.forHtmlContent(value));
    }
}
