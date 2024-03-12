package nu.revitalized.revitalizedwebshop.specifications;

// Imports
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class SupplementSpecification implements Specification<Supplement> {
    private String name;
    private String brand;
    private final Double minPrice;
    private final Double maxPrice;
    private final Integer minStock;
    private final Integer maxStock;
    private final Double minRating;
    private final Double maxRating;
    private String contains;

    public SupplementSpecification(
            String name,
            String brand,
            Double minPrice,
            Double maxPrice,
            Integer minStock,
            Integer maxStock,
            Double minRating,
            Double maxRating,
            String contains
    ) {
        this.name = name;
        this.brand = brand;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.minRating = minRating;
        this.maxRating = maxRating;
        this.contains = contains;
    }

    @Override
    public Predicate toPredicate(Root<Supplement> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            name = name.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name + "%"));
        }

        if (brand != null && !brand.isEmpty()) {
            brand = brand.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), "%" + brand + "%"));
        }

        if (minPrice != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        if (minStock != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("stock"), minStock));
        }

        if (maxStock != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("stock"), maxStock));
        }

        if (minRating != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("averageRating"), minRating));
        }

        if (maxRating != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("averageRating"), maxRating));
        }

        if (contains != null && !contains.isEmpty()) {
            contains = contains.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("contains")), "%" + contains + "%"));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}