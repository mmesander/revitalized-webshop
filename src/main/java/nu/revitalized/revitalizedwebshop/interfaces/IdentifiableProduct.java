package nu.revitalized.revitalizedwebshop.interfaces;

// Imports
import nu.revitalized.revitalizedwebshop.models.Review;
import java.util.Set;

public interface IdentifiableProduct {
    Set<Review> getReviews();
}
