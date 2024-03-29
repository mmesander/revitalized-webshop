package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Boolean isPaid;
    private String discountCode;
    private Double totalAmount;

    // Relations
    @ManyToMany
    @JoinTable(
            name = "order_garments",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "garment_id")
    )
    private List<Garment> garments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "order_supplements",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "supplement_id")
    )
    private List<Supplement> supplements = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_orders")
    private User user;

    @ManyToOne
    @JoinColumn(name = "shipping_details")
    private ShippingDetails shippingDetails;
}