package nu.revitalized.revitalizedwebshop.dtos.output;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    private String name;
    private String brand;
    private Double price;
    private Double averageRating;
    private String contains;
}
