package nu.revitalized.revitalizedwebshop.dtos.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceInputDto {
    @NotNull
    private Double price;
}
