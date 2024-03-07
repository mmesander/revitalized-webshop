package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidPrice;
import nu.revitalized.revitalizedwebshop.interfaces.ValidSize;
import nu.revitalized.revitalizedwebshop.interfaces.ValidStock;
import nu.revitalized.revitalizedwebshop.interfaces.ValidText;

@Getter
@Setter
public class GarmentPatchInputDto {
    // Variables
    @ValidText(fieldName = "Name")
    private String name;

    @ValidText(fieldName = "Brand")
    private String brand;

    @Size(min = 10, max = 200, message = "Description must be between 2 and 200 characters")
    private String description;

    @ValidPrice
    private Double price;

    @ValidStock
    private Integer stock;

    @ValidSize
    private String size;

    @ValidText(fieldName = "Color")
    private String color;
}