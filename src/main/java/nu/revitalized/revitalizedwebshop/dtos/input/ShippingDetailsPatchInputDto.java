package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidText;

@Getter
@Setter
public class ShippingDetailsPatchInputDto {
    @ValidText(fieldName = "Address name")
    private String detailsName;

    @ValidText(fieldName = "First name")
    private String firstName;

    @ValidText(fieldName = "Middle name")
    private String middleName;

    @ValidText(fieldName = "Last name")
    private String lastName;

    @ValidText(fieldName = "Country")
    private String country;

    @ValidText(fieldName = "City")
    private String city;

    @Pattern(regexp = "\\d{4}[a-zA-Z]{2}", message = "Zip code should have 4 digits followed by 2 letters")
    private String zipCode;

    @ValidText(fieldName = "Street")
    private String street;

    @Positive(message = "House number can't be negative")
    private Integer houseNumber;

    @ValidText(fieldName = "House number addition")
    private String houseNumberAddition;

    @Email(message = "Enter a valid email")
    private String email;
}
