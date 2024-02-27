package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriId;
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;

import nu.revitalized.revitalizedwebshop.dtos.input.IdInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsPatchInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShippingDetailsDto;
import nu.revitalized.revitalizedwebshop.dtos.output.UserDto;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.ShippingDetailsService;
import nu.revitalized.revitalizedwebshop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class ShippingDetailsController {
    private final ShippingDetailsService shippingDetailsService;
    private final UserService userService;

    public ShippingDetailsController(
            ShippingDetailsService shippingDetailsService,
            UserService userService
    ) {
        this.shippingDetailsService = shippingDetailsService;
        this.userService = userService;
    }

    // CRUD Requests
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

    @PostMapping("/shipping-details")
    public ResponseEntity<ShippingDetailsDto> createShippingDetails(
            @Valid
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ShippingDetailsInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            ShippingDetailsDto dto = shippingDetailsService.createShippingDetails(inputDto, userDetails.getUsername());

            URI uri = buildUriId(dto);

            return ResponseEntity.created(uri).body(dto);
        }
    }

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
            @Valid
            @RequestBody ShippingDetailsPatchInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            ShippingDetailsDto dto = shippingDetailsService.patchShippingDetails(id, inputDto);

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping("/shipping-details/{id}")
    public ResponseEntity<Object> deleteShippingDetails(
            @PathVariable("id") Long id
    ) {
        String confirmation = shippingDetailsService.deleteShippingDetailsById(id);

        return ResponseEntity.ok().body(confirmation);
    }

    // Relation - User Requests
    @PutMapping(value = "users/{username}/shipping-details")
    public ResponseEntity<Object> assignShippingDetailsToUser(
            @PathVariable("username") String username,
            @Valid
            @RequestBody IdInputDto idInputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            try {
                UserDto dto = shippingDetailsService.assignShippingDetailsToUser(username, idInputDto.getId());

                return ResponseEntity.ok().body(dto);
            } catch (Exception exception) {
                throw new BadRequestException(exception.getMessage());
            }
        }
    }
}
