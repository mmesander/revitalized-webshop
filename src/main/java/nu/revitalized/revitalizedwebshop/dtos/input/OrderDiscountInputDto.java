package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDiscountInputDto {
    @NotNull(message = "Discount code is required")
    private String discountCode;
}