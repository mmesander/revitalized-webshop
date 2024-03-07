package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsPatchInputDto;
import org.apache.commons.lang3.StringUtils;
import static nu.revitalized.revitalizedwebshop.helpers.NameFormatter.formatName;

public class BuildFullName {
    public static String buildFullName(ShippingDetailsInputDto inputDto) {
        StringBuilder fullName = new StringBuilder();

        if (StringUtils.isNotBlank(inputDto.getMiddleName())) {
            fullName.append(formatName(inputDto.getFirstName()));
            fullName.append(" ");
            fullName.append(inputDto.getMiddleName().toLowerCase());
            fullName.append(" ");
            fullName.append(formatName(inputDto.getLastName()));
            return fullName.toString();
        } else {
            fullName.append(formatName(inputDto.getFirstName()));
            fullName.append(" ");
            fullName.append(formatName(inputDto.getLastName()));

            return fullName.toString();
        }
    }

    public static String buildFullNamePatch(ShippingDetailsPatchInputDto inputDto) {
        StringBuilder fullName = new StringBuilder();

        if (StringUtils.isNotBlank(inputDto.getMiddleName())) {
            fullName.append(formatName(inputDto.getFirstName()));
            fullName.append(" ");
            fullName.append(inputDto.getMiddleName().toLowerCase());
            fullName.append(" ");
            fullName.append(formatName(inputDto.getLastName()));
            return fullName.toString();
        } else {
            fullName.append(formatName(inputDto.getFirstName()));
            fullName.append(" ");
            fullName.append(formatName(inputDto.getLastName()));

            return fullName.toString();
        }
    }
}