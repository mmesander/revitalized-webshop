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
    @DecimalMin(value = "0.01", message = "Value must be greater than 0")
    @DecimalMax(value = "100.00", message = "Value must be less than or equal to 100")
    @Digits(integer = 3, fraction = 2, message = "Value must have at most 2 digits after the decimal point")
    private Double value;
}
