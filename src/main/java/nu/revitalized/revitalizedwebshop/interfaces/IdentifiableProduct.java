package nu.revitalized.revitalizedwebshop.interfaces;

// Imports
import nu.revitalized.revitalizedwebshop.models.Review;
import java.util.List;

public interface IdentifiableProduct {
    List<Review> getReviews();
}