package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.output.GarmentDto;
import nu.revitalized.revitalizedwebshop.services.GarmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GarmentController {
    private final GarmentService garmentService;

    public GarmentController(GarmentService garmentService) {
        this.garmentService = garmentService;
    }


    // CRUD Requests -- GET Requests
    @GetMapping("/kleding")
    public ResponseEntity<List<GarmentDto>> getAllGarments() {
        List<GarmentDto> dtos = garmentService.getAllGarments();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/kleding/{id}")
    public ResponseEntity<GarmentDto> getGarmentById(
            @PathVariable("id") Long id
    ) {
        GarmentDto dto = garmentService.getGarmentById(id);

        return ResponseEntity.ok().body(dto);
    }



    // CRUD Requests -- POST Requests
    // CRUD Requests -- PUT/PATCH Requests
    // CRUD Requests -- DELETE Requests
    // Relations Requests
}
