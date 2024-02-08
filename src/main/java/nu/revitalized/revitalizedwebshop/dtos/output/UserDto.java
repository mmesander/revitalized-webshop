package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableUsername;

@Getter
@Setter
public class UserDto implements IdentifiableUsername {
    // Variables
    private String username;
    private String password;
    private String email;

    // Relations
}
