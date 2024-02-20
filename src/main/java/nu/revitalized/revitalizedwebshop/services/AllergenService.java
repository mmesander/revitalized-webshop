package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.services.SupplementService.supplementToShortDto;
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.BuildConfirmation.buildSpecificConfirmation;
import nu.revitalized.revitalizedwebshop.dtos.input.AllergenInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.AllergenDto;
import nu.revitalized.revitalizedwebshop.dtos.output.AllergenShortDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SupplementShortDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Allergen;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.AllergenRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AllergenService {
    private final AllergenRepository allergenRepository;

    public AllergenService(AllergenRepository allergenRepository) {
        this.allergenRepository = allergenRepository;
    }

    // Transfer Methods
    public static Allergen dtoToAllergen(AllergenInputDto inputDto) {
        Allergen allergen = new Allergen();

        copyProperties(inputDto, allergen);

        return allergen;
    }

    public static AllergenDto allergenToDto(Allergen allergen) {
        AllergenDto allergenDto = new AllergenDto();

        copyProperties(allergen, allergenDto);

        if (allergen.getSupplements() != null) {
            Set<SupplementShortDto> supplementShortDtos = new HashSet<>();
            for (Supplement supplement : allergen.getSupplements()) {
                supplementShortDtos.add(supplementToShortDto(supplement));
            }
            allergenDto.setSupplements(supplementShortDtos);
        }

        return allergenDto;
    }

    public static AllergenShortDto allergenToShortDto(Allergen allergen) {
        AllergenShortDto allergenShortDto = new AllergenShortDto();

        copyProperties(allergen, allergenShortDto);

        return allergenShortDto;
    }

    // CRUD Methods
    public List<AllergenDto> getAllAllergens() {
        List<Allergen> allergens = allergenRepository.findAll();
        List<AllergenDto> allergenDtos = new ArrayList<>();

        for (Allergen allergen : allergens) {
            AllergenDto allergenDto = allergenToDto(allergen);
            allergenDtos.add(allergenDto);
        }

        if (allergenDtos.isEmpty()) {
            throw new RecordNotFoundException("No allergens found");
        } else {
            allergenDtos.sort(Comparator.comparing(AllergenDto::getId));
            return allergenDtos;
        }
    }

    public AllergenDto getAllergenById(Long id) {
        Optional<Allergen> allergen = allergenRepository.findById(id);

        if (allergen.isPresent()) {
            return allergenToDto(allergen.get());
        } else {
            throw new RecordNotFoundException("No allergen found with id: " + id);
        }
    }

    public List<AllergenDto> getAllAllergensByName(String name) {
        List<Allergen> allergens = allergenRepository.findAllergenByNameContainsIgnoreCase(name);
        List<AllergenDto> allergenDtos = new ArrayList<>();

        for (Allergen allergen : allergens) {
            AllergenDto allergenDto = allergenToDto(allergen);
            allergenDtos.add(allergenDto);
        }

        if (allergenDtos.isEmpty()) {
            throw new RecordNotFoundException("No allergens named: " + name + " are found");
        } else {
            return allergenDtos;
        }
    }

    public AllergenDto createAllergen(AllergenInputDto inputDto) {
        Allergen allergen = dtoToAllergen(inputDto);
        boolean exists = allergenRepository.existsByNameIgnoreCase(inputDto.getName());

        if (exists) {
            throw new InvalidInputException("Allergen with name: " + inputDto.getName() + " already exists.");
        } else {
            allergenRepository.save(allergen);

            return allergenToDto(allergen);
        }
    }

    public AllergenDto updateAllergen(Long id, AllergenInputDto inputDto) {
        Optional<Allergen> allergen = allergenRepository.findById(id);

        if (allergen.isPresent()) {
            Allergen newAllergen = allergen.get();

            newAllergen.setName(inputDto.getName());

            Allergen updatedAllergen = allergenRepository.save(newAllergen);

            return allergenToDto(updatedAllergen);
        } else {
            throw new RecordNotFoundException("No allergen found with id: " + id);
        }
    }

    public String deleteAllergen(Long id) {
        Optional<Allergen> allergen = allergenRepository.findById(id);

        if (allergen.isPresent()) {
            allergenRepository.deleteById(id);

            return buildSpecificConfirmation("Allergen", allergen.get().getName(), id);
        } else {
            throw new RecordNotFoundException("No allergen found with id: " + id);
        }
    }
}

