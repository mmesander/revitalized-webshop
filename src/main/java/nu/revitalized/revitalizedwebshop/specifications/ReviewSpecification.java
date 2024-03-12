package nu.revitalized.revitalizedwebshop.specifications;

// Imports
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import nu.revitalized.revitalizedwebshop.models.Review;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class ReviewSpecification implements Specification<Review> {
    private final Integer minRating;
    private final Integer maxRating;

    public ReviewSpecification(Integer minRating, Integer maxRating) {
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    @Override
    public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (minRating != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("averageRating"), minRating));
        }

        if (maxRating != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("averageRating"), maxRating));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}