package nu.revitalized.revitalizedwebshop.controllers;

// Imports

import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;

import nu.revitalized.revitalizedwebshop.dtos.input.GarmentInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.GarmentDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SearchDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.GarmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class GarmentController {
    private final GarmentService garmentService;

    public GarmentController(GarmentService garmentService) {
        this.garmentService = garmentService;
    }


    // CRUD Requests -- GET Requests
    @GetMapping("/producten/kleding")
    public ResponseEntity<List<GarmentDto>> getAllGarments() {
        List<GarmentDto> dtos = garmentService.getAllGarments();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/producten/kleding/{id}")
    public ResponseEntity<GarmentDto> getGarmentById(
            @PathVariable("id") Long id
    ) {
        GarmentDto dto = garmentService.getGarmentById(id);

        return ResponseEntity.ok().body(dto);
    }



//    @GetMapping("/producten/kleding/zoeken")
//    public ResponseEntity<List<GarmentDto>> getGarmentsByParam(
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String brand,
//            @RequestParam(required = false) Double price,
//            @RequestParam(required = false) Double averageRating,
//            @RequestParam(required = false) String size,
//            @RequestParam(required = false) String color
//    ) {
//        SearchDto searchDto = new SearchDto();
//
//        searchDto.setName(name);
//        searchDto.setBrand(brand);
//        searchDto.setPrice(price);
//        searchDto.setAverageRating(averageRating);
//        searchDto.setSize(size);
//        searchDto.setColor(color);
//
//        List<GarmentDto> dtos = garmentService.getGarmentsByParam(searchDto);
//
//        return ResponseEntity.ok().body(dtos);
//    }

    // CRUD Requests -- POST Requests
    @PostMapping("/producten/kleding")
    public ResponseEntity<GarmentDto> createGarment(
            @Valid
            @RequestBody GarmentInputDto inputDto,
            BindingResult bindingResult
    ) {
        GarmentDto dto;

        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            dto = garmentService.createGarment(inputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + dto.getId()).toUriString());
            return ResponseEntity.created(uri).body(dto);
        }
    }

    // CRUD Requests -- PUT/PATCH Requests
    @PutMapping("/producten/kleding/{id}")
    public ResponseEntity<GarmentDto> updateGarment(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody GarmentInputDto inputDto,
            BindingResult bindingResult
    ) {
        GarmentDto dto;

        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            dto = garmentService.updateGarment(id, inputDto);
        }

        return ResponseEntity.ok().body(dto);
    }

    @PatchMapping("/producten/kleding/{id}")
    public ResponseEntity<GarmentDto> patchGarment(
            @PathVariable("id") Long id,
            @RequestBody GarmentInputDto inputDto
    ) {
        GarmentDto dto = garmentService.patchGarment(id, inputDto);

        return ResponseEntity.ok().body(dto);
    }

    // CRUD Requests -- DELETE Requests
    @DeleteMapping("/producten/kleding/{id}")
    public ResponseEntity<Object> deleteGarment(
            @PathVariable("id") Long id
    ) {
        garmentService.deleteGarment(id);

        return ResponseEntity.noContent().build();
    }

    // Relations Requests
}
