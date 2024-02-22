package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriId;
import jakarta.validation.Valid;
import nu.revitalized.revitalizedwebshop.dtos.input.DiscountInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.IdInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.DiscountDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.DiscountService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
public class DiscountController {
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    // CRUD Requests
    @GetMapping("/users/discounts")
    public ResponseEntity<List<DiscountDto>> getAllDiscounts() {
        List<DiscountDto> dtos = discountService.getAllDiscounts();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/users/discounts/{id}")
    public ResponseEntity<DiscountDto> getSpecificDiscount(
            @PathVariable("id") Long id
    ) {
        DiscountDto dto = discountService.getDiscountById(id);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/users/discounts/search")
    public ResponseEntity<List<DiscountDto>> getDiscountsByParam(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double value,
            @RequestParam(required = false) Double minValue,
            @RequestParam(required = false) Double maxValue
    ) {
        List<DiscountDto> dtos = discountService.getAllDiscountsByParam(
                name, value, minValue, maxValue
        );

        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("/users/discounts")
    public ResponseEntity<DiscountDto> createDiscount(
            @Valid
            @RequestBody DiscountInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            DiscountDto dto = discountService.createDiscount(inputDto);

            URI uri = buildUriId(dto);

            return ResponseEntity.created(uri).body(dto);
        }
    }

    @PutMapping("/users/discounts/{id}")
    public ResponseEntity<DiscountDto> updateDiscount(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody DiscountInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            DiscountDto dto = discountService.updateDiscount(id, inputDto);

            return ResponseEntity.ok().body(dto);
        }
    }

    @PatchMapping("/users/discounts/{id}")
    public ResponseEntity<DiscountDto> patchDiscount(
            @PathVariable("id") Long id,
            @RequestBody DiscountInputDto inputDto
    ) {
        DiscountDto dto = discountService.patchDiscount(id, inputDto);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/users/discounts/{id}")
    public ResponseEntity<Object> deleteDiscount(
            @PathVariable("id") Long id
    ) {
        String confirmation = discountService.deleteDiscount(id);

        return ResponseEntity.ok().body(confirmation);
    }

    // Relation - User Requests
    @PostMapping(value = "/users/{username}/discounts")
    public ResponseEntity<Object> assignDiscountToUser(
            @PathVariable("username") String username,
            @Valid
            @RequestBody IdInputDto inputDto
            ) {
        DiscountDto dto = discountService.assignDiscountToUser(username, inputDto);

        return ResponseEntity.ok().body(dto);
    }
}
