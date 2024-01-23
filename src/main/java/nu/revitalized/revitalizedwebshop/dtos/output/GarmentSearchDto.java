package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GarmentSearchDto {
    // Variables
    private String name;
    private String brand;
    private Double price;
    private Double averageRating;
    private String size;
    private String color;
}
