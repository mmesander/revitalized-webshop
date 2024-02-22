package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableId;

@Getter
@Setter
public class DiscountDto implements IdentifiableId {
    private Long id;
    private String name;
    private Double value;
}
