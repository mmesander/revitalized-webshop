package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.services.AllergenService.allergenToShortDto;
import static nu.revitalized.revitalizedwebshop.helpers.BuildConfirmation.buildSpecificConfirmation;
import static nu.revitalized.revitalizedwebshop.specifications.SupplementSpecification.*;
import nu.revitalized.revitalizedwebshop.dtos.input.SupplementInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.AllergenShortDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SupplementDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SupplementShortDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Allergen;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.AllergenRepository;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import java.util.*;

@Service
public class SupplementService {
    private final SupplementRepository supplementRepository;
    private final AllergenRepository allergenRepository;

    public SupplementService(
            SupplementRepository supplementRepository,
            AllergenRepository allergenRepository
    ) {
        this.supplementRepository = supplementRepository;
        this.allergenRepository = allergenRepository;
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

        if (supplement.getAllergens() != null) {
            Set<AllergenShortDto> allergenShortDtos = new HashSet<>();
            for (Allergen allergen : supplement.getAllergens()) {
                allergenShortDtos.add(allergenToShortDto(allergen));
            }
            supplementDto.setAllergens(allergenShortDtos);
        }

        return supplementDto;
    }

    public static SupplementShortDto supplementToShortDto(Supplement supplement) {
        SupplementShortDto supplementShortDto = new SupplementShortDto();

        copyProperties(supplement, supplementShortDto);

        return supplementShortDto;
    }


    // CRUD Methods --> GET Methods
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
            supplementDtos.sort(Comparator.comparing(SupplementDto::getId));
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

    public List<SupplementDto> getSupplementsByParam(
            String name,
            String brand,
            Double price,
            Double minPrice,
            Double maxPrice,
            Integer stock,
            Integer minStock,
            Integer maxStock,
            Double averageRating,
            Double minRating,
            Double maxRating,
            String contains
    ) {
        Specification<Supplement> params = Specification.where
                        (StringUtils.isBlank(name) ? null : getSupplementNameLikeFilter(name))
                .and(StringUtils.isBlank(brand) ? null : getSupplementBrandLikeFilter(brand))
                .and(price == null ? null : getSupplementPriceLikeFilter(price))
                .and(minPrice == null ? null : getSupplementPriceMoreThanFilter(minPrice))
                .and(maxPrice == null ? null : getSupplementPriceLessThanFilter(maxPrice))
                .and(stock == null ? null : getSupplementStockLikeFilter(stock))
                .and(minStock == null ? null : getSupplementStockMoreThanFilter(minStock))
                .and(maxStock == null ? null : getSupplementStockLessThanFilter(maxStock))
                .and(averageRating == null ? null : getSupplementAverageRatingLikeFilter(averageRating))
                .and(minRating == null ? null : getSupplementAverageRatingMoreThanFilter(maxRating))
                .and(maxRating == null ? null : getSupplementAverageRatingLessThanFilter(maxRating))
                .and(StringUtils.isBlank(contains) ? null : getSupplementContainsLikeFilter(contains));

        List<Supplement> filteredSupplements = supplementRepository.findAll(params);
        List<SupplementDto> supplementDtos = new ArrayList<>();

        for (Supplement supplement : filteredSupplements) {
            SupplementDto supplementDto = supplementToDto(supplement);
            supplementDtos.add(supplementDto);
        }

        if (supplementDtos.isEmpty()) {
            throw new RecordNotFoundException("No supplements found with the specified filters");
        } else {
            return supplementDtos;
        }
    }

    public List<SupplementDto> getOutOfStockSupplements() {
        List<Supplement> supplements = supplementRepository.findAll();
        List<SupplementDto> supplementDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            if (supplement.getStock() == 0) {
                SupplementDto supplementDto = supplementToDto(supplement);
                supplementDtos.add(supplementDto);
            }
        }

