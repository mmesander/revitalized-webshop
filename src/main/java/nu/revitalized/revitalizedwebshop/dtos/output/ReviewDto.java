package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableId;
import java.util.Date;

@Getter
@Setter
public class ReviewDto implements IdentifiableId {
    // Variables
    private Long id;
    private String review;
    private Integer rating;
    private Date date;

    // Relations
    private Long productId;
    private String username;
}