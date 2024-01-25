package nu.revitalized.revitalizedwebshop.specifications;

import nu.revitalized.revitalizedwebshop.models.Garment;
import org.springframework.data.jpa.domain.Specification;

public class GarmentSpecification {
    private GarmentSpecification() {}

    // Request Filter: Garment name
    public static Specification<Garment> getGarmentNameLikeFilter(String nameLike) {}

    // Request Filter: Garment brand
    public static Specification<Garment> getGarmentBrandLikeFilter(String brandLike) {}

    // Request Filter: Garment price
    public static Specification<Garment> getGarmentPriceLikeFilter(Double priceLike) {}

    // Request Filter: Garment minPrice
    public static Specification<Garment> getGarmentPriceMoreThanFilter(Double minPriceLike) {}

    // Request Filter: Garment maxPrice
    public static Specification<Garment> getGarmentPriceLessThanFilter(Double maxPriceLike) {}

    // Request Filter: Garment averageRating
    public static Specification<Garment> getGarmentAverageRatingLikeFilter(Double averageRatingLike) {}

    // Request Filter: Garment minRating
    public static Specification<Garment> getGarmentAverageRatingMoreThanFilter(Double minRatingLike) {}

    // Request Filter: Garment maxRating
    public static Specification<Garment> getGarmentAverageRatingLessThanFilter(Double maxRatingLike) {}

    // Request Filter: Garment size
    public static Specification<Garment> getGarmentSizeLikeFilter(String sizeLike) {}

    // Request Filter: Garment color
    public static Specification<Garment> getGarmentColorLike(String colorLike) {}
}
