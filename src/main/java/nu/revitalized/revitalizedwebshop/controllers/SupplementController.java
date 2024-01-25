package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import nu.revitalized.revitalizedwebshop.dtos.input.IdInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.PriceInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.SupplementInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SearchDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SupplementDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.SupplementService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class SupplementController {
    private final SupplementService supplementService;

    public SupplementController(SupplementService supplementService) {
        this.supplementService = supplementService;
    }


    // CRUD Requests -- GET Requests
    @GetMapping("/producten/supplementen")
    public ResponseEntity<List<SupplementDto>> getAllSupplements() {
        List<SupplementDto> dtos = supplementService.getAllSupplements();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/producten/supplementen/{id}")
    public ResponseEntity<SupplementDto> getSupplementById(
            @PathVariable(value = "id") Long id
    ) {
        SupplementDto dto = supplementService.getSupplementById(id);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/producten/supplementen/zoeken")
    public ResponseEntity<List<SupplementDto>> getSupplementsByParam(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double averageRating,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            @RequestParam(required = false) String contains
    ) {
        List<SupplementDto> dtos = supplementService.getSupplementsByParam(
                brand, name, price, minPrice, maxPrice, averageRating, minRating,
                maxRating, contains);

        return ResponseEntity.ok().body(dtos);
    }

    // CRUD Requests -- POST Requests
    @PostMapping("/producten/supplementen")
    public ResponseEntity<SupplementDto> createSupplement(
            @Valid
            @RequestBody SupplementInputDto inputDto,
            BindingResult bindingResult
    ) {
        SupplementDto dto;

        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            dto = supplementService.createSupplement(inputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + dto.getId()).toUriString());
            return ResponseEntity.created(uri).body(dto);
        }
    }

    // CRUD Requests -- PUT/PATCH Requests
    @PutMapping("/producten/supplementen/{id}")
    public ResponseEntity<SupplementDto> updateSupplement(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody SupplementInputDto inputDto,
            BindingResult bindingResult
    ) {
        SupplementDto dto;

        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            dto = supplementService.updateSupplement(id, inputDto);
        }

        return ResponseEntity.ok().body(dto);
    }

    @PatchMapping("/producten/supplementen/{id}")
    public ResponseEntity<SupplementDto> patchSupplement(
            @PathVariable("id") Long id,
            @RequestBody SupplementInputDto inputDto
    ) {
        SupplementDto dto = supplementService.patchSupplement(id, inputDto);

        return ResponseEntity.ok().body(dto);
    }

    // CRUD Requests -- DELETE Requests
    @DeleteMapping("/producten/supplementen/{id}")
    public ResponseEntity<Object> deleteSupplement(
            @PathVariable("id") Long id
    ) {
        supplementService.deleteSupplement(id);

        return ResponseEntity.noContent().build();
    }


    // Relations Requests
    @PostMapping(value = "/producten/supplementen/{id}/allergenen")
    public ResponseEntity<Object> addAllergenToSupplement(
            @PathVariable("id") Long id,
            @Valid @RequestBody IdInputDto inputDto
            ) {
        SupplementDto dto = supplementService.assignAllergenToSupplement(id, inputDto.getId());
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/producten/supplementen/{id}/allergenen")
    public ResponseEntity<Object> removeAllergenFromSupplement(
            @PathVariable("id") Long id,
            @Valid @RequestBody IdInputDto inputDto
    ) {
        SupplementDto dto = supplementService.removeAllergenFromSupplement(id, inputDto.getId());
        return ResponseEntity.ok().body(dto);
    }
}
