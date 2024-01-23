package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.services.GarmentService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GarmentController {
    private final GarmentService garmentService;

    public GarmentController(GarmentService garmentService) {
        this.garmentService = garmentService;
    }


    // CRUD Requests -- GET Requests
    // CRUD Requests -- POST Requests
    // CRUD Requests -- PUT/PATCH Requests
    // CRUD Requests -- DELETE Requests
    // Relations Requests
}
