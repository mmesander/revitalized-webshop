package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.AllergenInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.AllergenDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.AllergenService;
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUri;
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
    @GetMapping("/producten/supplementen/allergenen")
    public ResponseEntity<List<AllergenDto>> getAllAllergens() {
        List<AllergenDto> dtos = allergenService.getAllAllergens();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/producten/supplementen/allergenen/{id}")
    public ResponseEntity<AllergenDto> getAllergenById(
            @PathVariable(value = "id") Long id
    ) {
        AllergenDto dto = allergenService.getAllergenById(id);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/producten/supplementen/allergenen/zoeken/{name}")
    public ResponseEntity<List<AllergenDto>> getAllAllergensByName(
            @PathVariable(value = "name", required = true) String name
    ) {
        List<AllergenDto> dtos = allergenService.getAllAllergensByName(name);

        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("/producten/supplementen/allergenen")
    public ResponseEntity<AllergenDto> createAllergen(
            @Valid
            @RequestBody AllergenInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            AllergenDto dto = allergenService.createAllergen(inputDto);

            URI uri = buildUri(dto);

            return ResponseEntity.created(uri).body(dto);
        }
    }

    @PutMapping("/producten/supplementen/allergenen/{id}")
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

    @DeleteMapping("/producten/supplementen/allergenen/{id}")
    public ResponseEntity<Object> deleteAllergen(
            @PathVariable("id") Long id
    ) {
        allergenService.deleteAllergen(id);

        return ResponseEntity.noContent().build();
    }
}
