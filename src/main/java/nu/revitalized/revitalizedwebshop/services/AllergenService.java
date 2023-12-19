package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.output.AllergenDto;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.helpers.HelperDtoTransferAllergen;
import nu.revitalized.revitalizedwebshop.models.Allergen;
import nu.revitalized.revitalizedwebshop.repositories.AllergenRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AllergenService {
    private final AllergenRepository allergenRepository;

    public AllergenService(AllergenRepository allergenRepository) {
        this.allergenRepository = allergenRepository;
    }


    // Get Methods
    public List<AllergenDto> getAllAllergens() {
        List<Allergen> allergens = allergenRepository.findAll();
        List<AllergenDto> allergenDtos = new ArrayList<>();

        for (Allergen allergen : allergens) {
            AllergenDto allergenDto = HelperDtoTransferAllergen.transferToDto(allergen);
            allergenDtos.add(allergenDto);
        }

        if (allergenDtos.isEmpty()) {
            throw new RecordNotFoundException("No allergens found");
        } else {
            return allergenDtos;
        }
    }

    public List<AllergenDto> getAllAllergensByName(String name) {
        List<Allergen> allergens = allergenRepository.findAll();
        List<AllergenDto> allergenDtos = new ArrayList<>();

        for (Allergen allergen : allergens) {
            AllergenDto allergenDto = HelperDtoTransferAllergen.transferToDto(allergen);
            allergenDtos.add(allergenDto);
        }

        if (allergenDtos.isEmpty()) {
            throw new RecordNotFoundException("No allergens named: " + name + " are found");
        } else {
            return allergenDtos;
        }
    }

    public AllergenDto getAllergenById(Long id) {
        Optional<Allergen> allergen = allergenRepository.findById(id);

        if (allergen.isPresent()) {
            return HelperDtoTransferAllergen.transferToDto(allergen.get());
        } else {
            throw new RecordNotFoundException("No allergen found with id: " + id);
        }
    }

    public void deleteAllergen(Long id) {
        Optional<Allergen> allergen = allergenRepository.findById(id);

        if (allergen.isPresent()) {
            allergenRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No allergen found with id: " + id);
        }
    }
}

