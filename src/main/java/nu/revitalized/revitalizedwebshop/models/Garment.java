package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableProduct;

import java.util.ArrayList;
import java.util.List;

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
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany(mappedBy = "garments")
    List<Order> orders = new ArrayList<>();

    // Relations
    @OneToOne(mappedBy = "garment")
    private File file;
}