package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

//TODO Repo bouwen, service layer bouwen en controllers bouwen

@Getter
@Setter
@Entity
@Table(name = "allergens")
public class Allergen {
    // Variables
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    // Relations
//    private Set<Supplement> supplements;
}
