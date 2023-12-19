package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.output.AllergenDto;
import nu.revitalized.revitalizedwebshop.services.AllergenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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


}
