package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidText;

@Getter
@Setter
public class UserInputDto {
    @NotNull(message = "Username is required")
    @ValidText(fieldName = "Username")
    private String username;
    @NotNull
    @Size(min = 8, max = 20, message = "Password length must be between 8 and 20 characters")
    @Pattern.List({
            @Pattern(regexp = ".*\\d.*", message = "Password must contain a digit"),
            @Pattern(regexp = ".*[a-z].*", message = "Password must contain a lowercase letter"),
            @Pattern(regexp = ".*[A-Z].*", message = "Password must contain an uppercase letter"),
            @Pattern(regexp = ".*\\W.*", message = "Password must contain a special character")
    })
    private String password;
    @NotNull(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;
}
