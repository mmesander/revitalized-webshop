package nu.revitalized.revitalizedwebshop.services;

import static nu.revitalized.revitalizedwebshop.helpers.CopyPropertiesHelper.copyProperties;
import static nu.revitalized.revitalizedwebshop.specifications.GarmentSpecification.*;
import static nu.revitalized.revitalizedwebshop.specifications.SupplementSpecification.*;
import static nu.revitalized.revitalizedwebshop.specifications.SupplementSpecification.getSupplementAverageRatingLessThanFilter;

import nu.revitalized.revitalizedwebshop.dtos.input.GarmentInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.GarmentDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SearchDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.repositories.GarmentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        return garmentDto;
    }


    // CRUD Methods --> GET Methods
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
            return garmentDtos;
        }
    }

    public GarmentDto getGarmentById(Long id) {
        Optional<Garment> garment = garmentRepository.findById(id);

        if (garment.isPresent()) {
            return garmentToDto(garment.get());
        } else {
            throw new RecordNotFoundException("No garment found with id: " + id);
        }
    }

    public List<GarmentDto> getGarmentsByParam(
            String name,
            String brand,
            Double price,
            Double minPrice,
            Double maxPrice,
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
                .and(averageRating == null ? null : getGarmentAverageRatingLikeFilter(averageRating))
                .and(minRating == null ? null : getGarmentAverageRatingMoreThanFilter(maxRating))
                .and(maxRating == null ? null :getGarmentAverageRatingLessThanFilter(maxRating))
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

    public List<GarmentDto> getGarmentsByParam2(SearchDto searchDto) {
        List<Garment> garments = garmentRepository.findGarmentsByCriteria(
                searchDto.getName(),
                searchDto.getBrand(),
                searchDto.getPrice(),
                searchDto.getAverageRating(),
                searchDto.getSize(),
                searchDto.getColor()
        );
        List<GarmentDto> garmentDtos = new ArrayList<>();

        for (Garment garment : garments) {
            GarmentDto garmentDto = garmentToDto(garment);
            garmentDtos.add(garmentDto);
        }

        if (garmentDtos.isEmpty()) {
            throw new RecordNotFoundException("No garments found with the specified criteria");
        } else {
            return garmentDtos;
        }
    }

    // CRUD Methods --> POST Methods
    public GarmentDto createGarment(GarmentInputDto inputDto) {
        Garment garment = dtoToGarment(inputDto);
        List<GarmentDto> dtos = getAllGarments();
        boolean isUnique = true;

        for (GarmentDto garmentDto : dtos) {
            if (garmentDto.getName().equalsIgnoreCase(inputDto.getName())) {
                isUnique = false;
            }
        }

        if (isUnique) {
            garmentRepository.save(garment);
            return garmentToDto(garment);
        } else {
            throw new InvalidInputException("Garment with name: " + inputDto.getName() + " already exists");
        }
    }

    // CRUD Methods --> PUT/PATCH Methods
    public GarmentDto updateGarment(Long id, GarmentInputDto inputDto) {
        Optional<Garment> optionalGarment = garmentRepository.findById(id);

        if (optionalGarment.isPresent()) {
            Garment garment = optionalGarment.get();

            copyProperties(inputDto, garment);

            Garment updatedGarment = garmentRepository.save(garment);

            return garmentToDto(updatedGarment);
        } else {
            throw new RecordNotFoundException("No garment found with id: " + id);
        }
    }

    public GarmentDto patchGarment(Long id, GarmentInputDto inputDto) {
        Optional<Garment> optionalGarment = garmentRepository.findById(id);

        if (optionalGarment.isPresent()) {
            Garment garment = optionalGarment.get();

            if (inputDto.getName() != null) {
                garment.setName(inputDto.getName());
            }

            if (inputDto.getBrand() != null) {
                garment.setBrand(inputDto.getBrand());
            }

            if (inputDto.getDescription() != null) {
                garment.setDescription(inputDto.getDescription());
            }

            if (inputDto.getPrice() != null) {
                garment.setPrice(inputDto.getPrice());
            }

            if (inputDto.getSize() != null) {
                garment.setSize(inputDto.getSize());
            }

            if (inputDto.getColor() != null) {
                garment.setColor(inputDto.getColor());
            }

            Garment patchedGarment = garmentRepository.save(garment);

            return garmentToDto(patchedGarment);
        } else {
            throw new RecordNotFoundException("No garment found with id: " + id);
        }
    }

    // CRUD Methods --> DELETE Methods
    public void deleteGarment(Long id) {
        Optional<Garment> garment = garmentRepository.findById(id);

        if (garment.isPresent()) {
            garmentRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No garment found with id: " + id);
        }
    }


    // Relations Methods
}
