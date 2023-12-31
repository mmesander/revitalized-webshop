package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.AllergenInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.AllergenDto;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import static nu.revitalized.revitalizedwebshop.helpers.CopyPropertiesHelper.copyProperties;
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


    // Transfer Methods
    public Allergen transferToAllergen(AllergenInputDto inputDto) {
        Allergen allergen = new Allergen();

        copyProperties(inputDto, allergen);

        return allergen;
    }

    public AllergenDto transferAllergenToDto(Allergen allergen) {
        AllergenDto allergenDto = new AllergenDto();

        copyProperties(allergen, allergenDto);

        return allergenDto;
    }


    // Get Methods
    public List<AllergenDto> getAllAllergens() {
        List<Allergen> allergens = allergenRepository.findAll();
        List<AllergenDto> allergenDtos = new ArrayList<>();

        for (Allergen allergen : allergens) {
            AllergenDto allergenDto = transferAllergenToDto(allergen);
            allergenDtos.add(allergenDto);
        }

        if (allergenDtos.isEmpty()) {
            throw new RecordNotFoundException("No allergens found");
        } else {
            return allergenDtos;
        }
    }

    public AllergenDto getAllergenById(Long id) {
        Optional<Allergen> allergen = allergenRepository.findById(id);

        if (allergen.isPresent()) {
            return transferAllergenToDto(allergen.get());
        } else {
            throw new RecordNotFoundException("No allergen found with id: " + id);
        }
    }

    public List<AllergenDto> getAllAllergensByName(String name) {
        List<Allergen> allergens = allergenRepository.findAllergenByNameContainsIgnoreCase(name);
        List<AllergenDto> allergenDtos = new ArrayList<>();

        for (Allergen allergen : allergens) {
            AllergenDto allergenDto = transferAllergenToDto(allergen);
            allergenDtos.add(allergenDto);
        }

        if (allergenDtos.isEmpty()) {
            throw new RecordNotFoundException("No allergens named: " + name + " are found");
        } else {
            return allergenDtos;
        }
    }


    // Create Methods
    public AllergenDto createAllergen(AllergenInputDto inputDto) {
        Allergen allergen = transferToAllergen(inputDto);

        allergenRepository.save(allergen);

        return transferAllergenToDto(allergen);
    }


    // Update Methods
    public AllergenDto updateAllergen(Long id, AllergenInputDto inputDto) {
        Optional<Allergen> allergen = allergenRepository.findById(id);

        if (allergen.isPresent()) {
            Allergen newAllergen = allergen.get();

            newAllergen.setName(inputDto.getName());

            Allergen updatedAllergen = allergenRepository.save(newAllergen);

            return transferAllergenToDto(updatedAllergen);
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
}

