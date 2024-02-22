package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    // User
}
