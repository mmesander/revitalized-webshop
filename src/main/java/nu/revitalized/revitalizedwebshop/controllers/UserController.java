package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import jakarta.validation.Valid;
import nu.revitalized.revitalizedwebshop.dtos.input.*;
import nu.revitalized.revitalizedwebshop.dtos.output.*;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import static nu.revitalized.revitalizedwebshop.helpers.BuildConfirmation.buildPersonalConfirmation;
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriOrderNumber;
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriUsername;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ShippingDetailsService shippingDetailsService;
    private final ReviewService reviewService;
    private final DiscountService discountService;
    private final OrderService orderService;

    public UserController(
            UserService userService,
            ShippingDetailsService shippingDetailsService,
            ReviewService reviewService,
            DiscountService discountService,
            OrderService orderService
    ) {
        this.userService = userService;
        this.shippingDetailsService = shippingDetailsService;
        this.reviewService = reviewService;
        this.discountService = discountService;
        this.orderService = orderService;
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
                UserDto dto = userService.assignAuthorityToUser(username, authority.getAuthority().toUpperCase());

                return ResponseEntity.ok().body(dto);
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
                ShippingDetailsDto dto = shippingDetailsService.updateAuthUserShippingDetails(username, id, inputDto);

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
                ShippingDetailsDto dto = shippingDetailsService.patchAuthUserShippingDetails(username, id, inputDto);

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
            String confirmation = shippingDetailsService.deleteAuthUserShippingDetails(username, id);

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
                Object dto = reviewService.updateAuthUserReview(username, reviewId, inputDto);

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
                Object dto = reviewService.patchAuthUserReview(username, reviewId, inputDto);

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
            String confirmation = reviewService.deleteAuthUserReview(username, reviewId);

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
    @GetMapping("/auth/{username}/orders")
    public ResponseEntity<List<OrderDto>> getAllAuthUserOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            List<OrderDto> orders = orderService.getAllAuthUserOrders(username);

            return ResponseEntity.ok().body(orders);
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @GetMapping("/auth/{username}/orders/{orderNumber}")
    public ResponseEntity<OrderDto> getAuthUserOrderById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @PathVariable("orderNumber") Long orderNumber
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            OrderDto dto = orderService.getAuthUserOrderById(username, orderNumber);

            return ResponseEntity.ok().body(dto);
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @PostMapping("/auth/{username}/orders")
    public ResponseEntity<OrderDto> createAuthUserOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @Valid
            @RequestBody AuthUserOrderInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            if (bindingResult.hasFieldErrors()) {
                throw new InvalidInputException(handleBindingResultError(bindingResult));
            } else {
                OrderDto dto = userService.addAuthUserOrder(username, inputDto);

                URI uri = buildUriOrderNumber(dto);

                return ResponseEntity.created(uri).body(dto);
            }
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }
}