package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidPrice;
import nu.revitalized.revitalizedwebshop.interfaces.ValidSize;
import nu.revitalized.revitalizedwebshop.interfaces.ValidStock;
import nu.revitalized.revitalizedwebshop.interfaces.ValidText;

@Getter
@Setter
public class GarmentInputDto {
    // Variables
    @NotNull(message = "Name is required")
    @ValidText(fieldName = "Name")
    private String name;

    @NotNull(message = "Brand is required")
    @ValidText(fieldName = "Brand")
    private String brand;

    @NotNull
    @Size(min = 10, max = 200, message = "Description must be between 2 and 200 characters")
    private String description;

    @NotNull(message = "Price is required")
    @ValidPrice
    private Double price;

    @NotNull(message = "Stock is required")
    @ValidStock
    private Integer stock;

    @NotNull(message = "Size is required")
    @ValidSize
    private String size;

    @NotNull(message = "Color is required")
    @ValidText(fieldName = "Color")
    private String color;
}
