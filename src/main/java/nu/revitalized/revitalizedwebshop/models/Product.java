package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//TODO in de variablen moet nog een lijst met reviews komen;
//TODO in de variablen moet nog een foto komen;
//TODO de class moet een abstracte super class worden

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    // Variables
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String brand;
    private String description;
    private Double price;
    @Column(name = "average_rating")
    private Double averageRating;
}
