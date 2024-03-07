package nu.revitalized.revitalizedwebshop.specifications;

// Imports
import nu.revitalized.revitalizedwebshop.models.Supplement;
import org.springframework.data.jpa.domain.Specification;

public class SupplementSpecification {
    private SupplementSpecification() {
    }

    // Request Filter: Supplement name
    public static Specification<Supplement> getSupplementNameLikeFilter(String nameLike) {
        String formattedNameLike = "%" + nameLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("name")), formattedNameLike));
    }

    // Request Filter: Supplement brand
    public static Specification<Supplement> getSupplementBrandLikeFilter(String brandLike) {
        String formattedBrandLike = "%" + brandLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("brand")), formattedBrandLike));
    }

    // Request Filter: Supplement price
    public static Specification<Supplement> getSupplementPriceLikeFilter(Double priceLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("price"), priceLike)));
    }

    // Request Filter: Supplement minPrice
    public static Specification<Supplement> getSupplementPriceMoreThanFilter(Double minPriceLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("price"), minPriceLike)));
    }

    // Request Filter: Supplement maxPrice
    public static Specification<Supplement> getSupplementPriceLessThanFilter(Double maxPriceLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("price"), maxPriceLike)));
    }

    // Request Filter: Supplement stock
    public static Specification<Supplement> getSupplementStockLikeFilter(Integer stockLike) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("stock"), stockLike));
    }

    // Request Filter: Supplement minStock
    public static Specification<Supplement> getSupplementStockMoreThanFilter(Integer minStockLike) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("stock"), minStockLike));
    }

    // Request Filter: Supplement maxStock
    public static Specification<Supplement> getSupplementStockLessThanFilter(Integer maxStockLike) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("stock"), maxStockLike));
    }

    // Request Filter: Supplement averageRating
    public static Specification<Supplement> getSupplementAverageRatingLikeFilter(Double averageRatingLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("averageRating"), averageRatingLike)));
    }

    // Request Filter: Supplement minRating
    public static Specification<Supplement> getSupplementAverageRatingMoreThanFilter(Double minRatingLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("averageRating"), minRatingLike)));
    }

    // Request Filter: Supplement maxRating
    public static Specification<Supplement> getSupplementAverageRatingLessThanFilter(Double maxRatingLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("averageRating"), maxRatingLike)));
    }

    // Request Filter: Supplement contains
    public static Specification<Supplement> getSupplementContainsLikeFilter(String containsLike) {
        String formattedContainsLike = "%" + containsLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("contains")), formattedContainsLike));
    }
}