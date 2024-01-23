package nu.revitalized.revitalizedwebshop.services;

import static nu.revitalized.revitalizedwebshop.helpers.CopyPropertiesHelper.copyProperties;
import nu.revitalized.revitalizedwebshop.dtos.input.GarmentInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.GarmentDto;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.repositories.GarmentRepository;
import org.springframework.stereotype.Service;

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
}
