package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableId;

@Getter
@Setter
public class ShortShippingDetailsDto implements IdentifiableId {
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
}
