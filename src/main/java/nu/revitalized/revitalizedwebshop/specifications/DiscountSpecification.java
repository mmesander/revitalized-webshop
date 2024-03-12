package nu.revitalized.revitalizedwebshop.specifications;

// Imports
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import nu.revitalized.revitalizedwebshop.models.Discount;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class DiscountSpecification implements Specification<Discount> {
    private String name;
    private final Double value;
    private final Double minValue;
    private final Double maxValue;

    public DiscountSpecification(
            String name,
            Double value,
            Double minValue,
            Double maxValue
    ) {
        this.name = name;
        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public Predicate toPredicate(Root<Discount> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            name = name.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name + "%"));
        }

        if (value != null) {
            predicates.add(criteriaBuilder.equal(root.get("value"), value));
        }

        if (minValue != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("value"), minValue));
        }

        if (maxValue != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("value"), maxValue));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}