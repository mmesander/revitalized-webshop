package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.UriBuilder.buildUri;
import static nu.revitalized.revitalizedwebshop.helpers.BindingResultHelper.handleBindingResultError;
import jakarta.validation.Valid;
import nu.revitalized.revitalizedwebshop.dtos.input.UserInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.UserDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

            URI uri = buildUri(dto);
        }
    }

    // CRUD Requests -- PUT/PATCH Requests


    // CRUD Requests -- DELETE Requests



    // Relations Requests

}
