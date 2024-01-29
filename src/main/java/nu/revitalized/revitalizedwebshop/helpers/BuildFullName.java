package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.NameFormatter.formatName;
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsInputDto;
import org.apache.commons.lang3.StringUtils;

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
}
