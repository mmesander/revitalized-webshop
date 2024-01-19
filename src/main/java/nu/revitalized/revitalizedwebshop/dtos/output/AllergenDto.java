package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AllergenDto {
    // Variables
    private Long id;
    private String name;


    // Relations
    private Set<SupplementDto> supplements;
}
