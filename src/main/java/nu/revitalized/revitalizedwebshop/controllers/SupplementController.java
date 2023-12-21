package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.output.SupplementDto;
import nu.revitalized.revitalizedwebshop.services.SupplementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class SupplementController {
    private final SupplementService supplementService;

    public SupplementController(SupplementService supplementService) {
        this.supplementService = supplementService;
    }


    // Crud Requests
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


}
