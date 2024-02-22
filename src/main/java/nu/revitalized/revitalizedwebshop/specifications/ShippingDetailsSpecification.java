package nu.revitalized.revitalizedwebshop.specifications;

// Imports
import nu.revitalized.revitalizedwebshop.models.ShippingDetails;
import org.springframework.data.jpa.domain.Specification;

public class ShippingDetailsSpecification {
    private ShippingDetailsSpecification() {
    }

    // Request Filter: Shipping Details detailsName
    public static Specification<ShippingDetails> getShippingDetailsDetailsNameLikeFilter(String detailsNameLike) {
        String formattedDetailsNameLike = "%" + detailsNameLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("detailsName")), formattedDetailsNameLike));
    }

    // Request Filter: Shipping Details name
    public static Specification<ShippingDetails> getShippingDetailsNameLikeFilter(String nameLike) {
        String formattedNameLike = "%" + nameLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("name")), formattedNameLike));
    }

    // Request Filter: Shipping Details country
    public static Specification<ShippingDetails> getShippingDetailsCountryLikeFilter(String countryLike) {
        String formattedCountryLike = "%" + countryLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("country")), formattedCountryLike));
    }

    // Request Filter: Shipping Details city
    public static Specification<ShippingDetails> getShippingDetailsCityLikeFilter(String cityLike) {
        String formattedCityLike = "%" + cityLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("city")), formattedCityLike));
    }

    // Request Filter: Shipping Details zipCode
    public static Specification<ShippingDetails> getShippingDetailsZipCodeLikeFilter(String zipCodeLike) {
        String formattedZipCodeLike = "%" + zipCodeLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("zipCode")), formattedZipCodeLike));
    }

    // Request Filter: Shipping Details street
    public static Specification<ShippingDetails> getShippingDetailsStreetLikeFilter(String streetLike) {
        String formattedStreetLike = "%" + streetLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("street")), formattedStreetLike));
    }

    // Request Filter: Shipping Details houseNumber
    public static Specification<ShippingDetails> getShippingDetailsHouseNumberLikeFilter(String houseNumberLike) {
        String formattedHouseNumberLike = "%" + houseNumberLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("houseNumber")), formattedHouseNumberLike));
    }

    // Request Filter: Shipping Details email
    public static Specification<ShippingDetails> getShippingDetailsEmailLikeFilter(String emailLike) {
        String formattedEmailLike = "%" + emailLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("email")), formattedEmailLike));
    }
}
