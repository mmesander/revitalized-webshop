package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidText;

@Getter
@Setter
public class DiscountInputDto {
    // Variables
    @NotNull(message = "Name is required")
    @ValidText(fieldName = "Name")
    private String name;

    @NotNull(message = "Value is required")
    @DecimalMin(value = "0.000", message = "Value must be greater than or equal to 0")
    @DecimalMax(value = "2.000", message = "Value must be less than or equal to 2")
    @Digits(integer = 1, fraction = 3, message = "Value must have at most 3 digits after the decimal point")
    private Double value;
}
