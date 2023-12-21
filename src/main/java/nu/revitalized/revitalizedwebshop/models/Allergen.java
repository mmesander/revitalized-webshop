package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "allergens")
public class Allergen {
    // Variables
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;

    // Relations
//    private Set<Supplement> supplements;
}
