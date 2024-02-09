package nu.revitalized.revitalizedwebshop.dtos.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdInputDto {
    @NotNull
    private Long id;
}
