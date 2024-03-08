package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsPatchInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShippingDetailsDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShortShippingDetailsDto;
import nu.revitalized.revitalizedwebshop.dtos.output.UserDto;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.exceptions.UsernameNotFoundException;
import nu.revitalized.revitalizedwebshop.models.ShippingDetails;
import nu.revitalized.revitalizedwebshop.models.User;
import nu.revitalized.revitalizedwebshop.repositories.ShippingDetailsRepository;
import nu.revitalized.revitalizedwebshop.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import static nu.revitalized.revitalizedwebshop.helpers.BuildConfirmation.buildSpecificConfirmation;
import static nu.revitalized.revitalizedwebshop.helpers.BuildFullName.buildFullName;
import static nu.revitalized.revitalizedwebshop.helpers.BuildFullName.buildFullNamePatch;
import static nu.revitalized.revitalizedwebshop.helpers.BuildHouseNumber.buildHouseNumber;
import static nu.revitalized.revitalizedwebshop.helpers.BuildHouseNumber.buildHouseNumberPatch;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.NameFormatter.formatName;
import static nu.revitalized.revitalizedwebshop.services.UserService.userToDto;
import static nu.revitalized.revitalizedwebshop.specifications.ShippingDetailsSpecification.*;

@Service
public class ShippingDetailsService {
    private final ShippingDetailsRepository shippingDetailsRepository;
    private final UserRepository userRepository;

    public ShippingDetailsService(
            ShippingDetailsRepository shippingDetailsRepository,
            UserRepository userRepository
    ) {
        this.shippingDetailsRepository = shippingDetailsRepository;
        this.userRepository = userRepository;
    }

    // Transfer Methods
    public static ShippingDetails dtoToShippingDetails(ShippingDetailsInputDto inputDto) {
        ShippingDetails shippingDetails = new ShippingDetails();

        shippingDetails.setDetailsName(inputDto.getDetailsName().toUpperCase());
        shippingDetails.setName(buildFullName(inputDto));
        shippingDetails.setCountry(formatName(inputDto.getCountry()));
        shippingDetails.setCity(formatName(inputDto.getCity()));
        shippingDetails.setZipCode(inputDto.getZipCode().toUpperCase());
        shippingDetails.setStreet(formatName(inputDto.getStreet()));
        shippingDetails.setHouseNumber(buildHouseNumber(inputDto));
        shippingDetails.setEmail(inputDto.getEmail().toLowerCase());

        return shippingDetails;
    }

    public static ShippingDetailsDto shippingDetailsToDto(ShippingDetails shippingDetails) {
        ShippingDetailsDto shippingDetailsDto = new ShippingDetailsDto();

        copyProperties(shippingDetails, shippingDetailsDto);

        if (shippingDetails.getUser() != null) {
            shippingDetailsDto.setUser(shippingDetails.getUser().getUsername());
        }

        return shippingDetailsDto;
    }

    public static ShortShippingDetailsDto shippingDetailsToShortDto(ShippingDetails shippingDetails) {
        ShortShippingDetailsDto shortDto = new ShortShippingDetailsDto();

        copyProperties(shippingDetails, shortDto);

        return shortDto;
    }

    // CRUD Methods
    public List<ShippingDetailsDto> getAllShippingDetails() {
        List<ShippingDetails> shippingDetailsList = shippingDetailsRepository.findAll();
        List<ShippingDetailsDto> shippingDetailsDtos = new ArrayList<>();

        for (ShippingDetails shippingDetails : shippingDetailsList) {
            ShippingDetailsDto shippingDetailsDto = shippingDetailsToDto(shippingDetails);
            shippingDetailsDtos.add(shippingDetailsDto);
        }

        if (shippingDetailsDtos.isEmpty()) {
            throw new RecordNotFoundException("No shipping details found");
        } else {
            shippingDetailsDtos.sort(Comparator.comparing(ShippingDetailsDto::getId));
            return shippingDetailsDtos;
        }
    }

