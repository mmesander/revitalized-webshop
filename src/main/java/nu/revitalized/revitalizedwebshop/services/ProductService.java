package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.services.SupplementService.supplementToDto;
import static nu.revitalized.revitalizedwebshop.services.GarmentService.garmentToDto;
import static nu.revitalized.revitalizedwebshop.helpers.BuildConfirmation.buildSpecificConfirmation;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.services.ReviewService.*;
import nu.revitalized.revitalizedwebshop.dtos.output.ProductDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ReviewDto;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.GarmentRepository;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import org.springframework.stereotype.Service;

import java.util.*;

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

        if (supplement.getReviews() != null) {
            Set<ReviewDto> dtos = new TreeSet<>(Comparator.comparing(ReviewDto::getDate).reversed());
            for (Review review : supplement.getReviews()) {
                dtos.add(reviewToDto(review));
            }
            productDto.setReviews(dtos);
        }

        return productDto;
    }

    public static ProductDto garmentToProductDto(Garment garment) {
        ProductDto productDto = new ProductDto();

        copyProperties(garment, productDto);

        if (garment.getReviews() != null) {
            Set<ReviewDto> dtos = new TreeSet<>(Comparator.comparing(ReviewDto::getDate).reversed());
            for (Review review : garment.getReviews()) {
                dtos.add(reviewToDto(review));
            }
            productDto.setReviews(dtos);
        }

        return productDto;
    }

    // CRUD Methods
    public List<ProductDto> getAllProducts() {
        List<Supplement> supplements = supplementRepository.findAll();
        List<Garment> garments = garmentRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            ProductDto productDto = supplementToProductDto(supplement);
            productDtos.add(productDto);
        }

        for (Garment garment : garments) {
            ProductDto productDto = garmentToProductDto(garment);
            productDtos.add(productDto);
        }

        if (productDtos.isEmpty()) {
            throw new RecordNotFoundException("No products found");
        } else {
            productDtos.sort(Comparator.comparing(ProductDto::getId));
            return productDtos;
        }
    }

    public Object getProductById(Long id) {
        Optional<Supplement> supplement = supplementRepository.findById(id);
        Optional<Garment> garment = garmentRepository.findById(id);

        if (supplement.isPresent()) {
            return supplementToDto(supplement.get());
        } else if (garment.isPresent()) {
            return garmentToDto(garment.get());
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Product", id));
        }
    }

    public List<ProductDto> getOutOfStockProducts() {
        List<Supplement> supplements = supplementRepository.findAll();
        List<Garment> garments = garmentRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            if (supplement.getStock() == 0) {
                ProductDto productDto = supplementToProductDto(supplement);
                productDtos.add(productDto);
            }
        }

        for (Garment garment : garments) {
            if (garment.getStock() == 0) {
                ProductDto productDto = garmentToProductDto(garment);
                productDtos.add(productDto);
            }
        }

        if (productDtos.isEmpty()) {
            throw new RecordNotFoundException("No products out of stock found");
        } else {
            return productDtos;
        }
    }

    public List<ProductDto> getInStockProducts() {
        List<Supplement> supplements = supplementRepository.findAll();
        List<Garment> garments = garmentRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            if (supplement.getStock() > 0) {
                ProductDto productDto = supplementToProductDto(supplement);
                productDtos.add(productDto);
            }
        }

        for (Garment garment : garments) {
            if (garment.getStock() > 0) {
                ProductDto productDto = garmentToProductDto(garment);
                productDtos.add(productDto);
            }
        }

        if (productDtos.isEmpty()) {
            throw new RecordNotFoundException("No products in stock found");
        } else {
            return productDtos;
        }
    }

    public String deleteProduct(Long id) {
        Optional<Supplement> supplement = supplementRepository.findById(id);
        Optional<Garment> garment = garmentRepository.findById(id);

        if (supplement.isPresent()) {
            supplementRepository.deleteById(id);

            return buildSpecificConfirmation("Supplement", supplement.get().getName(), id);
        } else if (garment.isPresent()) {
            garmentRepository.deleteById(id);

            return buildSpecificConfirmation("Garment", garment.get().getName(), id);
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Product", id));
        }
    }
}
