package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderIsPaidInputDto {
    @NotNull(message = "Is payed is required: set true or false")
    private Boolean isPaid;
}
