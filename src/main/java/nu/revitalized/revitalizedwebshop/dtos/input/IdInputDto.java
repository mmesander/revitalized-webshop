package nu.revitalized.revitalizedwebshop.dtos.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.ValidNumber;

@Getter
@Setter
public class IdInputDto {
    @ValidNumber(fieldName = "Id")
    @NotNull
    private Long id;
}
