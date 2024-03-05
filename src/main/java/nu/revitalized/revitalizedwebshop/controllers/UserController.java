package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriUsername;
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import static nu.revitalized.revitalizedwebshop.helpers.BuildConfirmation.buildPersonalConfirmation;
import nu.revitalized.revitalizedwebshop.dtos.input.*;
import nu.revitalized.revitalizedwebshop.dtos.output.ShortDiscountDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ReviewDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShippingDetailsDto;
import nu.revitalized.revitalizedwebshop.dtos.output.UserDto;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.DiscountService;
import nu.revitalized.revitalizedwebshop.services.ReviewService;
import nu.revitalized.revitalizedwebshop.services.ShippingDetailsService;
import nu.revitalized.revitalizedwebshop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ShippingDetailsService shippingDetailsService;
    private final ReviewService reviewService;
    private final DiscountService discountService;

    public UserController(
            UserService userService,
            ShippingDetailsService shippingDetailsService,
            ReviewService reviewService,
            DiscountService discountService
    ) {
        this.userService = userService;
        this.shippingDetailsService = shippingDetailsService;
        this.reviewService = reviewService;
        this.discountService = discountService;
    }

    // ADMIN -- CRUD Requests
    @GetMapping(value = "")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> dtos = userService.getUsers();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable("username") String username
    ) {
        UserDto dto = userService.getUser(username);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<UserDto>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        List<UserDto> dtos = userService.getUsersByParam(username, email);

        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("")
    public ResponseEntity<UserDto> createUser(
            @Valid
            @RequestBody UserInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            UserDto dto = userService.createUser(inputDto);

            URI uri = buildUriUsername(dto);

            return ResponseEntity.created(uri).body(dto);
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateUserEmail(
            @PathVariable("username") String username,
            @Valid
            @RequestBody UserEmailInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            UserDto dto = userService.updateUserEmail(username, inputDto);

            return ResponseEntity.ok().body(dto);
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(
            @PathVariable("username") String username
    ) {
        String confirmation = userService.deleteUser(username);

        return ResponseEntity.ok().body(confirmation);
    }

    // ADMIN - Authority Requests
    @GetMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> getUserAuthorities(
            @PathVariable("username") String username
    ) {
        return ResponseEntity.ok().body(userService.getUserAuthorities(username));
    }

    @PutMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> assignAuthorityToUser(
            @PathVariable("username") String username,
            @Valid
            @RequestBody AuthorityInputDto authority,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            try {
                userService.assignAuthorityToUser(username, authority.getAuthority().toUpperCase());

                return ResponseEntity.ok().body(userService.getUser(username));
            } catch (Exception exception) {
                throw new BadRequestException(exception.getMessage());
            }
        }
    }

    @DeleteMapping(value = "/{username}/authorities/{authority}")
    public ResponseEntity<Object> removeAuthorityFromUser(
            @PathVariable("username") String username,
            @PathVariable("authority") String authority
    ) {
        String confirmation = userService.removeAuthorityFromUser(username, authority);

        return ResponseEntity.ok().body(confirmation);
    }

    // ADMIN - Discount Requests
    @GetMapping(value = "/{username}/discounts-all")
    public ResponseEntity<Object> getAllDiscountsFromUser(
            @PathVariable("username") String username
    ) {
        Set<ShortDiscountDto> discounts = userService.getAllDiscountsFromUser(username);

        return ResponseEntity.ok().body(discounts);
    }

    @DeleteMapping(value = "/{username}/discounts-all")
    public ResponseEntity<Object> removeAllDiscountsFromUser(
            @PathVariable("username") String username
    ) {
        String confirmation = userService.removeAllDiscountsFromUser(username);

        return ResponseEntity.ok().body(confirmation);
    }

    // USER (Authenticated) - CRUD Requests
    @GetMapping(value = "/auth/{username}")
    public ResponseEntity<UserDto> getAuthUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            UserDto userDto = userService.getUser(username);

            return ResponseEntity.ok(userDto);
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @PutMapping("/auth/{username}/update-email")
    public ResponseEntity<UserDto> updateAuthUserEmail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @Valid
            @RequestBody UserEmailInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            if (bindingResult.hasFieldErrors()) {
                throw new InvalidInputException(handleBindingResultError(bindingResult));
            } else {
                UserDto dto = userService.updateUserEmail(username, inputDto);

                return ResponseEntity.ok().body(dto);
            }
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    // USER (Authenticated) - ShippingDetails Requests
    @GetMapping("/auth/{username}/shipping-details")
    public ResponseEntity<List<ShippingDetailsDto>> getAllAuthUserShippingDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            List<ShippingDetailsDto> dtos = shippingDetailsService.getAllAuthUserShippingDetails(username);

            return ResponseEntity.ok().body(dtos);
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @GetMapping("/auth/{username}/shipping-details/{id}")
    public ResponseEntity<ShippingDetailsDto> getAuthUserShippingDetailsById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @PathVariable("id") Long id
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            ShippingDetailsDto dto = shippingDetailsService.getAuthUserShippingDetailsById(username, id);

            return ResponseEntity.ok().body(dto);
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @PostMapping("/auth/{username}/shipping-details")
    public ResponseEntity<UserDto> createNewAuthUserShippingDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @Valid
            @RequestBody ShippingDetailsInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            if (bindingResult.hasFieldErrors()) {
                throw new InvalidInputException(handleBindingResultError(bindingResult));
            } else {
                UserDto dto = userService.addAuthUserShippingDetails(username, inputDto);

                return ResponseEntity.ok().body(dto);
            }
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @PutMapping("/auth/{username}/shipping-details/{id}")
    public ResponseEntity<ShippingDetailsDto> updateAuthUserShippingDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @PathVariable("id") Long id,
            @Valid
            @RequestBody ShippingDetailsInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            if (bindingResult.hasFieldErrors()) {
                throw new InvalidInputException(handleBindingResultError(bindingResult));
            } else {
                ShippingDetailsDto dto = shippingDetailsService.updateShippingDetails(id, inputDto);

                return ResponseEntity.ok().body(dto);
            }
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @PatchMapping("/auth/{username}/shipping-details/{id}")
    public ResponseEntity<ShippingDetailsDto> patchAuthUserShippingDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @PathVariable("id") Long id,
            @Valid
            @RequestBody ShippingDetailsPatchInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            if (bindingResult.hasFieldErrors()) {
                throw new InvalidInputException(handleBindingResultError(bindingResult));
            } else {
                ShippingDetailsDto dto = shippingDetailsService.patchShippingDetails(id, inputDto);

                return ResponseEntity.ok().body(dto);
            }
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @DeleteMapping("/auth/{username}/shipping-details/{id}")
    public ResponseEntity<Object> deleteAuthUserShippingDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @PathVariable("id") Long id
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            String confirmation = shippingDetailsService.deleteShippingDetailsById(id);

            return ResponseEntity.ok().body(buildPersonalConfirmation(confirmation, "user", username));
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    // USER (Authenticated) - Review Requests
    @GetMapping("/auth/{username}/reviews")
    public ResponseEntity<Object> getAllAuthUserReviews(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            List<ReviewDto> dtos = reviewService.getAllAuthUserReviews(username);

            return ResponseEntity.ok().body(dtos);
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @GetMapping("/auth/{username}/reviews/{reviewId}")
    public ResponseEntity<Object> getAuthUserReviewById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @PathVariable("reviewId") Long reviewId
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            ReviewDto dto = reviewService.getAuthUserReviewById(username, reviewId);

            return ResponseEntity.ok().body(dto);
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @PostMapping("/auth/{username}/products/{productId}/reviews")
    public ResponseEntity<Object> createNewAuthUserProductReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @PathVariable("productId") Long productId,
            @Valid
            @RequestBody ReviewInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            if (bindingResult.hasFieldErrors()) {
                throw new InvalidInputException(handleBindingResultError(bindingResult));
            } else {
                Object dto = userService.addAuthUserProductReview(username, inputDto, productId);

                return ResponseEntity.ok().body(dto);
            }
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @PutMapping("/auth/{username}/reviews/{reviewId}")
    public ResponseEntity<Object> updateAuthUserProductReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @PathVariable("reviewId") Long reviewId,
            @Valid
            @RequestBody ReviewInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            if (bindingResult.hasFieldErrors()) {
                throw new InvalidInputException(handleBindingResultError(bindingResult));
            } else {
                Object dto = reviewService.updateReview(reviewId, inputDto);

                return ResponseEntity.ok().body(dto);
            }
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @PatchMapping("/auth/{username}/reviews/{reviewId}")
    public ResponseEntity<Object> patchAuthUserProductReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @PathVariable("reviewId") Long reviewId,
            @Valid
            @RequestBody ReviewPatchInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            if (bindingResult.hasFieldErrors()) {
                throw new InvalidInputException(handleBindingResultError(bindingResult));
            } else {
                Object dto = reviewService.patchReview(reviewId, inputDto);

                return ResponseEntity.ok().body(dto);
            }
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @DeleteMapping("/auth/{username}/reviews/{reviewId}")
    public ResponseEntity<Object> deleteAuthUserProductReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @PathVariable("reviewId") Long reviewId
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            String confirmation = reviewService.deleteReview(reviewId);

            return ResponseEntity.ok().body(buildPersonalConfirmation(confirmation, "user", username));
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    // USER (Authenticated) - Discount Requests
    @GetMapping("/auth/{username}/discounts")
    public ResponseEntity<List<String>> getAllAuthUserDiscounts(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            List<String> discounts = discountService.getAllAuthUserDiscounts(username);

            return ResponseEntity.ok().body(discounts);
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    // USER (Authenticated) - Order Requests

}
