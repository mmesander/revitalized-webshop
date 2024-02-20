package nu.revitalized.revitalizedwebshop.helpers;

//

import static nu.revitalized.revitalizedwebshop.helpers.CalculateAverageRating.calculateAverageRating;

import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableProduct;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.models.Supplement;

import java.util.Set;

public class UpdateRating {
//    public static IdentifiableProduct updateRating(Review review, IdentifiableProduct product, boolean deleteRating) {
//        Set<Review> reviews = product.getReviews();
//
//        if (deleteRating) {
//            reviews.remove(review);
//            product.setReviews(reviews);
//            product.setAverageRating(calculateAverageRating(product));
//        } else {
//            if (product instanceof Supplement) {
//                review.setSupplement(product);
//            } else if (product instanceof Garment) {
//                review.setGarment(product);
//
//            }
//            product.getReviews().add(review);
//            product.setAverageRating(calculateAverageRating(product));
//        }
//
//        return product;
//    }

    public static Supplement updateSupplementRating(Review review, Supplement supplement, boolean deleteRating) {
        Set<Review> reviews = supplement.getReviews();

        if (deleteRating) {
            reviews.remove(review);
            supplement.setReviews(reviews);
            supplement.setAverageRating(calculateAverageRating(supplement));
        } else {
            review.setSupplement(supplement);
            supplement.getReviews().add(review);
            supplement.setAverageRating(calculateAverageRating(supplement));
        }

        return supplement;
    }

    public static Garment updateGarmentRating(Review review, Garment garment, boolean deleteRating) {
        Set<Review> reviews = garment.getReviews();

        if (deleteRating) {
            reviews.remove(review);
            garment.setReviews(reviews);
            garment.setAverageRating(calculateAverageRating(garment));
        } else {
            review.setGarment(garment);
            garment.getReviews().add(review);
            garment.setAverageRating(calculateAverageRating(garment));
        }

        return garment;
    }
}