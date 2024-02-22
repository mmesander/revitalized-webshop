package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscountDto {
    private Long id;
    private String name;
    private Double value;
}
