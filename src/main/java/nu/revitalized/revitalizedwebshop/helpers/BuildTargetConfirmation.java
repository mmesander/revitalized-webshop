package nu.revitalized.revitalizedwebshop.helpers;

public class BuildTargetConfirmation {
    public static String buildPersonalConfirmation(String confirmation, String targetName, String target) {

        return confirmation +
                " " +
                "from " +
                targetName +
                ": " +
                target;
    }
}
