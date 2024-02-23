package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.security.config.SpringSecurityConfig.passwordEncoder;
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.BuildHouseNumber.buildHouseNumber;
import static nu.revitalized.revitalizedwebshop.services.ReviewService.*;
import static nu.revitalized.revitalizedwebshop.services.ShippingDetailsService.*;
import static nu.revitalized.revitalizedwebshop.specifications.UserSpecification.*;
import nu.revitalized.revitalizedwebshop.dtos.input.ReviewInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.UserEmailInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.UserInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.*;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.exceptions.UsernameNotFoundException;
import nu.revitalized.revitalizedwebshop.models.*;
import nu.revitalized.revitalizedwebshop.repositories.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final ShippingDetailsRepository shippingDetailsRepository;
    private final ShippingDetailsService shippingDetailsService;
    private final SupplementRepository supplementRepository;
    private final GarmentRepository garmentRepository;
    private final ReviewService reviewService;
    private final DiscountService discountService;

    public UserService(
            UserRepository userRepository,
            AuthorityRepository authorityRepository,
            ShippingDetailsRepository shippingDetailsRepository,
            ShippingDetailsService shippingDetailsService,
            SupplementRepository supplementRepository,
            GarmentRepository garmentRepository,
            ReviewService reviewService,
            DiscountService discountService
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.shippingDetailsRepository = shippingDetailsRepository;
        this.shippingDetailsService = shippingDetailsService;
        this.supplementRepository = supplementRepository;
        this.garmentRepository = garmentRepository;
        this.reviewService = reviewService;
        this.discountService = discountService;
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
            Set<ShippingDetailsShortDto> dtos = new TreeSet<>(Comparator.comparingLong(ShippingDetailsShortDto::getId));
            for (ShippingDetails shippingDetails : user.getShippingDetails()) {
                dtos.add(shippingDetailsToShortDto(shippingDetails));
            }
            userDto.setShippingDetails(dtos);
        }

        if (user.getReviews() != null) {
            Set<ReviewDto> dtos = new TreeSet<>(Comparator.comparing(ReviewDto::getDate).reversed());
            for (Review review : user.getReviews()) {
                dtos.add(reviewToDto(review));
            }
            userDto.setReviews(dtos);
        }

        if (user.getDiscounts() != null) {
            Set<String> discounts = new HashSet<>();

            for (Discount discount : user.getDiscounts()) {
                String discountName = discount.getName();
                discounts.add(discountName);
            }
            userDto.setDiscounts(discounts);
        }

        return userDto;
    }

    public static UserShortDto userToShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();

        copyProperties(user, userShortDto);

        return userShortDto;
    }

    // CRUD Methods
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

    // Relation - Authorities Methods
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

    // Relation - Discount Methods
    public Set<String> getAllUserDiscounts(String username) {
        Optional<User> user = userRepository.findById(username);

        if (user.isPresent()) {
            UserDto userDto = userToDto(user.get());

            if (userDto.getDiscounts().isEmpty()) {
                throw new RecordNotFoundException("No discounts found for user: " + username);
            }

            return userDto.getDiscounts();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public String removeAllUserDiscounts(String username) {
        Optional<User> user = userRepository.findById(username);

        if (user.isPresent()) {
            Set<Discount> discounts = user.get().getDiscounts();

            if (discounts.isEmpty()) {
                throw new RecordNotFoundException("No discounts found for user: " + username);
            }

            for (Discount discount : discounts) {
                discountService.removeDiscountFromUser(username, discount.getId());
            }
            return "All discounts from user: " + username + " are removed";
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    // Relation - Authenticated User Methods
    public UserDto addAuthUserShippingDetails(String username, ShippingDetailsInputDto inputDto) {
        Optional<User> user = userRepository.findById(username);
        UserDto dto;

        if (user.isPresent()) {
            shippingDetailsService.createShippingDetails(inputDto, username);

            String houseNumber = buildHouseNumber(inputDto);
            ShippingDetails presentShippingDetails = null;

            Optional<List<ShippingDetails>> optionalListOfShippingDetails =
                    shippingDetailsRepository.findByStreetIgnoreCaseAndHouseNumber(
                            inputDto.getStreet(), houseNumber);

            if (optionalListOfShippingDetails.isPresent()) {
                for (ShippingDetails shippingDetails : optionalListOfShippingDetails.get()) {
                    if (shippingDetails.getUser() == null) {
                        presentShippingDetails = shippingDetails;
                    }
                }
            }

            if (presentShippingDetails != null) {
                shippingDetailsService.assignShippingDetailsToUser(username, presentShippingDetails.getId());
            } else {
                throw new BadRequestException("Shipping details with address: " + inputDto.getStreet() + houseNumber
                        + " is not found");
            }

            dto = userToDto(user.get());

        } else {
            throw new UsernameNotFoundException(username);
        }

        return dto;
    }

    public Object addAuthUserProductReview(String username, ReviewInputDto inputDto, Long productId) {
        Optional<User> optionalUser = userRepository.findById(username);
        Optional<Supplement> optionalSupplement = supplementRepository.findById(productId);
        Optional<Garment> optionalGarment = garmentRepository.findById(productId);
        ReviewDto createdReview;
        Object dto;

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        boolean exists = false;
        Long existingId = null;

        if (optionalSupplement.isPresent()) {
            Supplement supplement = optionalSupplement.get();
            for (Review review : supplement.getReviews()) {
                if (review.getUser().getUsername().equalsIgnoreCase(username)) {
                    exists = true;
                    existingId = review.getId();
                    break;
                }
            }
        } else if (optionalGarment.isPresent()) {
            Garment garment = optionalGarment.get();
            for (Review review : garment.getReviews()) {
                if (review.getUser().getUsername().equalsIgnoreCase(username)) {
                    exists = true;
                    existingId = review.getId();
                    break;
                }
            }
        } else {
            throw new RecordNotFoundException("No product found with id: " + productId);
        }

        if (exists) {
            throw new BadRequestException("Product: " + productId + " already has a review with id: " + existingId
                    + " written by user: " + username);
        } else {
            createdReview = reviewService.createPersonalReview(inputDto, username);
            dto = reviewService.assignReviewToProduct(productId, createdReview.getId());
            return dto;
        }
    }
}
