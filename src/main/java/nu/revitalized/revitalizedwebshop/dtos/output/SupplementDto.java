package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class SupplementDto {
    // Variables
    private Long id;
    private String name;
    private String brand;
    private String description;
    private Double price;
    private Double averageRating;
    private String contains;


    // Relations
    private Set<AllergenDto> allergens;
}
