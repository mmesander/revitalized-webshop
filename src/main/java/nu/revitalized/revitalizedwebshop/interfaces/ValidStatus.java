package nu.revitalized.revitalizedwebshop.interfaces;

// Imports
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.*;

@NotNull(message = "Status is required")
@Pattern(regexp = "(?i)^(in process|verifying|queued|packing|shipping|delivered|failed)$",
        message = "Invalid status, choose from: in process|verifying|queued|packing|shipping|delivered|failed")
@Constraint(validatedBy = {})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidStatus {
    String message() default "Invalid status, choose from: in process|verifying|queued|packing|shipping|delivered|failed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String fieldName() default "Field";
}