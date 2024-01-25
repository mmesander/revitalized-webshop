package nu.revitalized.revitalizedwebshop.specifications;

import nu.revitalized.revitalizedwebshop.models.Supplement;
import org.springframework.data.jpa.domain.Specification;

public class SupplementSpecification {
    private SupplementSpecification() {}

    // Request filter: Supplement brand
    public static Specification<Supplement> getSupplementBrandLikeFilter(String brandLike) {
        String formattedBrandLike = "%" + brandLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("brand")), formattedBrandLike));
    }

    // Request filter: Supplement name
    public static Specification<Supplement> getSupplementNameLikeFilter(String nameLike) {
        String formattedNameLike = "%" + nameLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("name")), formattedNameLike));
    }

    // Request Filter: Supplement price
    public static Specification<Supplement> getSupplementPriceLikeFilter(Double priceLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("price"), priceLike)));
    }

    // Request Filter: Supplement maxPrice
    public static Specification<Supplement> getSupplementPriceLessThanFilter(Double priceLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("price"), priceLike)));
    }

    // Request Filter: Supplement minPrice
    public static Specification<Supplement> getSupplementPriceGreaterThanFilter(Double priceLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("price"), priceLike)));
    }

    // Request Filter: Supplement averageRating
    public static Specification<Supplement> getSupplementAverageRatingLikeFilter(Double averageRatingLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("averageRating"), averageRatingLike)));
    }

    // Request Filter: Supplement maxAverageRating
    public static Specification<Supplement> getSupplementAverageRatingLessThanFilter(Double averageRatingLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("averageRating"), averageRatingLike)));
    }

    // Request Filter: Supplement minAverageRating
    public static Specification<Supplement> getSupplementAverageRatingMoreThanFilter(Double averageRatingLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("averageRating"), averageRatingLike)));
    }

    // Request Filter: Supplement contains
    public static Specification<Supplement> getSupplementContainsLikeFilter(String containsLike) {
        String formattedContainsLike = "%" + containsLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("contains")), formattedContainsLike));
    }
}
