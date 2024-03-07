package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsPatchInputDto;
import org.apache.commons.lang3.StringUtils;
import static nu.revitalized.revitalizedwebshop.helpers.NameFormatter.formatName;

public class BuildFullName {
    public static String buildFullName(ShippingDetailsInputDto inputDto) {
        return buildFullNameHelper(inputDto.getFirstName(), inputDto.getMiddleName(), inputDto.getLastName());
    }

    public static String buildFullNamePatch(ShippingDetailsPatchInputDto inputDto) {
        return buildFullNameHelper(inputDto.getFirstName(), inputDto.getMiddleName(), inputDto.getLastName());
    }

    private static String buildFullNameHelper(String firstName, String middleName, String lastName) {
        StringBuilder fullName = new StringBuilder();
        fullName.append(formatName(firstName));
        if (StringUtils.isNotBlank(middleName)) {
            fullName.append(" ").append(middleName.toLowerCase());
        }
        fullName.append(" ").append(formatName(lastName));
        return fullName.toString();
    }
}