package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableProduct;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "supplements")
public class Supplement extends Product implements IdentifiableProduct {
    // Variables
    private String contains;

    // Relations
    @ManyToMany
    @JoinTable(
            name = "supplement_allergens",
            joinColumns = @JoinColumn(name = "supplement_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
    private Set<Allergen> allergens;

    @OneToMany(
            mappedBy = "supplement",
            fetch = FetchType.EAGER
    )
    private Set<Review> reviews;

    @ManyToMany(mappedBy = "supplements")
    List<Order> orders;

    // Relations
    @OneToOne(mappedBy = "supplement")
    private File file;
}