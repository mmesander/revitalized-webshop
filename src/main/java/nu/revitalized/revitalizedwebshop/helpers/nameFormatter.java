package nu.revitalized.revitalizedwebshop.helpers;

public class nameFormatter {
    public static String formatName(String name) {
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase();

        return name;
    }
}
