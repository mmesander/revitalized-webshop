package nu.revitalized.revitalizedwebshop.helpers;

public class BuildSpecificConfirmation {
    public static String buildSpecificConfirmation(String title, String name, Long id) {

        return title +
                ": " +
                name +
                " with id: " +
                id.toString() +
                " is removed";
    }
}
