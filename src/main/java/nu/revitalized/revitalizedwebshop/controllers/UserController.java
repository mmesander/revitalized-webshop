package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.services.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // CRUD Requests -- GET Requests
    // CRUD Requests -- POST Requests
    // CRUD Requests -- PUT/PATCH Requests
    // CRUD Requests -- DELETE Requests
    // Relations Requests
}
