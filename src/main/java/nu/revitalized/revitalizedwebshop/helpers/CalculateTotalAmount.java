package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.models.Order;
import nu.revitalized.revitalizedwebshop.models.Supplement;

public class CalculateTotalAmount {
    public static Double calculateTotalAmount(Order order) {
        Double totalAmount = 0.00;

        for (Supplement supplement : order.getSupplements()) {
            totalAmount = totalAmount + supplement.getPrice();
        }

        for (Garment garment : order.getGarments()) {
            totalAmount = totalAmount + garment.getPrice();
        }

        return totalAmount;
    }
}
