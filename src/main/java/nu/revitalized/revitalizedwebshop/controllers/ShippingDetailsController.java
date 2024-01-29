package nu.revitalized.revitalizedwebshop.controllers;

// Imports

import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUri;
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;

import jakarta.validation.Valid;
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShippingDetailsDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.ShippingDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ShippingDetailsController {
    private final ShippingDetailsService shippingDetailsService;

    public ShippingDetailsController(ShippingDetailsService shippingDetailsService) {
        this.shippingDetailsService = shippingDetailsService;
    }


    // CRUD Requests -- GET Requests
    @GetMapping("/shipping-details")
    public ResponseEntity<List<ShippingDetailsDto>> getAllShippingDetails() {
        List<ShippingDetailsDto> dtos = shippingDetailsService.getAllShippingDetails();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/shipping-details/{id}")
    public ResponseEntity<ShippingDetailsDto> getShippingDetailsById(
            @PathVariable("id") Long id
    ) {
        ShippingDetailsDto dto = shippingDetailsService.getShippingDetailsById(id);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/shipping-details/search")
    public ResponseEntity<List<ShippingDetailsDto>> getShippingDetailsByParam(
            @RequestParam(required = false) String detailsName,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String zipCode,
            @RequestParam(required = false) String street,
            @RequestParam(required = false) String houseNumber,
            @RequestParam(required = false) String email
    ) {
        List<ShippingDetailsDto> dtos = shippingDetailsService.getShippingDetailsByParam(
                detailsName, name, country, city, zipCode, street, houseNumber, email);

        return ResponseEntity.ok().body(dtos);
    }

    // CRUD Requests -- POST Requests
    @PostMapping("/shipping-details")
    public ResponseEntity<ShippingDetailsDto> createShippingDetails(
            @Valid
            @RequestBody ShippingDetailsInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            ShippingDetailsDto dto = shippingDetailsService.createShippingDetails(inputDto);

            URI uri = buildUri(dto);

            return ResponseEntity.created(uri).body(dto);
        }
    }

    // CRUD Requests -- PUT/PATCH Requests
    @PutMapping("/shipping-details/{id}")
    public ResponseEntity<ShippingDetailsDto> updateShippingDetails(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody ShippingDetailsInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            ShippingDetailsDto dto = shippingDetailsService.updateShippingDetails(id, inputDto);

            return ResponseEntity.ok().body(dto);
        }
    }

    @PatchMapping("/shipping-details/{id}")
    public ResponseEntity<ShippingDetailsDto> patchShippingDetails(
            @PathVariable("id") Long id,
            @RequestBody ShippingDetailsInputDto inputDto
    ) {
        ShippingDetailsDto dto = shippingDetailsService.patchShippingDetails(id, inputDto);

        return ResponseEntity.ok().body(dto);
    }

    // CRUD Requests -- DELETE Requests
    @DeleteMapping("/shipping-details/{id}")
    public ResponseEntity<Object> deleteShippingDetails(
            @PathVariable("id") Long id
    ) {
        shippingDetailsService.deleteShippingDetailsById(id);

        return ResponseEntity.noContent().build();
    }
}
