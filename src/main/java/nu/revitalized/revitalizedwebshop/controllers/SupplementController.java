package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import jakarta.validation.Valid;
import nu.revitalized.revitalizedwebshop.dtos.input.IdInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.SupplementInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.SupplementPatchInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SupplementDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.SupplementService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriId;

@RestController
public class SupplementController {
    private final SupplementService supplementService;

    public SupplementController(SupplementService supplementService) {
        this.supplementService = supplementService;
    }

    // CRUD Requests
    @GetMapping("/products/supplements")
    public ResponseEntity<List<SupplementDto>> getAllSupplements() {
        List<SupplementDto> dtos = supplementService.getAllSupplements();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/products/supplements/{id}")
    public ResponseEntity<SupplementDto> getSupplementById(
            @PathVariable(value = "id") Long id
    ) {
        SupplementDto dto = supplementService.getSupplementById(id);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/products/supplements/search")
    public ResponseEntity<List<SupplementDto>> getSupplementsByParam(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating,
            @RequestParam(required = false) String contains
    ) {
        List<SupplementDto> dtos = supplementService.getSupplementsByParam(
                name, brand, minPrice, maxPrice, minStock, maxStock,
                minRating, maxRating, contains
        );

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/products/supplements/out-of-stock")
    public ResponseEntity<List<SupplementDto>> getOutOfStockSupplements() {
        List<SupplementDto> dtos = supplementService.getOutOfStockSupplements();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/products/supplements/in-stock")
    public ResponseEntity<List<SupplementDto>> getInStockSupplements() {
        List<SupplementDto> dtos = supplementService.getInStockSupplements();

        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("/products/supplements")
    public ResponseEntity<SupplementDto> createSupplement(
            @Valid
            @RequestBody SupplementInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            SupplementDto dto = supplementService.createSupplement(inputDto);

            URI uri = buildUriId(dto);

            return ResponseEntity.created(uri).body(dto);
        }
    }

    @PutMapping("/products/supplements/{id}")
    public ResponseEntity<SupplementDto> updateSupplement(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody SupplementInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            SupplementDto dto = supplementService.updateSupplement(id, inputDto);

            return ResponseEntity.ok().body(dto);
        }
    }

    @PatchMapping("/products/supplements/{id}")
    public ResponseEntity<SupplementDto> patchSupplement(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody SupplementPatchInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            SupplementDto dto = supplementService.patchSupplement(id, inputDto);

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping("/products/supplements/{id}")
    public ResponseEntity<Object> deleteSupplement(
            @PathVariable("id") Long id
    ) {
        String confirmation = supplementService.deleteSupplement(id);

        return ResponseEntity.ok().body(confirmation);
    }

    // Relation - Allergen Requests
    @PutMapping(value = "/products/supplements/{id}/allergens")
    public ResponseEntity<Object> assignAllergenToSupplement(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody IdInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            SupplementDto dto = supplementService.assignAllergenToSupplement(id, inputDto.getId());

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping(value = "/products/supplements/{id}/allergens")
    public ResponseEntity<Object> removeAllergenFromSupplement(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody IdInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            SupplementDto dto = supplementService.removeAllergenFromSupplement(id, inputDto.getId());

            return ResponseEntity.ok().body(dto);
        }
    }
}