    public ShippingDetailsDto getShippingDetailsById(Long id) {
        ShippingDetails shippingDetails = shippingDetailsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Shipping details", id)));

        return shippingDetailsToDto(shippingDetails);
    }

    public List<ShippingDetailsDto> getShippingDetailsByParam(
            String detailsName,
            String name,
            String country,
            String city,
            String zipCode,
            String street,
            String houseNumber,
            String email
    ) {
        Specification<ShippingDetails> params = Specification.where
                        (StringUtils.isBlank(detailsName) ? null : getShippingDetailsDetailsNameLikeFilter(detailsName))
                .and(StringUtils.isBlank(name) ? null : getShippingDetailsNameLikeFilter(name))
                .and(StringUtils.isBlank(country) ? null : getShippingDetailsCountryLikeFilter(country))
                .and(StringUtils.isBlank(city) ? null : getShippingDetailsCityLikeFilter(city))
                .and(StringUtils.isBlank(zipCode) ? null : getShippingDetailsZipCodeLikeFilter(zipCode))
                .and(StringUtils.isBlank(street) ? null : getShippingDetailsStreetLikeFilter(street))
                .and(StringUtils.isBlank(houseNumber) ? null : getShippingDetailsHouseNumberLikeFilter(houseNumber))
                .and(StringUtils.isBlank(email) ? null : getShippingDetailsEmailLikeFilter(email));

        List<ShippingDetails> filteredShippingDetails = shippingDetailsRepository.findAll(params);
        List<ShippingDetailsDto> shippingDetailsDtos = new ArrayList<>();

        for (ShippingDetails shippingDetails : filteredShippingDetails) {
            ShippingDetailsDto shippingDetailsDto = shippingDetailsToDto(shippingDetails);
            shippingDetailsDtos.add(shippingDetailsDto);
        }

        if (shippingDetailsDtos.isEmpty()) {
            throw new RecordNotFoundException("No shipping details found with the specified filters");
        } else {
            return shippingDetailsDtos;
        }
    }

    public ShippingDetailsDto createShippingDetails(ShippingDetailsInputDto inputDto, String username) {
        ShippingDetails shippingDetails = dtoToShippingDetails(inputDto);

        boolean exists = shippingDetailsRepository.existsByStreetIgnoreCaseAndHouseNumberAndUser_Username(
                inputDto.getStreet(), buildHouseNumber(inputDto), username
        );

        if (exists) {
            if (inputDto.getHouseNumberAddition() != null) {
                throw new InvalidInputException("Shipping details with address: " + inputDto.getStreet() + " "
                        + inputDto.getHouseNumber() + inputDto.getHouseNumberAddition().toUpperCase() + " already exists.");
            } else {
                throw new InvalidInputException("Shipping details with address: " + inputDto.getStreet() + " "
                        + inputDto.getHouseNumber() + " already exists.");
            }
        } else {
            shippingDetailsRepository.save(shippingDetails);

            return shippingDetailsToDto(shippingDetails);
        }
    }

    public ShippingDetailsDto updateShippingDetails(Long id, ShippingDetailsInputDto inputDto) {
        ShippingDetails shippingDetails = shippingDetailsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Shipping details", id)));

        shippingDetails.setDetailsName(inputDto.getDetailsName().toUpperCase());
        shippingDetails.setName(buildFullName(inputDto));
        shippingDetails.setCountry(formatName(inputDto.getCountry()));
        shippingDetails.setCity(formatName(inputDto.getCity()));
        shippingDetails.setZipCode(inputDto.getZipCode().toUpperCase());
        shippingDetails.setStreet(formatName(inputDto.getStreet()));
        shippingDetails.setHouseNumber(buildHouseNumber(inputDto));
        shippingDetails.setEmail(inputDto.getEmail().toLowerCase());
        ShippingDetails updatedShippingDetails = shippingDetailsRepository.save(shippingDetails);

        return shippingDetailsToDto(updatedShippingDetails);
    }

