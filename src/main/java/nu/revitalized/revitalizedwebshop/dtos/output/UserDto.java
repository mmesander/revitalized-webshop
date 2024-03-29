package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableUsername;
import nu.revitalized.revitalizedwebshop.models.Authority;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserDto implements IdentifiableUsername {
    // Variables
    private String username;
    private String password;
    private String email;

    // Relations
    @JsonSerialize
    private Set<Authority> authorities;
    private List<ShortShippingDetailsDto> shippingDetails;
    private List<ReviewDto> reviews;
    private Set<ShortDiscountDto> discounts;
    private List<ShortOrderDto> orders;
}