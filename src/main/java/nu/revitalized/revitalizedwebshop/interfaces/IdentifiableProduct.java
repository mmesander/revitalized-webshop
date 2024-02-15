package nu.revitalized.revitalizedwebshop.interfaces;

import nu.revitalized.revitalizedwebshop.models.Review;

import java.util.List;

public interface IdentifiableProduct {
    List<Review> getReviews();
}
