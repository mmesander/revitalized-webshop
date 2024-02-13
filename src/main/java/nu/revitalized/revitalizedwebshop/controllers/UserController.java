package nu.revitalized.revitalizedwebshop.controllers;

// Imports

import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUriUsername;
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;

import jakarta.validation.Valid;
import nu.revitalized.revitalizedwebshop.dtos.input.AuthorityInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.UserInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.UserDto;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // CRUD Requests -- GET Requests
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

    // CRUD Requests -- POST Requests
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

    // CRUD Requests -- PUT/PATCH Requests
    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("username") String username,
            @RequestBody UserInputDto inputDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasFieldErrors()) {
            throw new InvalidInputException(handleBindingResultError(bindingResult));
        } else {
            UserDto dto = userService.updateUser(username, inputDto);

            return ResponseEntity.ok().body(dto);
        }
    }

    // CRUD Requests -- DELETE Requests
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(
            @PathVariable("username") String username
    ) {
        String confirmation = userService.deleteUser(username);

        return ResponseEntity.ok().body(confirmation);
    }


    // Relations Requests
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


    // User Requests
    @GetMapping(value = "/user/{username}")
    public ResponseEntity<UserDto> getUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String username
            ) {
        if (Objects.equals(userDetails.getUsername(), username)) {
            UserDto userDto = userService.getUser(username);

            return ResponseEntity.ok(userDto);
        } else {
            throw new BadRequestException("Invalid token used");
        }
    }
}
