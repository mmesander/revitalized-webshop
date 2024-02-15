package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    // Variables
    private Long id;
    private String review;
    private Integer rating;
}
