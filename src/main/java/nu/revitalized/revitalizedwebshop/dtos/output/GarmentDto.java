package nu.revitalized.revitalizedwebshop.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableId;
import nu.revitalized.revitalizedwebshop.models.Review;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class GarmentDto implements IdentifiableId {
    // Variables
    private Long id;
    private String name;
    private String brand;
    private String description;
    private Double price;
    private Integer stock;
    private Double averageRating;
    private String size;
    private String color;


    // Relations
    private Set<ReviewDto> reviews;
}
