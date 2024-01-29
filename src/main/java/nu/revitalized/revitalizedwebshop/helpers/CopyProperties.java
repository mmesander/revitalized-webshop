package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import org.springframework.beans.BeanUtils;

public class CopyProperties {
    public static <Input, Output> void copyProperties(Input source, Output destination) {
        BeanUtils.copyProperties(source, destination);
    }
}
