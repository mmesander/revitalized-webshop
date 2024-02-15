package nu.revitalized.revitalizedwebshop.helpers;

public class BuildBadAssignRequest {
    public static String buildBadAssignRequest(String title, String titleName, Long titleId, Long reviewId) {

        return "Review with id: " +
                reviewId +
                " is already assigned to " +
                title +
                ": " +
                titleName +
                " with id: " +
                titleId;

    }
}
