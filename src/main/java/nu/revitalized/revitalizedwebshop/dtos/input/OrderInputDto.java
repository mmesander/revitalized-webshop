package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidStatus;

@Getter
@Setter
public class OrderInputDto {
    @ValidStatus
    private String status;

    @NotNull(message = "Is paid is required: set true or false")
    private Boolean isPaid;
}
