package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import org.springframework.beans.BeanUtils;

public class CopyPropertiesHelper {
    public static <Input, Output> void copyProperties(Input source, Output destination) {
        BeanUtils.copyProperties(source, destination);
    }
}
