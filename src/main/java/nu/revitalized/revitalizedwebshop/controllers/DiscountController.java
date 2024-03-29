package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import jakarta.validation.Valid;
import nu.revitalized.revitalizedwebshop.dtos.input.DiscountInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.DiscountPatchInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.IdInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.DiscountDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.DiscountService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriId;

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

    @GetMapping("/users/discounts-active")
    public ResponseEntity<List<DiscountDto>> getAllActiveDiscounts() {
        List<DiscountDto> dtos = discountService.getAllActiveDiscounts();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/users/discounts-inactive")
    public ResponseEntity<List<DiscountDto>> getAllInactiveDiscounts() {
        List<DiscountDto> dtos = discountService.getAllInactiveDiscounts();

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
            @Valid
            @RequestBody DiscountPatchInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            DiscountDto dto = discountService.patchDiscount(id, inputDto);

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping("/users/discounts/{id}")
    public ResponseEntity<Object> deleteDiscount(
            @PathVariable("id") Long id
    ) {
        String confirmation = discountService.deleteDiscount(id);

        return ResponseEntity.ok().body(confirmation);
    }

    // Relation - User Requests
    @PutMapping(value = "/users/{username}/discounts")
    public ResponseEntity<Object> assignUserToDiscount(
            @PathVariable("username") String username,
            @Valid
            @RequestBody IdInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            DiscountDto dto = discountService.assignUserToDiscount(username, inputDto.getId());

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping(value = "/users/{username}/discounts")
    public ResponseEntity<Object> removeUserFromDiscount(
            @PathVariable("username") String username,
            @Valid
            @RequestBody IdInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            String confirmation = discountService.removeUserFromDiscount(username, inputDto.getId());

            return ResponseEntity.ok().body(confirmation);
        }
    }
}