package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.GarmentInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.GarmentPatchInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.GarmentDto;
import nu.revitalized.revitalizedwebshop.dtos.output.OrderItemDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ReviewDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.helpers.CalculateAverageRating;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.repositories.GarmentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.*;
import static nu.revitalized.revitalizedwebshop.helpers.BuildConfirmation.buildSpecificConfirmation;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.helpers.CalculateAverageRating.calculateAverageRating;
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.services.ReviewService.reviewToDto;
import static nu.revitalized.revitalizedwebshop.specifications.GarmentSpecification.*;

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

        if (garment.getReviews() != null) {
            List<ReviewDto> dtos = new ArrayList<>();
            for (Review review : garment.getReviews()) {
                dtos.sort(Comparator.comparing(ReviewDto::getDate).reversed());
                dtos.add(reviewToDto(review));
            }
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
            Double price,
            Double minPrice,
            Double maxPrice,
            Integer stock,
            Integer minStock,
            Integer maxStock,
            Double averageRating,
            Double minRating,
            Double maxRating,
            String size,
            String color
    ) {
        Specification<Garment> params = Specification.where
                        (StringUtils.isBlank(name) ? null : getGarmentNameLikeFilter(name))
                .and(StringUtils.isBlank(brand) ? null : getGarmentBrandLikeFilter(brand))
                .and(price == null ? null : getGarmentPriceLikeFilter(price))
                .and(minPrice == null ? null : getGarmentPriceMoreThanFilter(minPrice))
                .and(maxPrice == null ? null : getGarmentPriceLessThanFilter(maxPrice))
                .and(stock == null ? null : getGarmentStockLikeFilter(stock))
                .and(minStock == null ? null : getGarmentStockMoreThanFilter(minStock))
                .and(maxStock == null ? null : getGarmentStockLessThanFilter(maxStock))
                .and(averageRating == null ? null : getGarmentAverageRatingLikeFilter(averageRating))
                .and(minRating == null ? null : getGarmentAverageRatingMoreThanFilter(maxRating))
                .and(maxRating == null ? null : getGarmentAverageRatingLessThanFilter(maxRating))
                .and(StringUtils.isBlank(size) ? null : getGarmentSizeLikeFilter(size))
                .and(StringUtils.isBlank(color) ? null : getGarmentColorLike(color));

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