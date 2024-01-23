package nu.revitalized.revitalizedwebshop.dtos.output;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    // General
    private String name;
    private String brand;
    private Double price;
    private Double averageRating;


    // Supplements
    private String contains;


    // Garments
}
