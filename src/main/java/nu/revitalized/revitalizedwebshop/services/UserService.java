package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.*;
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
import static nu.revitalized.revitalizedwebshop.helpers.BuildHouseNumber.buildHouseNumber;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.security.config.SpringSecurityConfig.passwordEncoder;
import static nu.revitalized.revitalizedwebshop.services.DiscountService.discountToShortDto;
import static nu.revitalized.revitalizedwebshop.services.OrderService.orderToShortDto;
import static nu.revitalized.revitalizedwebshop.services.ReviewService.reviewToDto;
import static nu.revitalized.revitalizedwebshop.services.ShippingDetailsService.shippingDetailsToShortDto;
import static nu.revitalized.revitalizedwebshop.specifications.UserSpecification.getUserEmailLike;
import static nu.revitalized.revitalizedwebshop.specifications.UserSpecification.getUserUsernameLikeFilter;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final ShippingDetailsRepository shippingDetailsRepository;
    private final SupplementRepository supplementRepository;
    private final GarmentRepository garmentRepository;
    private final ShippingDetailsService shippingDetailsService;
    private final ReviewService reviewService;
    private final DiscountService discountService;
    private final OrderService orderService;

    public UserService(
            UserRepository userRepository,
            AuthorityRepository authorityRepository,
            ShippingDetailsRepository shippingDetailsRepository,
            SupplementRepository supplementRepository,
            GarmentRepository garmentRepository,
            ShippingDetailsService shippingDetailsService,
            ReviewService reviewService,
            DiscountService discountService,
            OrderService orderService
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.shippingDetailsRepository = shippingDetailsRepository;
        this.supplementRepository = supplementRepository;
        this.garmentRepository = garmentRepository;
        this.shippingDetailsService = shippingDetailsService;
        this.reviewService = reviewService;
        this.discountService = discountService;
        this.orderService = orderService;
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
            Set<ShortShippingDetailsDto> dtos = new TreeSet<>(Comparator.comparingLong(ShortShippingDetailsDto::getId));
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
            Set<ShortDiscountDto> discounts = new HashSet<>();

            for (Discount discount : user.getDiscounts()) {
                ShortDiscountDto shortDto = discountToShortDto(discount);
                discounts.add(shortDto);
            }
            userDto.setDiscounts(discounts);
        }

        if (user.getOrders() != null) {
            List<ShortOrderDto> shortOrderDtos = new ArrayList<>();

            for (Order order : user.getOrders()) {
                ShortOrderDto shortOrderDto = orderToShortDto(order);
                shortOrderDtos.add(shortOrderDto);
            }
            userDto.setOrders(shortOrderDtos);
        }

        return userDto;
    }

    public static ShortUserDto userToShortDto(User user) {
        ShortUserDto shortUserDto = new ShortUserDto();

        copyProperties(user, shortUserDto);

        return shortUserDto;
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
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return userToDto(user);
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
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        user.setEmail(inputDto.getEmail());
        User updatedUser = userRepository.save(user);

        return userToDto(updatedUser);
    }

    public String deleteUser(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (user.getUsername().equalsIgnoreCase("mmesander")) {
            throw new BadRequestException("Can't remove user: " + user.getUsername());
        }

        userRepository.deleteById(username);

        return "User: " + username + " is deleted";
    }

    // Relation - Authorities Methods
    public Set<Authority> getUserAuthorities(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        UserDto userDto = userToDto(user);

        return userDto.getAuthorities();
    }

    public UserDto assignAuthorityToUser(String username, String authority) {
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

    public String removeAuthorityFromUser(String username, String authority) {
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
    public Set<ShortDiscountDto> getAllDiscountsFromUser(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (user.getDiscounts().isEmpty()) {
            throw new RecordNotFoundException("No discounts found for user: " + username);
        }

        return userToDto(user).getDiscounts();
    }

    public String removeAllDiscountsFromUser(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (user.getDiscounts().isEmpty()) {
            throw new RecordNotFoundException("No discounts found for user: " + username);
        }

        for (Discount discount : user.getDiscounts()) {
            discountService.removeUserFromDiscount(username, discount.getId());
        }

        return "All discounts from user: " + username + " are removed";
    }

    // Relation - Authenticated User Methods
    public UserDto addAuthUserShippingDetails(String username, ShippingDetailsInputDto inputDto) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

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
            shippingDetailsService.assignUserToShippingDetails(username, presentShippingDetails.getId());
        } else {
            throw new BadRequestException("Shipping details with address: " + inputDto.getStreet() + houseNumber
                    + " is not found");
        }

        return userToDto(user);
    }

    public Object addAuthUserProductReview(String username, ReviewInputDto inputDto, Long productId) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        boolean exists = false;
        Long existingId = null;

        if (supplementRepository.existsById(productId)) {
            Supplement supplement = supplementRepository.findById(productId).orElseThrow();
            for (Review review : supplement.getReviews()) {
                if (review.getUser().getUsername().equalsIgnoreCase(username)) {
                    exists = true;
                    existingId = review.getId();
                    break;
                }
            }
        } else if (garmentRepository.existsById(productId)) {
            Garment garment = garmentRepository.findById(productId).orElseThrow();
            for (Review review : garment.getReviews()) {
                if (review.getUser().getUsername().equalsIgnoreCase(username)) {
                    exists = true;
                    existingId = review.getId();
                    break;
                }
            }
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Product", productId));
        }

        if (exists) {
            throw new BadRequestException("Product: " + productId + " already has a review with id: " + existingId
                    + " written by user: " + username);
        }

        ReviewDto createdReview = reviewService.createAuthUserReview(inputDto, user.getUsername());
        Object objectDto = reviewService.assignReviewToProduct(productId, createdReview.getId());

        return objectDto;
    }

    public OrderDto addAuthUserOrder(String username, AuthUserOrderInputDto inputDto) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        // Check if user has valid discount
        if (!inputDto.getDiscountCode().isEmpty()) {
            boolean hasDiscount = false;
            for (Discount discount : user.getDiscounts()) {
                if (inputDto.getDiscountCode().equalsIgnoreCase(discount.getName())
                        && discount.getUsers().contains(user)) {
                    hasDiscount = true;
                    break;
                }
            }
            if (!hasDiscount) {
                throw new BadRequestException("Discount: " + inputDto.getDiscountCode() + " is not valid");
            }
        }

        // Check if user has shipping details with specified ID
        boolean hasShippingDetails = false;
        for (ShippingDetails shippingDetails : user.getShippingDetails()) {
            if (inputDto.getShippingDetailsId().equals(shippingDetails.getId())) {
                hasShippingDetails = true;
                break;
            }
        }
        if (!hasShippingDetails) {
            throw new BadRequestException("User: " + username + " does not have shipping details with id: "
                    + inputDto.getShippingDetailsId());
        }

        OrderDto createdOrder = orderService.createAuthUserOrder(username, inputDto);

        return orderService.assignMultipleProductsToOrder(
                createdOrder.getOrderNumber(), inputDto.getProductIds()
        );
    }
}