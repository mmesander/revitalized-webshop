package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.output.GarmentDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SearchDto;
import nu.revitalized.revitalizedwebshop.services.GarmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/kleding/zoeken")
    public ResponseEntity<List<GarmentDto>> getGarmentsByParam(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Double averageRating,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String color
    ) {
        SearchDto searchDto = new SearchDto();

        searchDto.setName(name);
        searchDto.setBrand(brand);
        searchDto.setPrice(price);
        searchDto.setAverageRating(averageRating);
        searchDto.setSize(size);
        searchDto.setColor(color);

        List<GarmentDto> dtos = garmentService.getGarmentsByParam(searchDto);

        return ResponseEntity.ok().body(dtos);
    }

    // CRUD Requests -- POST Requests
    // CRUD Requests -- PUT/PATCH Requests
    // CRUD Requests -- DELETE Requests
    // Relations Requests
}
