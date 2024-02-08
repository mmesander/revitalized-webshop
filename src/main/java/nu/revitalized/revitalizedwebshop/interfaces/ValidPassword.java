package nu.revitalized.revitalizedwebshop.interfaces;

// Imports
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.lang.annotation.*;

@NotNull
@Size(min = 8, max = 20, message = "Password length must be between 8 and 20 characters")
@Pattern.List({
        @Pattern(regexp = ".*\\d.*", message = "Password must contain a digit"),
        @Pattern(regexp = ".*[a-z].*", message = "Password must contain a lowercase letter"),
        @Pattern(regexp = ".*[A-Z].*", message = "Password must contain an uppercase letter"),
        @Pattern(regexp = ".*[@#$%^&+=].*", message = "Password must contain a special character")
})
@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ReportAsSingleViolation
public @interface ValidPassword {
    String message() default "Invalid password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
