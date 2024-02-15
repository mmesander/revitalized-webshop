package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableProduct;
import nu.revitalized.revitalizedwebshop.models.Review;
import java.text.DecimalFormat;


public class CalculateAverageRating {

    public static Double calculateAverageRating(IdentifiableProduct product) {
        if (product.getReviews() == null) {
            return null;
        } else {
            Integer length = product.getReviews().size();
            Integer totalSum = 0;

            for (Review review : product.getReviews()) {
                totalSum = review.getRating() + totalSum;
            }

            Double averageRating = (double) (totalSum / length);

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            averageRating = Double.parseDouble(decimalFormat.format(averageRating));

            return averageRating;
        }
    }
}
