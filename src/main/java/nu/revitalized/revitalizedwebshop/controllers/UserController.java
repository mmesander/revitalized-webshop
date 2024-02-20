package nu.revitalized.revitalizedwebshop.controllers;

// Imports

import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriUsername;
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import static nu.revitalized.revitalizedwebshop.helpers.BuildConfirmation.buildPersonalConfirmation;

import jakarta.validation.Valid;
import nu.revitalized.revitalizedwebshop.dtos.input.*;
import nu.revitalized.revitalizedwebshop.dtos.output.ReviewDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShippingDetailsDto;
import nu.revitalized.revitalizedwebshop.dtos.output.UserDto;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.ReviewService;
import nu.revitalized.revitalizedwebshop.services.ShippingDetailsService;
import nu.revitalized.revitalizedwebshop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ShippingDetailsService shippingDetailsService;
    private final ReviewService reviewService;

    public UserController(
            UserService userService,
            ShippingDetailsService shippingDetailsService,
            ReviewService reviewService
    ) {
        this.userService = userService;
        this.shippingDetailsService = shippingDetailsService;
        this.reviewService = reviewService;
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


    // ADMIN - Authorities Requests
    @GetMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> getUserAuthorities(
            @PathVariable("username") String username
    ) {
        return ResponseEntity.ok().body(userService.getUserAuthorities(username));
    }

    @PostMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> addUserAuthority(
            @PathVariable("username") String username,
            @Valid
            @RequestBody AuthorityInputDto authority,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            try {
                userService.addAuthority(username, authority.getAuthority().toUpperCase());

                return ResponseEntity.ok().body(userService.getUser(username));
            } catch (Exception exception) {
                throw new BadRequestException(exception.getMessage());
            }
        }
    }

    @DeleteMapping(value = "/{username}/authorities/{authority}")
    public ResponseEntity<Object> deleteUserAuthority(
            @PathVariable("username") String username,
            @PathVariable("authority") String authority
    ) {
        String confirmation = userService.removeAuthority(username, authority);

        return ResponseEntity.ok().body(confirmation);
    }


    // ADMIN - ShippingDetails Requests
    @PutMapping(value = "/{username}/shipping-details")
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
                userService.assignShippingDetailsToUser(username, idInputDto.getId());

                return ResponseEntity.ok().body(userService.getUser(username));
            } catch (Exception exception) {
                throw new BadRequestException(exception.getMessage());
            }
        }
    }


    // USER - CRUD Requests
    @GetMapping(value = "/auth/{username}")
    public ResponseEntity<UserDto> getSpecificUser(
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
    public ResponseEntity<UserDto> updateSpecificUserEmail(
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


    // USER - ShippingDetails Requests
    @PostMapping("/auth/{username}/shipping-details")
    public ResponseEntity<UserDto> createNewUserShippingDetails(
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
                UserDto dto = userService.addUserShippingDetails(username, inputDto);

                return ResponseEntity.ok().body(dto);
            }
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @PutMapping("/auth/{username}/shipping-details/{id}")
    public ResponseEntity<ShippingDetailsDto> updateUserShippingDetails(
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
    public ResponseEntity<ShippingDetailsDto> patchUserShippingDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @PathVariable("id") Long id,
            @RequestBody ShippingDetailsInputDto inputDto,
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
    public ResponseEntity<Object> deleteUserShippingDetails(
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


    // USER - Review Requests
    @GetMapping("/auth/{username}/reviews")
    public ResponseEntity<Object> getAllPersonalUserReviews(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username
    ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            List<ReviewDto> dtos = reviewService.getAllPersonalReviews(username);

            return ResponseEntity.ok().body(dtos);
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @PostMapping("/auth/{username}/products/{productId}/reviews")
    public ResponseEntity<Object> createNewUserProductReview(
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
                Object dto = userService.addUserProductReview(username, inputDto, productId);

                return ResponseEntity.ok().body(dto);
            }
        } else {
            throw new BadRequestException("Used token is not valid");
        }
    }

    @PutMapping("/auth/{username}/reviews/{reviewId}")
    public ResponseEntity<Object> updateUserProductReview(
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
    public ResponseEntity<Object> patchUserProductReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("username") String username,
            @PathVariable("reviewId") Long reviewId,
            @RequestBody ReviewInputDto inputDto,
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
    public ResponseEntity<Object> deleteUserProductReview(
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
}
