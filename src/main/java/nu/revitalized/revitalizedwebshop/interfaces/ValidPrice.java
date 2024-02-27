package nu.revitalized.revitalizedwebshop.interfaces;

// Imports
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import java.lang.annotation.*;

@Positive(message = "Price must be higher than zero")
@Digits(integer = 10, fraction = 2, message = "Price should contain only digits followed by two decimals")
@Constraint(validatedBy = {})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidPrice {
    String message() default "Price should contain only digits followed by two decimals";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName() default "Field";
}