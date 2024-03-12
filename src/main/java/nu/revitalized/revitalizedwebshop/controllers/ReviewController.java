package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import jakarta.validation.Valid;
import nu.revitalized.revitalizedwebshop.dtos.input.IdInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.ReviewInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.ReviewPatchInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ReviewDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriId;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // CRUD Requests
    @GetMapping("/products/reviews")
    public ResponseEntity<List<ReviewDto>> getAllProductReviews() {
        List<ReviewDto> dtos = reviewService.getAllReviews();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/products/reviews/{id}")
    public ResponseEntity<ReviewDto> getReviewById(
            @PathVariable("id") Long id
    ) {
        ReviewDto dto = reviewService.getReview(id);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/products/reviews/search")
    public ResponseEntity<List<ReviewDto>> getReviewsByParam(
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating
    ) {
        List<ReviewDto> dtos = reviewService.getReviewsByParam(
                minRating, maxRating
        );

        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("/products/reviews")
    public ResponseEntity<ReviewDto> createReview(
            @Valid
            @RequestBody ReviewInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            ReviewDto dto = reviewService.createReview(inputDto);

            URI uri = buildUriId(dto);

            return ResponseEntity.created(uri).body(dto);
        }
    }

    @PutMapping("/products/reviews/{id}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody ReviewInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            ReviewDto dto = reviewService.updateReview(id, inputDto);

            return ResponseEntity.ok().body(dto);
        }
    }

    @PatchMapping("/products/reviews/{id}")
    public ResponseEntity<ReviewDto> patchReview(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody ReviewPatchInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            ReviewDto dto = reviewService.patchReview(id, inputDto);

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping("/products/reviews/{id}")
    public ResponseEntity<Object> deleteReview(
            @PathVariable("id") Long id
    ) {
        String confirmation = reviewService.deleteReview(id);

        return ResponseEntity.ok().body(confirmation);
    }

    // Relation - Product Requests
    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<ReviewDto>> getAllReviewsFromProduct(
            @PathVariable("productId") Long productId
    ) {
        List<ReviewDto> dtos = reviewService.getAllReviewsFromProduct(productId);

        return ResponseEntity.ok().body(dtos);
    }

    @PutMapping("/products/{productId}/reviews")
    public ResponseEntity<Object> assignReviewToProduct(
            @PathVariable("productId") Long productId,
            @Valid
            @RequestBody IdInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            Object dto = reviewService.assignReviewToProduct(productId, inputDto.getId());

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping("/products/{productId}/reviews")
    public ResponseEntity<Object> removeReviewFromProduct(
            @PathVariable("productId") Long productId,
            @Valid
            @RequestBody IdInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            Object dto = reviewService.removeReviewFromProduct(productId, inputDto.getId());

            return ResponseEntity.ok().body(dto);
        }
    }
}