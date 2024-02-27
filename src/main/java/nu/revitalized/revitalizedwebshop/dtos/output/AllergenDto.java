package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableId;

import java.util.Set;

@Getter
@Setter
public class AllergenDto implements IdentifiableId {
    // Variables
    private Long id;
    private String name;


    // Relations
    private Set<ShortSupplementDto> supplements;
}
