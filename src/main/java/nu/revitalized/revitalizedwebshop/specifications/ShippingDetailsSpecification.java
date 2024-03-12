package nu.revitalized.revitalizedwebshop.specifications;

// Imports
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import nu.revitalized.revitalizedwebshop.models.ShippingDetails;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class ShippingDetailsSpecification implements Specification<ShippingDetails> {
    private String detailsName;
    private String name;
    private String country;
    private String city;
    private String zipCode;
    private String street;
    private String houseNumber;
    private String email;

    public ShippingDetailsSpecification(
            String detailsName,
            String name,
            String country,
            String city,
            String zipCode,
            String street,
            String houseNumber,
            String email
    ) {
        this.detailsName = detailsName;
        this.name = name;
        this.country = country;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.email = email;
    }

    @Override
    public Predicate toPredicate(Root<ShippingDetails> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (detailsName != null && !detailsName.isEmpty()) {
            detailsName = detailsName.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("detailsName")), "%" + detailsName + "%"));
        }

        if (name != null && !name.isEmpty()) {
            name = name.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name + "%"));
        }

        if (country != null && !country.isEmpty()) {
            country = country.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("country")), "%" + country + "%"));
        }

        if (city != null && !city.isEmpty()) {
            city = city.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), "%" + city + "%"));
        }

        if (zipCode != null && !zipCode.isEmpty()) {
            zipCode = zipCode.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("zipCode")), "%" + zipCode + "%"));
        }

        if (street != null && !street.isEmpty()) {
            street = street.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("street")), "%" + street + "%"));
        }

        if (houseNumber != null && !houseNumber.isEmpty()) {
            houseNumber = houseNumber.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("houseNumber")), "%" + houseNumber + "%"));
        }

        if (email != null && !email.isEmpty()) {
            email = email.toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email + "%"));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}