    public ShippingDetailsDto patchShippingDetails(Long id, ShippingDetailsPatchInputDto inputDto) {
        ShippingDetails shippingDetails = shippingDetailsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Shipping details", id)));

        if (inputDto.getDetailsName() != null) {
            shippingDetails.setDetailsName(inputDto.getDetailsName().toUpperCase());
        }
        if ((inputDto.getFirstName() != null && inputDto.getMiddleName() != null && inputDto.getLastName() != null)
                || (inputDto.getFirstName() != null && inputDto.getLastName() != null)) {
            shippingDetails.setDetailsName(buildFullNamePatch(inputDto));
        }
        if (inputDto.getCountry() != null) {
            shippingDetails.setCountry(formatName(inputDto.getCountry()));
        }
        if (inputDto.getCity() != null) {
            shippingDetails.setCity(formatName(inputDto.getCity()));
        }
        if (inputDto.getZipCode() != null) {
            shippingDetails.setZipCode(inputDto.getZipCode().toUpperCase());
        }
        if (inputDto.getStreet() != null) {
            shippingDetails.setStreet(formatName(inputDto.getStreet()));
        }
        if ((inputDto.getHouseNumber() != null && inputDto.getHouseNumberAddition() != null) ||
                inputDto.getHouseNumber() != null) {
            shippingDetails.setHouseNumber(buildHouseNumberPatch(inputDto));
        }
        if (inputDto.getEmail() != null) {
            shippingDetails.setEmail(inputDto.getEmail().toLowerCase());
        }
        ShippingDetails patchedShippingDetails = shippingDetailsRepository.save(shippingDetails);

        return shippingDetailsToDto(patchedShippingDetails);
    }

    public String deleteShippingDetailsById(Long id) {
        ShippingDetails shippingDetails = shippingDetailsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Shipping details", id)));

        shippingDetailsRepository.deleteById(id);

        return buildSpecificConfirmation("Shipping Details", shippingDetails.getDetailsName(), id);
    }

    // Relation - User Methods
    public UserDto assignUserToShippingDetails(String username, Long id) {
        ShippingDetails shippingDetails = shippingDetailsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Shipping details", id)));

        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (shippingDetails.getUser() != null) {
            throw new BadRequestException("Shipping Details with id: " + id
                    + " is already assigned to user: " + shippingDetails.getUser().getUsername());
        }

        Set<ShippingDetails> shippingDetailsSet = user.getShippingDetails();
        shippingDetailsSet.add(shippingDetails);
        user.setShippingDetails(shippingDetailsSet);
        shippingDetails.setUser(user);
        shippingDetailsRepository.save(shippingDetails);

        return userToDto(user);
    }

    // Relation - Authenticated User Methods
    public List<ShippingDetailsDto> getAllAuthUserShippingDetails(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Set<ShippingDetails> shippingDetailsSet = user.getShippingDetails();
        List<ShippingDetailsDto> shippingDetailsDtos = new ArrayList<>();

        for (ShippingDetails shippingDetails : shippingDetailsSet) {
            ShippingDetailsDto shippingDetailsDto = shippingDetailsToDto(shippingDetails);
            shippingDetailsDtos.add(shippingDetailsDto);
        }

        if (shippingDetailsDtos.isEmpty()) {
            throw new RecordNotFoundException("No shipping details found from user: " + username);
        } else {
            shippingDetailsDtos.sort(Comparator.comparing(ShippingDetailsDto::getId));
            return shippingDetailsDtos;
        }
    }

    public ShippingDetailsDto getAuthUserShippingDetailsById(String username, Long id) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        ShippingDetails shippingDetails = shippingDetailsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(buildIdNotFound("Shipping details", id)));

        Set<ShippingDetails> shippingDetailsSet = user.getShippingDetails();
        ShippingDetailsDto dto = null;

        for (ShippingDetails foundShippingDetails : shippingDetailsSet) {
            if (foundShippingDetails.equals(shippingDetails)) {
                dto = shippingDetailsToDto(shippingDetails);
            }
        }

        if (dto == null) {
            throw new BadRequestException("User: " + username + " does not have shipping details with id: " + id);
        }

        return dto;
    }
}