package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingDetailsInputDto {
    private String shippingDetailsName;
    @NotNull(message = "First name is required")
    private String firstName;
    private String middleName;
    @NotNull(message = "Last name is required")
    private String lastName;
    @NotNull(message = "Country is required")
    private String country;
    @NotNull(message = "City is required")
    private String city;
    @NotNull(message = "Zip code is required")
    @Pattern(regexp = "\\d{4}[a-zA-Z]{2}", message = "Zip code should have 4 digits followed by 2 letters")
    private String zipCode;
    @NotNull(message = "Street is required")
    private String street;
    @NotNull(message = "House number is required")
    private int houseNumber;
    private String houseNumberAddition;
    @NotNull(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;
}
