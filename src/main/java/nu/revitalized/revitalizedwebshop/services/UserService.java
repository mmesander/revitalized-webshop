package nu.revitalized.revitalizedwebshop.services;

// Imports

import static nu.revitalized.revitalizedwebshop.security.config.SpringSecurityConfig.passwordEncoder;
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.services.ShippingDetailsService.dtoToShippingDetails;
import static nu.revitalized.revitalizedwebshop.services.ShippingDetailsService.shippingDetailsToDto;
import static nu.revitalized.revitalizedwebshop.specifications.UserSpecification.*;

import nu.revitalized.revitalizedwebshop.dtos.input.UserEmailInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.UserInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShippingDetailsDto;
import nu.revitalized.revitalizedwebshop.dtos.output.UserDto;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.exceptions.UsernameNotFoundException;
import nu.revitalized.revitalizedwebshop.models.ShippingDetails;
import nu.revitalized.revitalizedwebshop.models.User;
import nu.revitalized.revitalizedwebshop.repositories.AuthorityRepository;
import nu.revitalized.revitalizedwebshop.repositories.UserRepository;
import nu.revitalized.revitalizedwebshop.models.Authority;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public UserService(
            UserRepository userRepository,
            AuthorityRepository authorityRepository
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }


    // Transfer Methods
    public static User dtoToUser(UserInputDto inputDto) {
        User user = new User();

        user.setUsername(inputDto.getUsername().toLowerCase());
        user.setPassword(passwordEncoder().encode(inputDto.getPassword()));
        user.setEmail(inputDto.getEmail());

        return user;
    }

    public static UserDto userToDto(User user) {
        UserDto userDto = new UserDto();

        copyProperties(user, userDto);

        if (user.getShippingDetails() != null) {
            Set<ShippingDetailsDto> dtos = new HashSet<>();
            for (ShippingDetails shippingDetails : user.getShippingDetails()) {
                dtos.add(shippingDetailsToDto(shippingDetails));
            }
            userDto.setShippingDetails(dtos);
        }

        return userDto;
    }


    // CRUD Methods --> GET Methods
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = userToDto(user);
            userDtos.add(userDto);
        }

        if (userDtos.isEmpty()) {
            throw new RecordNotFoundException("No users found");
        } else {
            userDtos.sort(Comparator.comparing(UserDto::getUsername));
            return userDtos;
        }
    }

    public UserDto getUser(String username) {
        Optional<User> user = userRepository.findById(username);

        if (user.isPresent()) {
            return userToDto(user.get());
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public List<UserDto> getUsersByParam(
            String username,
            String email
    ) {
        Specification<User> params = Specification.where
                        (StringUtils.isBlank(username) ? null : getUserUsernameLikeFilter(username))
                .and(StringUtils.isBlank(email) ? null : getUserEmailLike(email));

        List<User> filteredUsers = userRepository.findAll(params);
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : filteredUsers) {
            UserDto userDto = userToDto(user);
            userDtos.add(userDto);
        }

        if (userDtos.isEmpty()) {
            throw new RecordNotFoundException("No users found with the specified filters");
        } else {
            return userDtos;
        }
    }

    // CRUD Methods --> POST Methods
    public UserDto createUser(UserInputDto inputDto) {
        User user = dtoToUser(inputDto);

        boolean usernameExists = userRepository.existsByUsernameIgnoreCase(inputDto.getUsername());
        boolean emailExists = userRepository.existsByEmailIgnoreCase(inputDto.getEmail());

        if (usernameExists && emailExists) {
            throw new InvalidInputException("Username: " + inputDto.getUsername().toLowerCase() + " and email: "
                    + inputDto.getEmail().toLowerCase() + " are already in use");
        } else if (usernameExists) {
            throw new InvalidInputException("Username: " + inputDto.getUsername().toLowerCase()
                    + " is already in use");
        } else if (emailExists) {
            throw new InvalidInputException("Email: " + inputDto.getEmail().toLowerCase()
                    + " is already in use");
        } else {

            userRepository.save(user);

            user.addAuthority(new Authority(user.getUsername(), "ROLE_USER"));

            userRepository.save(user);

            return userToDto(user);
        }
    }

    // CRUD Methods --> PUT/PATCH Methods
    public UserDto updateUserEmail(String username, UserEmailInputDto inputDto) {
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();


            user.setEmail(inputDto.getEmail());

            User updatedUser = userRepository.save(user);

            return userToDto(updatedUser);
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    // CRUD Methods --> DELETE Methods
    public String deleteUser(String username) {
        Optional<User> user = userRepository.findById(username);

        if (user.isPresent()) {
            if (user.get().getUsername().equalsIgnoreCase("mmesander")) {
                throw new BadRequestException("Can't remove user: " + user.get().getUsername());
            } else {
                userRepository.deleteById(username);

                return "User: " + username + " is deleted";
            }
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    // Relations Methods
    public Set<Authority> getUserAuthorities(String username) {
        Optional<User> user = userRepository.findById(username);

        if (user.isPresent()) {
            UserDto userDto = userToDto(user.get());

            return userDto.getAuthorities();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public UserDto addAuthority(String username, String authority) {
        Optional<User> user = userRepository.findById(username);
        Optional<Authority> optionalAuthority = authorityRepository.findAuthoritiesByAuthorityContainsIgnoreCase(authority);


        if (user.isPresent() && optionalAuthority.isPresent()) {
            if (user.get().getUsername().equalsIgnoreCase("rplooij")
                    && authority.equalsIgnoreCase("ROLE_ADMIN")
            ) {
                throw new BadRequestException("User: " + user.get().getUsername() + " should not have admin rights!");
            } else {
                user.get().addAuthority(new Authority(username, authority));

                userRepository.save(user.get());

                return userToDto(user.get());
            }
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public String removeAuthority(String username, String authority) {
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getUsername().equalsIgnoreCase("mmesander") &&
                    authority.equalsIgnoreCase("ROLE_ADMIN")) {

                return "Forbidden to remove admin rights from user: " + user.getUsername()
                        + ", to remove please contact developer";

            } else if (user.getUsername().equalsIgnoreCase("mmesander") &&
                    authority.equalsIgnoreCase("ROLE_USER")) {

                return "Forbidden to remove user rights from user: " + user.getUsername()
                        + ", to remove please contact developer";

            } else {
                Authority toRemove = user.getAuthorities().stream()
                        .filter(a -> a.getAuthority().equalsIgnoreCase(authority))
                        .findFirst()
                        .orElseThrow(() -> new InvalidInputException("User does not have authority: " + authority));

                user.removeAuthority(toRemove);
                userRepository.save(user);

                return "Authority: " + authority + " is removed from user: " + username;
            }
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
