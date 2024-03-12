package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.GarmentInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.GarmentPatchInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.GarmentDto;
import nu.revitalized.revitalizedwebshop.dtos.output.OrderItemDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ReviewDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.repositories.GarmentRepository;
import nu.revitalized.revitalizedwebshop.specifications.GarmentSpecification;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static nu.revitalized.revitalizedwebshop.helpers.BuildConfirmation.buildSpecificConfirmation;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.helpers.CalculateAverageRating.calculateAverageRating;
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.services.ReviewService.reviewToDto;

@Service
public class GarmentService {
    private final GarmentRepository garmentRepository;

    public GarmentService(GarmentRepository garmentRepository) {
        this.garmentRepository = garmentRepository;
    }

    // Transfer Methods
    public static Garment dtoToGarment(GarmentInputDto inputDto) {
        Garment garment = new Garment();

        copyProperties(inputDto, garment);

        return garment;
    }

    public static GarmentDto garmentToDto(Garment garment) {
        GarmentDto garmentDto = new GarmentDto();

        copyProperties(garment, garmentDto);

        if (garment.getReviews() != null && !garment.getReviews().isEmpty()) {
            List<ReviewDto> dtos = new ArrayList<>();
            for (Review review : garment.getReviews()) {
                dtos.add(reviewToDto(review));
            }
            dtos.sort(Comparator.comparing(ReviewDto::getDate).reversed());
            garmentDto.setReviews(dtos);
            garmentDto.setAverageRating(calculateAverageRating(garment));
        }

        return garmentDto;
    }

    public static OrderItemDto garmentToOrderItemDto(Garment garment) {
        OrderItemDto orderItemDto = new OrderItemDto();

        copyProperties(garment, orderItemDto);
        orderItemDto.setQuantity(1);

        return orderItemDto;
    }

    // CRUD Methods
    public List<GarmentDto> getAllGarments() {
        List<Garment> garments = garmentRepository.findAll();
        List<GarmentDto> garmentDtos = new ArrayList<>();

        for (Garment garment : garments) {
            GarmentDto garmentDto = garmentToDto(garment);
            garmentDtos.add(garmentDto);
        }

        if (garmentDtos.isEmpty()) {
            throw new RecordNotFoundException("No garments are found");
        } else {
            garmentDtos.sort(Comparator.comparing(GarmentDto::getId));
            return garmentDtos;
        }
    }

    public GarmentDto getGarmentById(Long id) {
        Garment garment = garmentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Garment", id)));

        return garmentToDto(garment);
    }

    public List<GarmentDto> getGarmentsByParam(
            String name,
            String brand,
            Double minPrice,
            Double maxPrice,
            Integer minStock,
            Integer maxStock,
            Integer minRating,
            Integer maxRating,
            String size,
            String color
    ) {
        GarmentSpecification params = new GarmentSpecification(
                name, brand, minPrice, maxPrice, minStock, maxStock,
                minRating, maxRating, size, color
        );

        List<Garment> filteredGarments = garmentRepository.findAll(params);
        List<GarmentDto> garmentDtos = new ArrayList<>();

        for (Garment garment : filteredGarments) {
            GarmentDto garmentDto = garmentToDto(garment);
            garmentDtos.add(garmentDto);
        }

        if (garmentDtos.isEmpty()) {
            throw new RecordNotFoundException("No garments found with the specified filters");
        } else {
            return garmentDtos;
        }
    }

    public List<GarmentDto> getOutOfStockGarments() {
        List<Garment> garments = garmentRepository.findAll();
        List<GarmentDto> garmentDtos = new ArrayList<>();

        for (Garment garment : garments) {
            if (garment.getStock() == 0) {
                GarmentDto garmentDto = garmentToDto(garment);
                garmentDtos.add(garmentDto);
            }
        }

        if (garmentDtos.isEmpty()) {
            throw new RecordNotFoundException("No garments out of stock found");
        } else {
            return garmentDtos;
        }
    }

    public List<GarmentDto> getInOfStockGarments() {
        List<Garment> garments = garmentRepository.findAll();
        List<GarmentDto> garmentDtos = new ArrayList<>();

        for (Garment garment : garments) {
            if (garment.getStock() > 0) {
                GarmentDto garmentDto = garmentToDto(garment);
                garmentDtos.add(garmentDto);
            }
        }

        if (garmentDtos.isEmpty()) {
            throw new RecordNotFoundException("No garments in stock found");
        } else {
            return garmentDtos;
        }
    }

    public GarmentDto createGarment(GarmentInputDto inputDto) {
        Garment garment = dtoToGarment(inputDto);
        boolean exists = garmentRepository.existsByNameIgnoreCase(inputDto.getName());

        if (exists) {
            throw new InvalidInputException("Garment with name: " + inputDto.getName() + " already exists");
        } else {
            garmentRepository.save(garment);

            return garmentToDto(garment);
        }
    }

    public GarmentDto updateGarment(Long id, GarmentInputDto inputDto) {
        Garment garment = garmentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Garment", id)));

        copyProperties(inputDto, garment);
        Garment updatedGarment = garmentRepository.save(garment);

        return garmentToDto(updatedGarment);
    }

    public GarmentDto patchGarment(Long id, GarmentPatchInputDto inputDto) {
        Garment garment = garmentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Garment", id)));

        garment.setName(inputDto.getName() != null ? inputDto.getName() : garment.getName());
        garment.setBrand(inputDto.getBrand() != null ? inputDto.getBrand() : garment.getBrand());
        garment.setDescription(inputDto.getDescription() != null ? inputDto.getDescription() : garment.getDescription());
        garment.setPrice(inputDto.getPrice() != null ? inputDto.getPrice() : garment.getPrice());
        garment.setStock(inputDto.getStock() != null ? inputDto.getStock() : garment.getStock());
        garment.setSize(inputDto.getSize() != null ? inputDto.getSize() : garment.getSize());
        garment.setColor(inputDto.getColor() != null ? inputDto.getColor() : garment.getColor());

        Garment patchedGarment = garmentRepository.save(garment);

        return garmentToDto(patchedGarment);
    }

    public String deleteGarment(Long id) {
        Garment garment = garmentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Garment", id)));

        garmentRepository.deleteById(id);

        return buildSpecificConfirmation("Garment", garment.getName(), id);
    }
}