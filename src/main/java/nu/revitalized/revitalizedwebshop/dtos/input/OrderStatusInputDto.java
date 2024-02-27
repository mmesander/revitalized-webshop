package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidStatus;

@Getter
@Setter
public class OrderStatusInputDto {
    @ValidStatus
    private String status;
}
