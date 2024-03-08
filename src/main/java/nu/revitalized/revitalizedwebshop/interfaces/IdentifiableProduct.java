package nu.revitalized.revitalizedwebshop.interfaces;

// Imports
import nu.revitalized.revitalizedwebshop.models.Review;

import java.util.List;
import java.util.Set;

public interface IdentifiableProduct {
    List<Review> getReviews();
}