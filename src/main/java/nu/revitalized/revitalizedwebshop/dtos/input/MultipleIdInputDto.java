package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MultipleIdInputDto {
    @NotNull(message = "Id is required")
    private List<Long> productIds;
}
