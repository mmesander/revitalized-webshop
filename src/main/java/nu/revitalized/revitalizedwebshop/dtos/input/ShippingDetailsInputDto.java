package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidInteger;
import nu.revitalized.revitalizedwebshop.interfaces.ValidText;

@Getter
@Setter
public class ShippingDetailsInputDto {
    @NotNull(message = "Address name is required")
    @ValidText(fieldName = "Address name")
    private String detailsName;

    @NotNull(message = "First name is required")
    @ValidText(fieldName = "First name")
    private String firstName;

    @ValidText(fieldName = "Middle name")
    private String middleName;

    @NotNull(message = "Last name is required")
    @ValidText(fieldName = "Last name")
    private String lastName;

    @NotNull(message = "Country is required")
    @ValidText(fieldName = "Country")
    private String country;

    @NotNull(message = "City is required")
    @ValidText(fieldName = "City")
    private String city;

    @NotNull(message = "Zip code is required")
    @Pattern(regexp = "\\d{4}[a-zA-Z]{2}", message = "Zip code should have 4 digits followed by 2 letters")
    private String zipCode;

    @NotNull(message = "Street is required")
    @ValidText(fieldName = "Street")
    private String street;

    @NotNull(message = "House number is required")
    @Positive(message = "House number can't be negative")
    @ValidInteger(fieldName = "House number")
    private Integer houseNumber;

    @ValidInteger(fieldName = "House number addition")
    private String houseNumberAddition;

    @NotNull(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;
}
