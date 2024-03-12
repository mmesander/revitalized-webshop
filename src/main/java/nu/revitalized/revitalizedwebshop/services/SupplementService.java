package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.SupplementInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.SupplementPatchInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.*;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Allergen;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.AllergenRepository;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import nu.revitalized.revitalizedwebshop.specifications.SupplementSpecification;
import org.springframework.stereotype.Service;
import java.util.*;
import static nu.revitalized.revitalizedwebshop.helpers.BuildConfirmation.buildSpecificConfirmation;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.helpers.CalculateAverageRating.calculateAverageRating;
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.services.AllergenService.allergenToShortDto;
import static nu.revitalized.revitalizedwebshop.services.ReviewService.reviewToDto;

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
            Set<ShortAllergenDto> shortAllergenDtos = new TreeSet<>(Comparator.comparing(ShortAllergenDto::getId));
            for (Allergen allergen : supplement.getAllergens()) {
                shortAllergenDtos.add(allergenToShortDto(allergen));
            }
            supplementDto.setAllergens(shortAllergenDtos);
        }

        if (supplement.getReviews() != null) {
            List<ReviewDto> dtos = new ArrayList<>();
            for (Review review : supplement.getReviews()) {
                dtos.add(reviewToDto(review));
            }
            dtos.sort(Comparator.comparing(ReviewDto::getDate).reversed());
            supplementDto.setReviews(dtos);
            supplementDto.setAverageRating(calculateAverageRating(supplement));
        }

        return supplementDto;
    }

    public static ShortSupplementDto supplementToShortDto(Supplement supplement) {
        ShortSupplementDto shortSupplementDto = new ShortSupplementDto();

        copyProperties(supplement, shortSupplementDto);

        return shortSupplementDto;
    }

    public static OrderItemDto supplementToOrderItemDto(Supplement supplement) {
        OrderItemDto orderItemDto = new OrderItemDto();

        copyProperties(supplement, orderItemDto);
        orderItemDto.setQuantity(1);

        return orderItemDto;
    }

    // CRUD Methods
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
        Supplement supplement = supplementRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Supplement", id)));

        return supplementToDto(supplement);
    }

    public List<SupplementDto> getSupplementsByParam(
            String name,
            String brand,
            Double minPrice,
            Double maxPrice,
            Integer minStock,
            Integer maxStock,
            Double minRating,
            Double maxRating,
            String contains
    ) {
        SupplementSpecification params = new SupplementSpecification(
                name, brand, minPrice, maxPrice, minStock, maxStock, minRating, maxRating, contains
        );

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

    public SupplementDto updateSupplement(Long id, SupplementInputDto inputDto) {
        Supplement supplement = supplementRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Supplement", id)));

        copyProperties(inputDto, supplement);
        Supplement updatedSupplement = supplementRepository.save(supplement);

        return supplementToDto(updatedSupplement);
    }

    public SupplementDto patchSupplement(Long id, SupplementPatchInputDto inputDto) {
        Supplement supplement = supplementRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Supplement", id)));

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
    }

    public String deleteSupplement(Long id) {
        Supplement supplement = supplementRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Supplement", id)));

        supplementRepository.deleteById(id);

        return buildSpecificConfirmation("Supplement", supplement.getName(), id);
    }

    // Relation - Allergen Methods
    public SupplementDto assignAllergenToSupplement(Long supplementId, Long allergenId) {
        Supplement supplement = supplementRepository.findById(supplementId)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Supplement", supplementId)));

        Allergen allergen = allergenRepository.findById(allergenId)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Allergen", allergenId)));

        if (supplement.getAllergens().contains(allergen)) {
            throw new BadRequestException("Supplement already contains allergen: " + allergen.getName() + " with id: " + allergenId);
        }

        supplement.getAllergens().add(allergen);
        supplementRepository.save(supplement);

        return supplementToDto(supplement);
    }

    public SupplementDto removeAllergenFromSupplement(Long supplementId, Long allergenId) {
        Supplement supplement = supplementRepository.findById(supplementId)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Supplement", supplementId)));

        Allergen allergen = allergenRepository.findById(allergenId)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Allergen", allergenId)));

        if (!supplement.getAllergens().remove(allergen)) {
            throw new BadRequestException("Supplement doesn't contain allergen: " + allergen.getName() + " with id: " + allergenId);
        }

        supplementRepository.save(supplement);

        return supplementToDto(supplement);
    }
}