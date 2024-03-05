package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.helpers.BuildConfirmation.*;
import static nu.revitalized.revitalizedwebshop.specifications.DiscountSpecification.*;
import nu.revitalized.revitalizedwebshop.dtos.input.DiscountInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.DiscountPatchInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.DiscountDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShortDiscountDto;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.exceptions.UsernameNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Discount;
import nu.revitalized.revitalizedwebshop.models.User;
import nu.revitalized.revitalizedwebshop.repositories.DiscountRepository;
import nu.revitalized.revitalizedwebshop.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DiscountService {
    private final DiscountRepository discountRepository;
    private final UserRepository userRepository;

    public DiscountService(
            DiscountRepository discountRepository,
            UserRepository userRepository
    ) {
        this.discountRepository = discountRepository;
        this.userRepository = userRepository;
    }

    // Transfer Methods
    public static Discount dtoToDiscount(DiscountInputDto inputDto) {
        Discount discount = new Discount();

        copyProperties(inputDto, discount);

        return discount;
    }

    public static DiscountDto discountToDto(Discount discount) {
        DiscountDto discountDto = new DiscountDto();

        copyProperties(discount, discountDto);

        if (discount.getUsers() != null) {
            Set<String> users = new HashSet<>();

            for (User user : discount.getUsers()) {
                String username = user.getUsername();
                users.add(username);
            }
            discountDto.setUsers(users);
        }

        return discountDto;
    }

    public static ShortDiscountDto discountToShortDto(Discount discount) {
        ShortDiscountDto shortDto = new ShortDiscountDto();

        copyProperties(discount, shortDto);

        return shortDto;
    }

    // CRUD Methods
    public List<DiscountDto> getAllDiscounts() {
        List<Discount> discounts = discountRepository.findAll();
        List<DiscountDto> discountDtos = new ArrayList<>();

        for (Discount discount : discounts) {
            DiscountDto discountDto = discountToDto(discount);
            discountDtos.add(discountDto);
        }

        if (discountDtos.isEmpty()) {
            throw new RecordNotFoundException("No discounts found");
        } else {
            discountDtos.sort(Comparator.comparing(DiscountDto::getId));

            return discountDtos;
        }
    }

    public DiscountDto getDiscountById(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Discount", id)));

        return discountToDto(discount);
    }

    public List<DiscountDto> getAllDiscountsByParam(
            String name,
            Double value,
            Double minValue,
            Double maxValue
    ) {
        Specification<Discount> params = Specification.where
                        (StringUtils.isBlank(name) ? null : getDiscountNameLikeFilter(name))
                .and(value == null ? null : getDiscountValueLikeFilter(value))
                .and(minValue == null ? null : getDiscountValueMoreThanFilter(minValue))
                .and(maxValue == null ? null : getDiscountValueLessThanFilter(maxValue));

        List<Discount> filteredDiscounts = discountRepository.findAll(params);
        List<DiscountDto> discountDtos = new ArrayList<>();

        for (Discount discount : filteredDiscounts) {
            DiscountDto discountDto = discountToDto(discount);
            discountDtos.add(discountDto);
        }

        if (discountDtos.isEmpty()) {
            throw new RecordNotFoundException("No discounts found with the specified filters");
        } else {
            discountDtos.sort(Comparator.comparing(DiscountDto::getValue).reversed());

            return discountDtos;
        }
    }

    public List<DiscountDto> getAllActiveDiscounts() {
        List<Discount> discounts = discountRepository.findAll();
        List<DiscountDto> discountDtos = new ArrayList<>();

        for (Discount discount : discounts) {
            if (!discount.getUsers().isEmpty()) {
                DiscountDto discountDto = discountToDto(discount);
                discountDtos.add(discountDto);
            }
        }

        if (discountDtos.isEmpty()) {
            throw new RecordNotFoundException("No active discounts found");
        } else {
            discountDtos.sort(Comparator.comparing(DiscountDto::getId));

            return discountDtos;
        }
    }

    public List<DiscountDto> getAllInactiveDiscounts() {
        List<Discount> discounts = discountRepository.findAll();
        List<DiscountDto> discountDtos = new ArrayList<>();

        for (Discount discount : discounts) {
            if (discount.getUsers().isEmpty()) {
                DiscountDto discountDto = discountToDto(discount);
                discountDtos.add(discountDto);
            }
        }

        if (discountDtos.isEmpty()) {
            throw new RecordNotFoundException("No inactive discounts found");
        } else {
            discountDtos.sort(Comparator.comparing(DiscountDto::getId));

            return discountDtos;
        }
    }

    public DiscountDto createDiscount(DiscountInputDto inputDto) {
        Discount discount = dtoToDiscount(inputDto);

        discountRepository.save(discount);

        return discountToDto(discount);
    }

    public DiscountDto updateDiscount(Long id, DiscountInputDto inputDto) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Discount", id)));

        copyProperties(inputDto, discount);
        Discount updatedDiscount = discountRepository.save(discount);

        return discountToDto(updatedDiscount);
    }

    public DiscountDto patchDiscount(Long id, DiscountPatchInputDto inputDto) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Discount", id)));

        if (inputDto.getName() != null) {
            discount.setName(inputDto.getName());
        }
        if (inputDto.getValue() != null) {
            discount.setValue(inputDto.getValue());
        }
        Discount patchedDiscount = discountRepository.save(discount);

        return discountToDto(patchedDiscount);
    }

    public String deleteDiscount(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Discount", id)));

        if (!discount.getUsers().isEmpty()) {
            throw new BadRequestException("Can't remove an active discount, remove all users from discount first");
        }

        discountRepository.deleteById(id);
        return buildSpecificConfirmation("Discount", discount.getName(), id);
    }

    // Relation - User Methods
    public DiscountDto assignUserToDiscount(String username, Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Discount", id)));

        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        for (Discount presentDiscount : user.getDiscounts()) {
            if (presentDiscount.getName().equalsIgnoreCase(discount.getName())) {
                throw new InvalidInputException("User: " + username + " already has discount: " + discount.getName());
            }
        }

        Set<User> users = discount.getUsers();
        users.add(user);
        discount.setUsers(users);
        Discount assignedDiscount = discountRepository.save(discount);

        return discountToDto(assignedDiscount);
    }

    public String removeUserFromDiscount(String username, Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Discount", id)));

        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Set<User> users = discount.getUsers();

        if (!users.contains(user)) {
            throw new InvalidInputException("User: " + username + " does not have discount: " + discount.getName());
        }

        users.remove(user);
        discount.setUsers(users);
        discountRepository.save(discount);

        return "Discount: " + discount.getName() + " with id: " + discount.getId() + " is removed from user: "
                + username;
    }

    // Relation - Authenticated User Methods
    public List<String> getAllAuthUserDiscounts(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Set<Discount> discounts = user.getDiscounts();
        List<ShortDiscountDto> shortDtos = new ArrayList<>();

        for (Discount discount : discounts) {
            ShortDiscountDto shortDto = discountToShortDto(discount);
            shortDtos.add(shortDto);
        }

        if (shortDtos.isEmpty()) {
            throw new RecordNotFoundException("No discounts found for user: " + username);
        } else {
            shortDtos.sort(Comparator.comparing(ShortDiscountDto::getValue).reversed());
            List<String> strings = new ArrayList<>();
            for (ShortDiscountDto shortDto : shortDtos) {
                strings.add(shortDto.getValue() + "% discount with code: " + shortDto.getName());
            }

            return strings;
        }
    }
}