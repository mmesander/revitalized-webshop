package nu.revitalized.revitalizedwebshop.helpers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BindingResultHelper {
    public static String handleBindingResultError(BindingResult bindingResult) {
        StringBuilder newString = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            newString.append(fieldError.getField());
            newString.append(": ");
            newString.append(fieldError.getDefaultMessage());
            newString.append("\n");
        }

        return newString.toString();
    }
}
