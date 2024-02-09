package nu.revitalized.revitalizedwebshop.interfaces;

// Imports
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import nu.revitalized.revitalizedwebshop.helpers.ValidTextValidator;
import java.lang.annotation.*;

@Constraint(validatedBy = {ValidTextValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidText {
    String message() default "This field should contain only letters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName() default "Field";
}
