package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import java.util.Date;

public class CreateDate {
    public static Date createDate() {
        long timeStamp = System.currentTimeMillis();

        return new Date(timeStamp);
    }
}