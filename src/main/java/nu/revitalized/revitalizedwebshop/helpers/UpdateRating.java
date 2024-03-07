package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import java.util.Set;
import static nu.revitalized.revitalizedwebshop.helpers.CalculateAverageRating.calculateAverageRating;

public class UpdateRating {
    public static Supplement updateSupplementRating(Review review, Supplement supplement, boolean deleteRating, boolean update) {
        Set<Review> reviews = supplement.getReviews();

        if (deleteRating) {
            reviews.remove(review);
            supplement.setReviews(reviews);
            supplement.setAverageRating(calculateAverageRating(supplement));
        } else {
            if (update) {
                supplement.setAverageRating(calculateAverageRating(supplement));
            } else {
                review.setSupplement(supplement);
                supplement.getReviews().add(review);
                supplement.setAverageRating(calculateAverageRating(supplement));
            }
        }

        return supplement;
    }

    public static Garment updateGarmentRating(Review review, Garment garment, boolean deleteRating, boolean update) {
        Set<Review> reviews = garment.getReviews();

        if (deleteRating) {
            reviews.remove(review);
            garment.setReviews(reviews);
            garment.setAverageRating(calculateAverageRating(garment));
        } else {
            if (update) {
                garment.setAverageRating(calculateAverageRating(garment));
            } else {
                review.setGarment(garment);
                garment.getReviews().add(review);
                garment.setAverageRating(calculateAverageRating(garment));
            }
        }

        return garment;
    }
}