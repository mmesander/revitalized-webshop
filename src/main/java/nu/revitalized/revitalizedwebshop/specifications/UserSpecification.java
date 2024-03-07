package nu.revitalized.revitalizedwebshop.specifications;

// Imports
import nu.revitalized.revitalizedwebshop.models.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    private UserSpecification() {
    }

    // Request Filter: User username
    public static Specification<User> getUserUsernameLikeFilter(String usernameLike) {
        String formattedUsernameLike = "%" + usernameLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("username")), formattedUsernameLike));
    }

    // Request Filter: User email
    public static Specification<User> getUserEmailLike(String emailLike) {
        String formattedEmailLike = "%" + emailLike.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("email")), formattedEmailLike));
    }
}