package nu.revitalized.revitalizedwebshop.dtos.input;

// Imports
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidText;

@Getter
@Setter
public class AllergenInputDto {
    @NotNull(message = "Name is required")
    @ValidText(fieldName = "Name")
    private String name;
}
