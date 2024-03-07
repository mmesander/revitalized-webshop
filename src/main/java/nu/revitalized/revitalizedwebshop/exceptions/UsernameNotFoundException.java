package nu.revitalized.revitalizedwebshop.exceptions;

// Imports
import java.io.Serial;

public class UsernameNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UsernameNotFoundException() {
        super();
    }

    public UsernameNotFoundException(String username) {
        super("Cannot find user: " + username);
    }
}