package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsInputDto;
import org.apache.commons.lang3.StringUtils;

public class BuildHouseNumber {
    public static String buildHouseNumber(ShippingDetailsInputDto inputDto) {
        if (StringUtils.isNotBlank(inputDto.getHouseNumberAddition())) {
            return inputDto.getHouseNumber() + inputDto.getHouseNumberAddition().toUpperCase();
        } else {
            return String.valueOf(inputDto.getHouseNumber());
        }
    }

}
