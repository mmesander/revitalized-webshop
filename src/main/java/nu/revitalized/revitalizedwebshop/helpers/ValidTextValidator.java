package nu.revitalized.revitalizedwebshop.helpers;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nu.revitalized.revitalizedwebshop.interfaces.ValidText;

public class ValidTextValidator implements ConstraintValidator<ValidText, String> {
    private String fieldName;

    @Override
    public void initialize(ValidText constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (value == null || value.trim().isEmpty()) {
            return true; // Let @NotNull handle null values
        }

        if (!value.matches("[a-zA-Z ]+")) {
            context.buildConstraintViolationWithTemplate(fieldName + " should contain only letters").addConstraintViolation();
            return false;
        } else {
            context.disableDefaultConstraintViolation();
        }


        return true;
    }
}