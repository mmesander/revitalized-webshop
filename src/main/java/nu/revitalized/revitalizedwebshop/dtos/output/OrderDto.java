package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class OrderDto {
    // Variables
    private Long orderNumber;
    private Date orderDate;
    private String status;
    private Boolean isPayed;
    private String discountCode;
    private Double totalAmount;
}
