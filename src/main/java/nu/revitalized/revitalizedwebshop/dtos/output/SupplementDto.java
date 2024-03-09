package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableId;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class SupplementDto implements IdentifiableId {
    // Variables
    private Long id;
    private String name;
    private String brand;
    private String description;
    private Double price;
    private Integer stock;
    private Double averageRating;
    private String contains;

    // Relations
    private Set<ShortAllergenDto> allergens;
    private List<ReviewDto> reviews;
}