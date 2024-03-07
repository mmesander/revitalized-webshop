package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "discounts")
public class Discount {
    // Variables
    @SequenceGenerator(name = "discounts_seq", allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discounts_seq")
    private Long id;
    private String name;
    private Double value;

    // Relations
    @ManyToMany
    @JoinTable(
            name = "discounts_users",
            joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "username")
    )
    private Set<User> users;
}