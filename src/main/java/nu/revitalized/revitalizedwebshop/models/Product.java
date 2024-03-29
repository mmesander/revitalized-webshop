package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Product {
    // Variables
    @SequenceGenerator(name = "products_seq", allocationSize = 2137, initialValue = 1000462)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    private String brand;
    private String description;
    private Double price;
    private Integer stock;

    @Column(name = "average_rating")
    private Double averageRating;
}