package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidText;

@Getter
@Setter
public class ReviewInputDto {
    @NotNull(message = "Review is required")
    @ValidText
    private String review;

    @NotNull(message = "rating is required")
    @Min(value = 1, message = "Rating must be 1 or higher")
    @Max(value = 10, message = "Rating must be 10 or lower")
    private Integer rating;
}
