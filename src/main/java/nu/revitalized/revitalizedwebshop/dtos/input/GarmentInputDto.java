package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GarmentInputDto {
    // Variables
    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Brand is required")
    private String brand;
    @NotNull
    @Size(min = 10, max = 200, message = "Description must be between 2 and 200 characters")
    private String description;
    @NotNull
    @Positive(message = "Price must be higher than zero")
    private Double price;
    @NotNull(message = "Size is required")
    private String size;
    @NotNull(message = "Color is required")
    private String color;
}
