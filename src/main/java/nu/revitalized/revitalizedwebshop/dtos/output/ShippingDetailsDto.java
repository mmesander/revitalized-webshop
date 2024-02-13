package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableId;
import nu.revitalized.revitalizedwebshop.models.User;

@Getter
@Setter
public class ShippingDetailsDto implements IdentifiableId {
    // Variables
    private Long id;
    private String detailsName;
    private String name;
    private String country;
    private String city;
    private String zipCode;
    private String street;
    private String houseNumber;
    private String email;


    // Relations
    private User user;
}
