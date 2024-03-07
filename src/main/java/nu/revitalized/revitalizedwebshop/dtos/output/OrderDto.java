package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableOrderNumber;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderDto implements IdentifiableOrderNumber {
    // Variables
    private Long orderNumber;
    private Date orderDate;
    private String status;
    private Boolean isPaid;
    private String discountCode;
    private Double totalAmount;

    // Relations
    List<OrderItemDto> products;
    private String username;
    private ShortShippingDetailsDto shippingDetails;
}
