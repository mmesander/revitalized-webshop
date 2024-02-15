package nu.revitalized.revitalizedwebshop.interfaces;

import nu.revitalized.revitalizedwebshop.models.Review;
import java.util.Set;

public interface IdentifiableProduct {
    Set<Review> getReviews();
}
