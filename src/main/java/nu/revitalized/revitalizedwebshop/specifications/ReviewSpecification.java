package nu.revitalized.revitalizedwebshop.specifications;

// Imports
import nu.revitalized.revitalizedwebshop.models.Review;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecification {
    private ReviewSpecification() {
    }

    // Request Filter: Review rating
    public static Specification<Review> getReviewRatingLikeFilter(Integer ratingLike) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("rating"), ratingLike));
    }

    // Request Filter: Review minRating
    public static Specification<Review> getReviewRatingMoreThanFilter(Integer minRatingLike) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("rating"), minRatingLike));
    }

    // Request Filter: Review maxRating
    public static Specification<Review> getReviewRatingLessThanFilter(Integer maxRatingLike) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("rating"), maxRatingLike));
    }
}