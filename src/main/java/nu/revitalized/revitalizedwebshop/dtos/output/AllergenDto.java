package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.Identifiable;

import java.util.Set;

@Getter
@Setter
public class AllergenDto implements Identifiable {
    // Variables
    private Long id;
    private String name;


    // Relations
    private Set<SupplementShortDto> supplements;
}
