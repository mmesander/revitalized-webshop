package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.AllergenInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.AllergenDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.AllergenService;
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriId;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
public class AllergenController {
    private final AllergenService allergenService;

    public AllergenController(AllergenService allergenService) {
        this.allergenService = allergenService;
    }


    // CRUD Requests
    @GetMapping("/products/supplements/allergens")
    public ResponseEntity<List<AllergenDto>> getAllAllergens() {
        List<AllergenDto> dtos = allergenService.getAllAllergens();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/products/supplements/allergens/{id}")
    public ResponseEntity<AllergenDto> getAllergenById(
            @PathVariable(value = "id") Long id
    ) {
        AllergenDto dto = allergenService.getAllergenById(id);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/products/supplements/allergens/search/{name}")
    public ResponseEntity<List<AllergenDto>> getAllAllergensByName(
            @PathVariable(value = "name", required = true) String name
    ) {
        List<AllergenDto> dtos = allergenService.getAllAllergensByName(name);

        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("/products/supplements/allergens")
    public ResponseEntity<AllergenDto> createAllergen(
            @Valid
            @RequestBody AllergenInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            AllergenDto dto = allergenService.createAllergen(inputDto);

            URI uri = buildUriId(dto);

            return ResponseEntity.created(uri).body(dto);
        }
    }

    @PutMapping("/products/supplements/allergens/{id}")
    public ResponseEntity<AllergenDto> updateAllergen(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody AllergenInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            AllergenDto dto = allergenService.updateAllergen(id, inputDto);

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping("/products/supplements/allergens/{id}")
    public ResponseEntity<String> deleteAllergen(
            @PathVariable("id") Long id
    ) {
        String confirmation = allergenService.deleteAllergen(id);



        return ResponseEntity.ok().body(confirmation);
    }
}
