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
@Table(name = "garments")
public class Garment extends Product implements IdentifiableProduct {
    // Variables
    private String size;
    private String color;

    // Relations
    @OneToMany(
            mappedBy = "garment",
            fetch = FetchType.EAGER
    )
    private Set<Review> reviews;

    @ManyToMany(mappedBy = "garments")
    List<Order> orders;
}
