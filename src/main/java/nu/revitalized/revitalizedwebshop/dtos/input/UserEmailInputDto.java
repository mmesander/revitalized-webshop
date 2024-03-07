package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEmailInputDto {
    @NotNull(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;
}