package nu.revitalized.revitalizedwebshop.unused;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nu.revitalized.revitalizedwebshop.unused.ValidInteger;

public class ValidIntegerValidator implements ConstraintValidator<ValidInteger, Integer> {
    private String fieldName;

    @Override
    public void initialize(ValidInteger constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        for (char c: String.valueOf(value).toCharArray()) {
            if (!Character.isDigit(c) || c == '.' || c == ',') {
                context.buildConstraintViolationWithTemplate(fieldName + " should contain positive digits without decimals").addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
