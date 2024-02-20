package nu.revitalized.revitalizedwebshop.exceptions;

public class InvalidInputException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InvalidInputException() {
        super();
    }

    public InvalidInputException(String message) {
        super(message);
    }
}
