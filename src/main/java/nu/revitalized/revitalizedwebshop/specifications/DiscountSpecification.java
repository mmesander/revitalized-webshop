package nu.revitalized.revitalizedwebshop.specifications;

import nu.revitalized.revitalizedwebshop.models.Discount;
import org.springframework.data.jpa.domain.Specification;

public class DiscountSpecification {
    private DiscountSpecification() {}

    // Request Filter: Discount name
    public static Specification<Discount> getDiscountNameLikeFilter(String nameLike) {}

    // Request Filter: Discount value
    public static Specification<Discount> getDiscountValueLikeFilter(Double valueLike) {}

    // Request Filter: Discount minValue
    public static Specification<Discount> getDiscountNameLikeFilter(String nameLike) {}

    // Request Filter: Discount maxValue
    public static Specification<Discount> getDiscountNameLikeFilter(String nameLike) {}
}
