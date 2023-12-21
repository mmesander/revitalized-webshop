package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplementInputDto {
    private String name;
    private String brand;
    private String description;
    private Double price;
    private Double averageRating;
    private String contains;
}
