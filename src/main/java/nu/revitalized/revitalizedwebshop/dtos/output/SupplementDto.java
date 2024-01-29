package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.Identifiable;

import java.util.Set;

@Getter
@Setter
public class SupplementDto implements Identifiable {
    // Variables
    private Long id;
    private String name;
    private String brand;
    private String description;
    private Double price;
    private Double averageRating;
    private String contains;


    // Relations
    private Set<AllergenShortDto> allergens;
}
