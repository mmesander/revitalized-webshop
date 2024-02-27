package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import nu.revitalized.revitalizedwebshop.interfaces.ValidStatus;

public class OrderInputDto {
    @ValidStatus
    private String status;

    private String discountCode;
}
