package nu.revitalized.revitalizedwebshop.interfaces;

// Imports
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.*;

@Pattern(regexp = "(?i)^(XS|S|M|L|XL|)$",
        message = "Invalid status, choose from: XS|S|M|L|XL|")
@Constraint(validatedBy = {})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidSize {
    String message() default "Invalid status, choose from: XS|S|M|L|XL|";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName() default "Field";
}
