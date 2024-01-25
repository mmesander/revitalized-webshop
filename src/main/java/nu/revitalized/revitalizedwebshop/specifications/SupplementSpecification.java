package nu.revitalized.revitalizedwebshop.specifications;

import nu.revitalized.revitalizedwebshop.models.Supplement;
import org.springframework.data.jpa.domain.Specification;

public class SupplementSpecification {
    private SupplementSpecification() {}

    // Request filter: Supplement brand
    public static Specification<Supplement> getSupplementBrandLikeFilter(String brandLike) {
        String formattedBrandLike = "%" + brandLike.toLowerCase() + "%";
        return ((root, query, builder) -> builder.like(builder.lower(root.get("brand")), formattedBrandLike));
    }

    // Request filter: Supplement name
    public static Specification<Supplement> supplementNameLike(String nameLike) {
        String formattedNameLike = "%" + nameLike.toLowerCase() + "%";
        return ((root, query, builder) -> builder.like(builder.lower(root.get("name")), formattedNameLike));
    }

    // Request Filter: Supplement price

    // Request Filter: Supplement averageRating
    // Request Filter: Supplement contains


}
