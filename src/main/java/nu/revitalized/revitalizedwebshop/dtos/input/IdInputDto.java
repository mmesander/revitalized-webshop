package nu.revitalized.revitalizedwebshop.dtos.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidInteger;

@Getter
@Setter
public class IdInputDto {
    @ValidInteger(fieldName = "Id")
    @NotNull
    private Long id;
}
