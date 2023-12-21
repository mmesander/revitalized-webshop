package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplementDto {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private Double price;
    private Double averageRating;
    private String contains;
}
