package nu.revitalized.revitalizedwebshop.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.Identifiable;

@Getter
@Setter
public class GarmentDto implements Identifiable {
    // Variables
    private Long id;
    private String name;
    private String brand;
    private String description;
    private Double price;
    private Double averageRating;
    private String size;
    private String color;
}
