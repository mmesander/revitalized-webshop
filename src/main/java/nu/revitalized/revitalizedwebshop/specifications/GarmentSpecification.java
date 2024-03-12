package nu.revitalized.revitalizedwebshop.specifications;

// Imports
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import nu.revitalized.revitalizedwebshop.models.Garment;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class GarmentSpecification implements Specification<Garment> {
    private String name;
    private String brand;
    private final Double minPrice;
    private final Double maxPrice;
    private final Integer minStock;
    private final Integer maxStock;
    private final Double minRating;
    private final Double maxRating;
    private String size;
    private String color;

    public GarmentSpecification(
            String name,
            String brand,
            Double minPrice,
            Double maxPrice,
            Integer minStock,
            Integer maxStock,
            Double minRating,
            Double maxRating,
            String size,
            String color
    ) {
        this.name = name;
        this.brand = brand;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.minRating = minRating;
        this.maxRating = maxRating;
        this.size = size;
        this.color = color;
    }

    @Override
    public Predicate toPredicate(Root<Garment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
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

        if (size != null && !size.isEmpty()) {
            size = size.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("size")), "%" + size + "%"));
        }

        if (color != null && !color.isEmpty()) {
            color = color.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("color")), "%" + color + "%"));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}