package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDate {
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return dateFormat.format(date);
    }
}