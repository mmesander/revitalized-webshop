package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class ReviewDto {
    // Variables
    private Long id;
    private String review;
    private Integer rating;
    private Date date;
}
