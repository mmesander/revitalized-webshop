package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.output.AllergenDto;
import nu.revitalized.revitalizedwebshop.repositories.AllergenRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AllergenService {
    private final AllergenRepository allergenRepository;

    public AllergenService(AllergenRepository allergenRepository) {
        this.allergenRepository = allergenRepository;
    }


    // Get Methods
    public List<AllergenDto> getAllAllergens() {

    }
}
