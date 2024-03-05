package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameInputDto {
    @NotNull(message = "Username is required")
    @Pattern(regexp = "[a-zA-Z]+", message = "Username should contain only letters")
    private String username;
}
