package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    // Variables
    @SequenceGenerator(name = "orders_seq", allocationSize = 1, initialValue = 101)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq")
    private Long orderNumber;
    private Date orderDate;
    private String status;
    private boolean isPayed;
    private String discountCode;
    private Double totalAmount;

    // Relations
    // List<Supplement>
    // List<Garment>
    // List<Product> ?
    // ShippingDetails
    // User
}
