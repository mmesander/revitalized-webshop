package nu.revitalized.revitalizedwebshop.specifications;

// Imports
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import nu.revitalized.revitalizedwebshop.models.Order;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification implements Specification<Order> {
    private final Double minTotalAmount;
    private final Double maxTotalAmount;

    public OrderSpecification(Double minTotalAmount, Double maxTotalAmount) {
        this.minTotalAmount = minTotalAmount;
        this.maxTotalAmount = maxTotalAmount;

    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (minTotalAmount != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"), minTotalAmount));
        }

        if (maxTotalAmount != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalAmount"), maxTotalAmount));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}