package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import jakarta.validation.Valid;
import nu.revitalized.revitalizedwebshop.dtos.input.SupplementInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SupplementDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.SupplementService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
public class SupplementController {
    private final SupplementService supplementService;

    public SupplementController(SupplementService supplementService) {
        this.supplementService = supplementService;
    }


    // CRUD Requests
    @GetMapping("/supplementen")
    public ResponseEntity<List<SupplementDto>> getAllSupplements() {
        List<SupplementDto> dtos = supplementService.getAllSupplements();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/supplementen/{id}")
    public ResponseEntity<SupplementDto> getSupplementById(
            @PathVariable(value = "id") Long id
    ) {
        SupplementDto dto = supplementService.getSupplementById(id);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/supplementen/zoeken")
    public ResponseEntity<List<SupplementDto>> getSupplementsByBrandAndOrName(
            @RequestParam(value = "brand", required = false) Optional<String> brand,
            @RequestParam(value = "name", required = false) Optional<String> name
    ) {
        List<SupplementDto> dtos;

        if (brand.isPresent() && name.isPresent()) {
            dtos = supplementService.getSupplementsByBrandAndName(brand.get(), name.get());
        } else if (brand.isPresent()) {
            dtos = supplementService.getSupplementsByBrand(brand.get());
        } else if (name.isPresent()) {
            dtos = supplementService.getSupplementsByName(name.get());
        } else {
            dtos = supplementService.getAllSupplements();
        }

        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("/supplementen")
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

    @PutMapping("/supplementen/{id}")
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

    @PatchMapping("/supplementen/{id}")
    public ResponseEntity<SupplementDto> patchSupplement(
            @PathVariable("id") Long id,
            @RequestBody SupplementInputDto inputDto
    ) {
        SupplementDto dto = supplementService.patchSupplement(id, inputDto);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/supplementen/{id}")
    public ResponseEntity<Object> deleteSupplement(
            @PathVariable("id") Long id
    ) {
        supplementService.deleteSupplement(id);

        return ResponseEntity.noContent().build();
    }
}
