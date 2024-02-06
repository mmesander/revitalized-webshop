package nu.revitalized.revitalizedwebshop.specifications;

// Imports
import nu.revitalized.revitalizedwebshop.models.Garment;
import org.springframework.data.jpa.domain.Specification;

public class GarmentSpecification {
    private GarmentSpecification() {}

    // Request Filter: Garment name
    public static Specification<Garment> getGarmentNameLikeFilter(String nameLike) {
        String formattedNameLike = "%" + nameLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("name")), formattedNameLike));
    }

    // Request Filter: Garment brand
    public static Specification<Garment> getGarmentBrandLikeFilter(String brandLike) {
        String formattedBrandLike = "%" + brandLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("brand")), formattedBrandLike));
    }

    // Request Filter: Garment price
    public static Specification<Garment> getGarmentPriceLikeFilter(Double priceLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("price"), priceLike)));
    }

    // Request Filter: Garment minPrice
    public static Specification<Garment> getGarmentPriceMoreThanFilter(Double minPriceLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("price"), minPriceLike)));
    }

    // Request Filter: Garment maxPrice
    public static Specification<Garment> getGarmentPriceLessThanFilter(Double maxPriceLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("price"), maxPriceLike)));
    }

    // Request Filter: Garment stock
    public static Specification<Garment> getGarmentStockLikeFilter(Integer stockLike) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("stock"), stockLike));
    }

    // Request Filter: Garment minStock
    public static Specification<Garment> getGarmentStockMoreThanFilter(Integer minStockLike) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("stock"), minStockLike));
    }

    // Request Filter: Garment maxStock
    public static Specification<Garment> getGarmentStockLessThanFilter(Integer maxStockLike) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("stock"), maxStockLike));
    }

    // Request Filter: Garment averageRating
    public static Specification<Garment> getGarmentAverageRatingLikeFilter(Double averageRatingLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("averageRating"), averageRatingLike)));
    }

    // Request Filter: Garment minRating
    public static Specification<Garment> getGarmentAverageRatingMoreThanFilter(Double minRatingLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("averageRating"), minRatingLike)));
    }

    // Request Filter: Garment maxRating
    public static Specification<Garment> getGarmentAverageRatingLessThanFilter(Double maxRatingLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("averageRating"), maxRatingLike)));
    }

    // Request Filter: Garment size
    public static Specification<Garment> getGarmentSizeLikeFilter(String sizeLike) {
        String formattedSizeLike = "%" + sizeLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("size")), formattedSizeLike));
    }

    // Request Filter: Garment color
    public static Specification<Garment> getGarmentColorLike(String colorLike) {
        String formattedColorLike = "%" + colorLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("color")), formattedColorLike));
    }
}
