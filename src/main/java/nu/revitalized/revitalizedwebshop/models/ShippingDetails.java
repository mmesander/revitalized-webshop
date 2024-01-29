package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shipping_details")
public class ShippingDetails {
    // Variables
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String shippingDetailsName;
    private String name;
    private String country;
    private String city;
    private String zipCode;
    private String street;
    private String houseNumber;
    private String email;
}
