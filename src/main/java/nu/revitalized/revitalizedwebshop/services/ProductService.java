package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.CopyPropertiesHelper.copyProperties;
import nu.revitalized.revitalizedwebshop.dtos.output.ProductDto;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.GarmentRepository;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final SupplementRepository supplementRepository;
    private final GarmentRepository garmentRepository;

    public ProductService(
            SupplementRepository supplementRepository,
            GarmentRepository garmentRepository
    ) {
        this.supplementRepository = supplementRepository;
        this.garmentRepository = garmentRepository;
    }


    // Transfer Methods
    public static ProductDto supplementToProductDto(Supplement supplement) {
        ProductDto productDto = new ProductDto();

        copyProperties(supplement, productDto);

        return productDto;
    }

    public static ProductDto garmentToProductDto(Garment garment) {
        ProductDto productDto = new ProductDto();

        copyProperties(garment, productDto);

        return productDto;
    }


    // CRUD Methods --> GET Methods
    public ProductDto getAllProducts() {
        List<Supplement> supplements = supplementRepository.findAll();
        List<Garment> garments = garmentRepository.findAll();

        for (Supplement supplement : supplements) {

        }
    }

    // CRUD Methods --> POST Methods
    // CRUD Methods --> PUT/PATCH Methods
    // CRUD Methods --> DELETE Methods
    // Relations Methods
}
