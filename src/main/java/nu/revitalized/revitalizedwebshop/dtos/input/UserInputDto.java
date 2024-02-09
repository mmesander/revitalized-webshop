package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidPassword;
import nu.revitalized.revitalizedwebshop.interfaces.ValidText;
import nu.revitalized.revitalizedwebshop.security.Authority;
import java.util.Set;

@Getter
@Setter
public class UserInputDto {
    @NotNull(message = "Username is required")
    @ValidText(fieldName = "Username")
    private String username;

    @ValidPassword
    private String password;

    @NotNull(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;

    @JsonSerialize
    private Set<Authority> authorities;
}
