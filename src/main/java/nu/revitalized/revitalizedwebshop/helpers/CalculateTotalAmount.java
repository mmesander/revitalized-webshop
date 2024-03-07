package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import nu.revitalized.revitalizedwebshop.models.Discount;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.models.Order;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculateTotalAmount {
    public static Double calculateTotalAmount(Order order) {
        double totalAmount = 0.00;
        double discount = 0.00;


        for (Supplement supplement : order.getSupplements()) {
            totalAmount += supplement.getPrice();
        }

        for (Garment garment : order.getGarments()) {
            totalAmount += garment.getPrice();
        }

        if (order.getDiscountCode() != null && order.getUser() != null) {
            for (Discount userDiscount : order.getUser().getDiscounts()) {
                if (userDiscount.getName().equals(order.getDiscountCode())) {
                    discount = userDiscount.getValue();
                }
            }
            totalAmount = totalAmount - ((discount / 100) * totalAmount);
        }

        BigDecimal totalAmountRounded = BigDecimal.valueOf(totalAmount).setScale(2, RoundingMode.HALF_UP);

        return totalAmountRounded.doubleValue();
    }
}