package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.NameFormatter.formatName;
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsInputDto;
import org.apache.commons.lang3.StringUtils;

public class BuildFullName {
    public static String buildFullName(ShippingDetailsInputDto inputDto) {
        if (StringUtils.isNotBlank(inputDto.getMiddleName())) {
            return formatName(inputDto.getFirstName() + " " +
                    inputDto.getMiddleName().toLowerCase() + " " +
                    formatName(inputDto.getLastName()));
        } else {
            return formatName(inputDto.getFirstName()) + " " + formatName(inputDto.getLastName());
        }
    }
}
