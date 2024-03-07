package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AuthUserOrderInputDto {
    @NotNull(message = "ProductIds is required")
    private List<Long> productIds;

    @NotNull(message = "Shipping Details id is required ")
    private Long shippingDetailsId;

    private String discountCode;
}
