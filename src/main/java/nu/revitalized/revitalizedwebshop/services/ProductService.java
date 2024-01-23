package nu.revitalized.revitalizedwebshop.services;

// Imports
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

    // CRUD Methods --> GET Methods


    // CRUD Methods --> POST Methods
    // CRUD Methods --> PUT/PATCH Methods
    // CRUD Methods --> DELETE Methods
    // Relations Methods
}
