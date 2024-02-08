package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidPassword;

@Getter
@Setter
public class UserInputDto {
    @NotNull(message = "Username is required")
    private String username;
    @ValidPassword
    private String password;
    @NotNull(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;
}
