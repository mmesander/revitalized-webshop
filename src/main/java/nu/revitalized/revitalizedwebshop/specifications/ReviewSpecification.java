package nu.revitalized.revitalizedwebshop.specifications;

import nu.revitalized.revitalizedwebshop.models.Review;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecification {
    private ReviewSpecification() {}

    // Request Filter: ratingLike
    public static Specification<Review> getReviewRatingLikeFilter(Integer ratingLike) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("rating"), ratingLike));
    }

    // Request Filter: minRating
    public static Specification<Review> getReviewMoreThanFilter(Integer minRatingLike) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("rating"), minRatingLike));
    }

    // Request Filter: maxRating
    public static Specification<Review> getReviewLessThanFilter(Integer maxRatingLike) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("rating"), maxRatingLike));
    }
}
