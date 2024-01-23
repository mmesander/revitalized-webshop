package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import nu.revitalized.revitalizedwebshop.models.Allergen;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class SearchDto {
    // General Variables
    private String name;
    private String brand;
    private Double price;
    private Double averageRating;


    // Supplements Variables
    private String contains;

    // Supplements Relations
    private Set<Allergen> allergens;


    // Garments Variables
    private String size;
    private String color;
}
