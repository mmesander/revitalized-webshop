package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.NotNull;
import nu.revitalized.revitalizedwebshop.interfaces.ValidStatus;

public class OrderInputDto {
    @ValidStatus
    private String status;

    @NotNull(message = "Is payed is required: set true or false")
    private boolean isPayed;

    private String discountCode;
}
