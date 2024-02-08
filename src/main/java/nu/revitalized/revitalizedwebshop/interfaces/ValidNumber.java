package nu.revitalized.revitalizedwebshop.interfaces;

// Imports
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.*;

@Pattern(regexp = "\\d+", message = "{fieldName} should contain only digits")
@Constraint(validatedBy = {})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ReportAsSingleViolation
public @interface ValidNumber {
    String message() default "{fieldName} should contain only digits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName() default "Field";
}