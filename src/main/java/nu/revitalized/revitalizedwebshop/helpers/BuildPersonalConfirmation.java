package nu.revitalized.revitalizedwebshop.helpers;

public class BuildPersonalConfirmation {
    public static String buildPersonalConfirmation(String confirmation, String username) {

        String personalConfirmation = confirmation +
                " " +
                "from user with username: " +
                username;

        return personalConfirmation;
    }
}
