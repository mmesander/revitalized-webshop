package nu.revitalized.revitalizedwebshop.interfaces;

// Imports
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Digits;
import java.lang.annotation.*;

@Digits(integer = 10, fraction = 0, message = "{fieldName} should contain only digits")
@Constraint(validatedBy = {})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidInteger {
    String message() default "{fieldName} should contain only digits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName() default "Field";
}