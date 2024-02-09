package nu.revitalized.revitalizedwebshop.interfaces;

// Imports
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Digits;
import java.lang.annotation.*;

@Digits(integer = 10, fraction = 2, message = "Price should contain only digits and two decimals")
@Constraint(validatedBy = {})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidPrice {
    String message() default "Price should contain only digits and two decimals";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName() default "Field";
}