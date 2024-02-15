package nu.revitalized.revitalizedwebshop.unused;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.models.Review;

@Getter
@Setter
@Entity
@Table(name = "garment_reviews")
public class GarmentReview extends Review {
    @ManyToOne
    @JoinColumn(name = "garment_id")
    private Garment garment;
}
