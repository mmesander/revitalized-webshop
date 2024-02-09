package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidNumber;
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
    @Positive(message = "Price must be higher than zero")
    @ValidNumber(fieldName = "Price")
    private Double price;

    @NotNull(message = "Stock is required")
    @Positive(message = "Stock can't be negative")
    @ValidNumber(fieldName = "Stock")
    private Integer stock;

    @NotNull(message = "Size is required")
    private String size;

    @ValidText(fieldName = "Color")
    @NotNull(message = "Color is required")
    private String color;
}
