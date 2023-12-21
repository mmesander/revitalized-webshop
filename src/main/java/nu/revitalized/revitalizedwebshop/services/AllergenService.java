package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.AllergenInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.AllergenDto;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.helpers.HelperDtoTransfer;
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
            AllergenDto allergenDto = HelperDtoTransfer.transferToDto(allergen);
            allergenDtos.add(allergenDto);
        }

        if (allergenDtos.isEmpty()) {
            throw new RecordNotFoundException("No allergens found");
        } else {
            return allergenDtos;
        }
    }

    public List<AllergenDto> getAllAllergensByName(String name) {
        List<Allergen> allergens = allergenRepository.findAllergenByNameContainsIgnoreCase(name);
        List<AllergenDto> allergenDtos = new ArrayList<>();

        for (Allergen allergen : allergens) {
            AllergenDto allergenDto = HelperDtoTransfer.transferToDto(allergen);
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
            return HelperDtoTransfer.transferToDto(allergen.get());
        } else {
            throw new RecordNotFoundException("No allergen found with id: " + id);
        }
    }


    // Delete methods
    public void deleteAllergen(Long id) {
        Optional<Allergen> allergen = allergenRepository.findById(id);

        if (allergen.isPresent()) {
            allergenRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No allergen found with id: " + id);
        }
    }


    // Create Methods
    public AllergenDto createAllergen(AllergenInputDto inputDto) {
        Allergen allergen = HelperDtoTransfer.transferToAllergen(inputDto);

        allergenRepository.save(allergen);

        return HelperDtoTransfer.transferToDto(allergen);
    }


    // Update Methods
    public AllergenDto updateAllergen(Long id, AllergenInputDto inputDto) {
        Optional<Allergen> allergen = allergenRepository.findById(id);

        if (allergen.isPresent()) {
            Allergen newAllergen = allergen.get();

            newAllergen.setName(inputDto.getName());

            Allergen updatedAllergen = allergenRepository.save(newAllergen);

            return HelperDtoTransfer.transferToDto(updatedAllergen);
        } else {
            throw new RecordNotFoundException("No allergen found with id: " + id);
        }
    }
}

