package nu.revitalized.revitalizedwebshop.helpers;

public class BuildConfirmation {
    public static String buildSpecificConfirmation(String title, String name, Long id) {

        return title +
                ": " +
                name +
                " with id: " +
                id.toString() +
                " is removed";
    }

    public static String buildPersonalConfirmation(String confirmation, String targetName, String target) {

        return confirmation +
                " " +
                "from " +
                targetName +
                ": " +
                target;
    }
}