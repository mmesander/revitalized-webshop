package nu.revitalized.revitalizedwebshop.services;

import static nu.revitalized.revitalizedwebshop.helpers.CopyPropertiesHelper.copyProperties;
import nu.revitalized.revitalizedwebshop.dtos.input.GarmentInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.GarmentDto;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.repositories.GarmentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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


    // CRUD Methods --> POST Methods
    // CRUD Methods --> PUT/PATCH Methods
    // CRUD Methods --> DELETE Methods
}
