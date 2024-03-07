package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AuthUserOrderInputDto {
    private List<Long> productIds;
    private Long shippingDetailsId;
    private String discountCode;
}
