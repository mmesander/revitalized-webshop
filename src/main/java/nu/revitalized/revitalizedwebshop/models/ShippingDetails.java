package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "shipping_details")
public class ShippingDetails {
    // Variables
    @SequenceGenerator(name = "shipping_details_seq", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipping_details_seq")
    private Long id;
    private String detailsName;
    private String name;
    private String country;
    private String city;
    private String zipCode;
    private String street;
    private String houseNumber;
    private String email;

    // Relations
    @ManyToOne
    @JoinColumn(name = "user_shipping_details")
    private User user;

    @OneToMany(mappedBy = "shippingDetails")
    private List<Order> orders = new ArrayList<>();
}