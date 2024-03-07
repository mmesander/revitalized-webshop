package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableProduct;
import nu.revitalized.revitalizedwebshop.models.Review;

public class CalculateAverageRating {
    public static Double calculateAverageRating(IdentifiableProduct product) {
        if (product.getReviews() == null) {
            return null;
        } else {
            Integer length = product.getReviews().size();
            Double totalSum = 0.0;

            for (Review review : product.getReviews()) {
                totalSum = review.getRating() + totalSum;
            }

            double averageRating = (totalSum / length);


            averageRating = Math.round(averageRating * 10.0) / 10.0;

            return averageRating;
        }
    }
}