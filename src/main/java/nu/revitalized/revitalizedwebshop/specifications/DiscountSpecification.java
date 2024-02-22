package nu.revitalized.revitalizedwebshop.specifications;

// Imports
import nu.revitalized.revitalizedwebshop.models.Discount;
import org.springframework.data.jpa.domain.Specification;

public class DiscountSpecification {
    private DiscountSpecification() {
    }

    // Request Filter: Discount name
    public static Specification<Discount> getDiscountNameLikeFilter(String nameLike) {
        String formattedNameLike = "%" + nameLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("name")), formattedNameLike));
    }

    // Request Filter: Discount value
    public static Specification<Discount> getDiscountValueLikeFilter(Double valueLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("value"), valueLike)));
    }

    // Request Filter: Discount minValue
    public static Specification<Discount> getDiscountValueMoreThanFilter(Double minValueLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("price"), minValueLike)));
    }

    // Request Filter: Discount maxValue
    public static Specification<Discount> getDiscountValueLessThanFilter(Double maxValueLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("price"), maxValueLike)));
    }
}
