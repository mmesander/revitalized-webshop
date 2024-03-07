package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableId;
import java.util.Set;

@Getter
@Setter
public class DiscountDto implements IdentifiableId {
    // Variables
    private Long id;
    private String name;
    private Double value;

    // Relations
    private Set<String> users;
}