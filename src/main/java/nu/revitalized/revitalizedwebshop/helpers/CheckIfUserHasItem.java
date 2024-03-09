package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.models.ShippingDetails;
import nu.revitalized.revitalizedwebshop.models.User;
import java.util.List;

public class CheckIfUserHasItem {
    public static void checkIfUserHasReview(User user, Review review) {
        List<Review> reviews = user.getReviews();
        boolean hasReview = false;

        for (Review foundReview : reviews) {
            if (foundReview.equals(review)) {
                hasReview = true;
                break;
            }
        }

        if (!hasReview) {
            throw new BadRequestException("User: " + user.getUsername()
                    + " does not have review with id: " + review.getId());
        }
    }

    public static void checkIfUserHasShippingDetails(User user, ShippingDetails shippingDetails) {
        List<ShippingDetails> shippingDetailsList = user.getShippingDetails();
        boolean hasShippingDetails = false;

        for (ShippingDetails foundShippingDetails : shippingDetailsList) {
            if (foundShippingDetails.equals(shippingDetails)) {
                hasShippingDetails = true;
                break;
            }
        }

        if (!hasShippingDetails) {
            throw new BadRequestException("User: " + user.getUsername()
            + " does not have shipping details with id: " + shippingDetails.getId());
        }
    }
}
