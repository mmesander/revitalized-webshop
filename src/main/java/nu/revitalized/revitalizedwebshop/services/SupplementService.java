package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.SupplementInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SupplementDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SupplementShortDto;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import org.springframework.stereotype.Service;
import static nu.revitalized.revitalizedwebshop.helpers.CopyPropertiesHelper.copyProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SupplementService {
    private final SupplementRepository supplementRepository;

    public SupplementService(SupplementRepository supplementRepository) {
        this.supplementRepository = supplementRepository;
    }


    // Transfer Methods
    public static Supplement dtoToSupplement(SupplementInputDto inputDto) {
        Supplement supplement = new Supplement();

        copyProperties(inputDto, supplement);

        return supplement;
    }

    public static SupplementDto supplementToDto(Supplement supplement) {
        SupplementDto supplementDto = new SupplementDto();

        copyProperties(supplement, supplementDto);

        return supplementDto;
    }

    public static SupplementShortDto supplementShortToDto(Supplement supplement) {
        SupplementShortDto supplementShortDto = new SupplementShortDto();

        copyProperties(supplement, supplementShortDto);

        return supplementShortDto;
    }


    // Get Methods
    public List<SupplementDto> getAllSupplements() {
        List<Supplement> supplements = supplementRepository.findAll();
        List<SupplementDto> supplementDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            SupplementDto supplementDto = supplementToDto(supplement);
            supplementDtos.add(supplementDto);
        }

        if (supplementDtos.isEmpty()) {
            throw new RecordNotFoundException("No supplements are found");
        } else {
            return supplementDtos;
        }
    }

    public SupplementDto getSupplementById(Long id) {
        Optional<Supplement> supplement = supplementRepository.findById(id);

        if (supplement.isPresent()) {
            return supplementToDto(supplement.get());
        } else {
            throw new RecordNotFoundException("No supplement found with id: " + id);
        }
    }

    public List<SupplementDto> getSupplementsByBrandAndName(String brand, String name) {
        List<Supplement> supplements = supplementRepository.
                findSupplementsByBrandContainsIgnoreCaseAndNameContainsIgnoreCase(brand, name);
        List<SupplementDto> supplementDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            SupplementDto supplementDto = supplementToDto(supplement);
            supplementDtos.add(supplementDto);
        }

        if (supplementDtos.isEmpty()) {
            throw new RecordNotFoundException("No supplements found with name: " + name + " and brand: " + brand);
        } else {
            return supplementDtos;
        }
    }

    public List<SupplementDto> getSupplementsByBrand(String brand) {
        List<Supplement> supplements = supplementRepository.findSupplementsByBrandContainsIgnoreCase(brand);
        List<SupplementDto> supplementDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            SupplementDto supplementDto = supplementToDto(supplement);
            supplementDtos.add(supplementDto);
        }

        if (supplementDtos.isEmpty()) {
            throw new RecordNotFoundException("No supplements found with brand: " + brand);
        } else {
            return supplementDtos;
        }
    }

    public List<SupplementDto> getSupplementsByName(String name) {
        List<Supplement> supplements = supplementRepository.findSupplementByNameContainsIgnoreCase(name);
        List<SupplementDto> supplementDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            SupplementDto supplementDto = supplementToDto(supplement);
            supplementDtos.add(supplementDto);
        }

        if (supplementDtos.isEmpty()) {
            throw new RecordNotFoundException("No supplements found with name: " + name);
        } else {
            return supplementDtos;
        }
    }

    public List<SupplementDto> getSupplementsByPrice(Double price) {
        List<Supplement> supplements = supplementRepository.findSupplementsByPriceLessThanEqual(price);
        List<SupplementDto> supplementDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            SupplementDto supplementDto = supplementToDto(supplement);
            supplementDtos.add(supplementDto);
        }

        if (supplementDtos.isEmpty()) {
            throw new RecordNotFoundException("No supplements found with a price lower or equal to " + price);
        } else {
            return supplementDtos;
        }
    }


    // Create Methods
    public SupplementDto createSupplement(SupplementInputDto inputDto) {
        Supplement supplement = dtoToSupplement(inputDto);

        supplementRepository.save(supplement);

        return supplementToDto(supplement);
    }


    // Update Methods
    public SupplementDto updateSupplement(Long id, SupplementInputDto inputDto) {
        Optional<Supplement> supplement = supplementRepository.findById(id);

        if (supplement.isPresent()) {
            Supplement presentSupplement = supplement.get();

            copyProperties(inputDto, presentSupplement);

            Supplement updatedSupplement = supplementRepository.save(presentSupplement);

            return supplementToDto(updatedSupplement);
        } else {
            throw new RecordNotFoundException("No supplement found with id: " + id);
        }
    }

    public SupplementDto patchSupplement(Long id, SupplementInputDto inputDto) {
        Optional<Supplement> supplement = supplementRepository.findById(id);

        if (supplement.isPresent()) {
            Supplement presentSupplement = supplement.get();

            if (inputDto.getName() != null) {
                presentSupplement.setName(inputDto.getName());
            }
            if (inputDto.getBrand() != null) {
                presentSupplement.setBrand(inputDto.getBrand());
            }
            if (inputDto.getDescription() != null) {
                presentSupplement.setDescription(inputDto.getDescription());
            }
            if (inputDto.getPrice() != null) {
                presentSupplement.setPrice(inputDto.getPrice());
            }
            if (inputDto.getContains() != null) {
                presentSupplement.setContains(inputDto.getContains());
            }

            Supplement patchedSupplement = supplementRepository.save(presentSupplement);

            return supplementToDto(patchedSupplement);
        } else {
            throw new RecordNotFoundException("No supplement found with id: " + id);
        }
    }


    // Delete Methods
    public void deleteSupplement(Long id) {
        Optional<Supplement> supplement = supplementRepository.findById(id);

        if (supplement.isPresent()) {
            supplementRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No supplement found with id: " + id);
        }
    }
}
