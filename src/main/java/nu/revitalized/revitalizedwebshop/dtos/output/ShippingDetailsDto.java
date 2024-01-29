package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingDetailsDto {
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
