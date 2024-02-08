package nu.revitalized.revitalizedwebshop.exceptions;

public class UsernameNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UsernameNotFoundException() {
        super();
    }

    public UsernameNotFoundException(String username) {
        super("Cannot find user: " + username);
    }
}
