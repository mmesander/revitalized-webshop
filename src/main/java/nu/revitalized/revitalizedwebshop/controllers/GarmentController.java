package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriId;
import nu.revitalized.revitalizedwebshop.dtos.input.GarmentInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.GarmentDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.GarmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class GarmentController {
    private final GarmentService garmentService;

    public GarmentController(GarmentService garmentService) {
        this.garmentService = garmentService;
    }

    // CRUD Requests
    @GetMapping("/products/garments")
    public ResponseEntity<List<GarmentDto>> getAllGarments() {
        List<GarmentDto> dtos = garmentService.getAllGarments();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/products/garments/{id}")
    public ResponseEntity<GarmentDto> getGarmentById(
            @PathVariable("id") Long id
    ) {
        GarmentDto dto = garmentService.getGarmentById(id);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/products/garments/search")
    public ResponseEntity<List<GarmentDto>> getGarmentsByParam(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock,
            @RequestParam(required = false) Double averageRating,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String color
    ) {
        List<GarmentDto> dtos = garmentService.getGarmentsByParam(
                name, brand, price, minPrice, maxPrice, stock, minStock, maxStock,
                averageRating, minRating, maxRating, size, color
        );

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/products/garments/out-of-stock")
    public ResponseEntity<List<GarmentDto>> getOutOfStockGarments() {
        List<GarmentDto> dtos = garmentService.getOutOfStockGarments();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/products/garments/in-stock")
    public ResponseEntity<List<GarmentDto>> getInStockGarments() {
        List<GarmentDto> dtos = garmentService.getInOfStockGarments();

        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("/products/garments")
    public ResponseEntity<GarmentDto> createGarment(
            @Valid
            @RequestBody GarmentInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            GarmentDto dto = garmentService.createGarment(inputDto);

            URI uri = buildUriId(dto);

            return ResponseEntity.created(uri).body(dto);
        }
    }

    @PutMapping("/products/garments/{id}")
    public ResponseEntity<GarmentDto> updateGarment(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody GarmentInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            GarmentDto dto = garmentService.updateGarment(id, inputDto);

            return ResponseEntity.ok().body(dto);
        }
    }

    @PatchMapping("/products/garments/{id}")
    public ResponseEntity<GarmentDto> patchGarment(
            @PathVariable("id") Long id,
            @RequestBody GarmentInputDto inputDto
    ) {
        GarmentDto dto = garmentService.patchGarment(id, inputDto);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/products/garments/{id}")
    public ResponseEntity<Object> deleteGarment(
            @PathVariable("id") Long id
    ) {
        String confirmation = garmentService.deleteGarment(id);

        return ResponseEntity.ok().body(confirmation);
    }
}