        if (supplementDtos.isEmpty()) {
            throw new RecordNotFoundException("No supplements out of stock found");
        } else {
            return supplementDtos;
        }
    }
    public List<SupplementDto> getInStockSupplements() {
        List<Supplement> supplements = supplementRepository.findAll();
        List<SupplementDto> supplementDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            if (supplement.getStock() > 0) {
                SupplementDto supplementDto = supplementToDto(supplement);
                supplementDtos.add(supplementDto);
            }
        }

        if (supplementDtos.isEmpty()) {
            throw new RecordNotFoundException("No supplements in stock found");
        } else {
            return supplementDtos;
        }
    }

    // CRUD Methods --> POST Methods
    public SupplementDto createSupplement(SupplementInputDto inputDto) {
        Supplement supplement = dtoToSupplement(inputDto);
        boolean exists = supplementRepository.existsByNameIgnoreCase(inputDto.getName());

        if (exists) {
            throw new InvalidInputException("Supplement with name: " + inputDto.getName() + " already exists.");
        } else {
            supplementRepository.save(supplement);

            return supplementToDto(supplement);
        }
    }

    // CRUD Methods --> PUT/PATCH Methods
    public SupplementDto updateSupplement(Long id, SupplementInputDto inputDto) {
        Optional<Supplement> optionalSupplement = supplementRepository.findById(id);

        if (optionalSupplement.isPresent()) {
            Supplement supplement = optionalSupplement.get();

            copyProperties(inputDto, supplement);

            Supplement updatedSupplement = supplementRepository.save(supplement);

            return supplementToDto(updatedSupplement);
        } else {
            throw new RecordNotFoundException("No supplement found with id: " + id);
        }
    }

    public SupplementDto patchSupplement(Long id, SupplementInputDto inputDto) {
        Optional<Supplement> optionalSupplement = supplementRepository.findById(id);

        if (optionalSupplement.isPresent()) {
            Supplement supplement = optionalSupplement.get();

            if (inputDto.getName() != null) {
                supplement.setName(inputDto.getName());
            }
            if (inputDto.getBrand() != null) {
                supplement.setBrand(inputDto.getBrand());
            }
            if (inputDto.getDescription() != null) {
                supplement.setDescription(inputDto.getDescription());
            }
            if (inputDto.getPrice() != null) {
                supplement.setPrice(inputDto.getPrice());
            }
            if (inputDto.getStock() != null) {
                supplement.setStock(inputDto.getStock());
            }
            if (inputDto.getContains() != null) {
                supplement.setContains(inputDto.getContains());
            }

            Supplement patchedSupplement = supplementRepository.save(supplement);

            return supplementToDto(patchedSupplement);
        } else {
            throw new RecordNotFoundException("No supplement found with id: " + id);
        }
    }

    // CRUD Methods --> DELETE Methods
    public String deleteSupplement(Long id) {
        Optional<Supplement> supplement = supplementRepository.findById(id);

        if (supplement.isPresent()) {
            supplementRepository.deleteById(id);

            return buildSpecificConfirmation("Supplement", supplement.get().getName(), id);
        } else {
            throw new RecordNotFoundException("No supplement found with id: " + id);
        }
    }


    // Relations Methods
    public SupplementDto assignAllergenToSupplement(Long supplementId, Long allergenId) {
        Optional<Supplement> optionalSupplement = supplementRepository.findById(supplementId);
        Optional<Allergen> optionalAllergen = allergenRepository.findById(allergenId);
        Set<Allergen> allergens;
        SupplementDto dto;

        if (optionalSupplement.isPresent() && optionalAllergen.isPresent()) {
            Supplement supplement = optionalSupplement.get();
            Allergen allergen = optionalAllergen.get();

            allergens = supplement.getAllergens();

            if (allergens.contains(allergen)) {
                throw new InvalidInputException("Supplement already contains allergen: " + allergen.getName() + " with id: " + allergenId);
            } else {
                allergens.add(allergen);
                supplement.setAllergens(allergens);
                supplementRepository.save(supplement);
                dto = supplementToDto(supplement);
            }
            return dto;
        } else {
            if (optionalSupplement.isEmpty() && optionalAllergen.isEmpty()) {
                throw new RecordNotFoundException("Supplement with id: "
                        + supplementId + " and allergen with id: "
                        + allergenId + " are not found");
            } else if (optionalSupplement.isEmpty()) {
                throw new RecordNotFoundException("Supplement with id: " + supplementId + " is not found");
            } else {
                throw new RecordNotFoundException("Allergen with id: " + allergenId + " is not found");
            }
        }
    }

    public SupplementDto removeAllergenFromSupplement(Long supplementId, Long allergenId) {
        Optional<Supplement> optionalSupplement = supplementRepository.findById(supplementId);
        Optional<Allergen> optionalAllergen = allergenRepository.findById(allergenId);
        Set<Allergen> allergens;
        SupplementDto dto;

        if (optionalSupplement.isPresent() && optionalAllergen.isPresent()) {
            Supplement supplement = optionalSupplement.get();
            Allergen allergen = optionalAllergen.get();

            allergens = supplement.getAllergens();

            if (!allergens.contains(allergen)) {
                throw new InvalidInputException("Supplement doesn't contain allergen: " + allergen.getName() + " with id: " + allergenId);
            } else {
                allergens.remove(allergen);
                supplement.setAllergens(allergens);
                supplementRepository.save(supplement);
                dto = supplementToDto(supplement);
            }
            return dto;
        } else {
            if (optionalSupplement.isEmpty() && optionalAllergen.isEmpty()) {
                throw new RecordNotFoundException("Supplement with id: "
                        + supplementId + " and allergen with id: "
                        + allergenId + " are not found");
            } else if (optionalSupplement.isEmpty()) {
                throw new RecordNotFoundException("Supplement with id: " + supplementId + " is not found");
            } else {
                throw new RecordNotFoundException("Allergen with id: " + allergenId + " is not found");
            }
        }
    }
}
