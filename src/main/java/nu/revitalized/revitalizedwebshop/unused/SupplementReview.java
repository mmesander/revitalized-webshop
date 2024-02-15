package nu.revitalized.revitalizedwebshop.unused;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.models.Supplement;

@Getter
@Setter
@Entity
@Table(name = "supplement_reviews")
public class SupplementReview extends Review {
    @ManyToOne
    @JoinColumn(name = "supplement_id")
    private Supplement supplement;
}
