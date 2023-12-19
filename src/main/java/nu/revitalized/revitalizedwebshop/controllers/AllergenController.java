package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.output.AllergenDto;
import nu.revitalized.revitalizedwebshop.services.AllergenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AllergenController {
    private final AllergenService allergenService;

    public AllergenController(AllergenService allergenService) {
        this.allergenService = allergenService;
    }


    // CRUD Requests
    @GetMapping("/supplementen/allergenen")
    public ResponseEntity<List<AllergenDto>> getAllAllergens() {
        List<AllergenDto> dtos = allergenService.getAllAllergens();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/supplementen/allergenen/naam/{name}")
    public ResponseEntity<List<AllergenDto>> getAllAllergensByName(
            @PathVariable(value = "name", required = true) String name
    ) {
        List<AllergenDto> dtos = allergenService.getAllAllergensByName(name);

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/supplementen/allergenen/{id}")
    public ResponseEntity<AllergenDto> getAllergenById(
            @PathVariable(value = "id") Long id
    ) {
        AllergenDto dto = allergenService.getAllergenById(id);

        return ResponseEntity.ok().body(dto);
    }

}
