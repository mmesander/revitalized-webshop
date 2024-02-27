package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidText;

@Getter
@Setter
public class ReviewPatchInputDto {
    @ValidText
    private String review;

    @Min(value = 1, message = "Rating must be 1 or higher")
    @Max(value = 10, message = "Rating must be 10 or lower")
    private Integer rating;
}
