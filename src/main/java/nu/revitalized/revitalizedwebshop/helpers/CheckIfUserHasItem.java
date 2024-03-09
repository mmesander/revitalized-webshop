package nu.revitalized.revitalizedwebshop.helpers;

import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.models.User;

import java.util.List;

public class CheckIfUserHasItem {
    private void checkIfUserHasReview(User user, Review review) {
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
}
