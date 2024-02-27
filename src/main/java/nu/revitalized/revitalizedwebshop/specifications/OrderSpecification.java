package nu.revitalized.revitalizedwebshop.specifications;

// Imports
import nu.revitalized.revitalizedwebshop.models.Order;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;


public class OrderSpecification {
    private OrderSpecification() {}

    // Request Filter: Order date
    public static Specification<Order> getOrderDateLikeFilter(LocalDate dateLike) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("orderDate"), dateLike));
    }

    // Request Filter: Order afterDate
    public static Specification<Order> getOrderAfterDateFilter(LocalDate afterDate) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("orderDate"), afterDate));
    }

    // Request Filter: Order beforeDate
    public static Specification<Order> getOrderBeforeDateFilter(LocalDate beforeDate) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("orderDate"), beforeDate));
    }

    // Request Filter: Order totalPrice
    public static Specification<Order> getOrderPriceLikeFilter(Double totalPriceLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("totalPrice"), totalPriceLike)));
    }

    // Request Filter: Order minTotalPrice
    public static Specification<Order> getOrderPriceMoreThanFilter(Double minTotalPriceLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("totalPrice"), minTotalPriceLike)));
    }

    // Request Filter: Order maxTotalPrice
    public static Specification<Order> getOrderPriceLessThanFilter(Double maxTotalPriceLike) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(root.get("totalPrice"), maxTotalPriceLike)));
    }
}
