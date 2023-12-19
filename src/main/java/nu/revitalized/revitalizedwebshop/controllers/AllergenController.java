package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.services.AllergenService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AllergenController {
    private final AllergenService allergenService;

    public AllergenController(AllergenService allergenService) {
        this.allergenService = allergenService;
    }

    // Get Requests
}
