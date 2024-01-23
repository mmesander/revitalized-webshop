package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;

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


    // Garments Variables
    private String size;
    private String color;
}
