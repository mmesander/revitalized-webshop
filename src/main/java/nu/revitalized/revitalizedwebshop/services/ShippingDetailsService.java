package nu.revitalized.revitalizedwebshop.services;

// Imports

import static nu.revitalized.revitalizedwebshop.helpers.NameFormatter.formatName;
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.helpers.BuildFullName.*;
import static nu.revitalized.revitalizedwebshop.helpers.BuildHouseNumber.*;
import static nu.revitalized.revitalizedwebshop.services.UserService.userToDto;
import static nu.revitalized.revitalizedwebshop.services.UserService.userToShortDto;
import static nu.revitalized.revitalizedwebshop.helpers.BuildConfirmation.buildSpecificConfirmation;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.specifications.ShippingDetailsSpecification.*;

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

import java.util.*;

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
            shippingDetailsDto.setUser(userToShortDto(shippingDetails.getUser()));
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
        Optional<ShippingDetails> shippingDetails = shippingDetailsRepository.findById(id);

        if (shippingDetails.isPresent()) {
            return shippingDetailsToDto(shippingDetails.get());
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Shipping Details", id));
        }
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
        Optional<ShippingDetails> optionalShippingDetails = shippingDetailsRepository.findById(id);

        if (optionalShippingDetails.isPresent()) {
            ShippingDetails shippingDetails = optionalShippingDetails.get();

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
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Shipping Details", id));
        }
    }

    public ShippingDetailsDto patchShippingDetails(Long id, ShippingDetailsPatchInputDto inputDto) {
        Optional<ShippingDetails> optionalShippingDetails = shippingDetailsRepository.findById(id);

        if (optionalShippingDetails.isPresent()) {
            ShippingDetails shippingDetails = optionalShippingDetails.get();

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
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Shipping Details", id));
        }
    }

    public String deleteShippingDetailsById(Long id) {
        Optional<ShippingDetails> optionalShippingDetails = shippingDetailsRepository.findById(id);

        if (optionalShippingDetails.isPresent()) {

            shippingDetailsRepository.deleteById(id);

            return buildSpecificConfirmation("Shipping Details", optionalShippingDetails.get().getDetailsName(), id);
        } else {
            throw new RecordNotFoundException(buildIdNotFound("Shipping Details", id));
        }
    }

    // Relation - User Methods
    public UserDto assignUserToShippingDetails(String username, Long id) {
        Optional<ShippingDetails> optionalShippingDetails = shippingDetailsRepository.findById(id);
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalShippingDetails.isEmpty()) {
            throw new RecordNotFoundException(buildIdNotFound("Shipping Details", id));
        }

        if (optionalUser.isPresent()) {
            ShippingDetails shippingDetails = optionalShippingDetails.get();
            User user = optionalUser.get();

            Set<ShippingDetails> shippingDetailsSet = user.getShippingDetails();


            if (shippingDetails.getUser() != null) {
                throw new BadRequestException("Shipping Details with id: " + id
                        + " is already assigned to user: " + username);
            } else {
                shippingDetailsSet.add(shippingDetails);
                user.setShippingDetails(shippingDetailsSet);
                shippingDetails.setUser(user);
                shippingDetailsRepository.save(shippingDetails);

                return userToDto(user);
            }
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    // Relation - Authenticated User Methods
    public List<ShippingDetailsDto> getAllAuthUserShippingDetails(String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        Set<ShippingDetails> shippingDetailsSet;
        List<ShippingDetailsDto> shippingDetailsDtos = new ArrayList<>();

        if (optionalUser.isPresent()) {
            shippingDetailsSet = optionalUser.get().getShippingDetails();
        } else {
            throw new UsernameNotFoundException(username);
        }

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
        Optional<User> optionalUser = userRepository.findById(username);
        Optional<ShippingDetails> optionalShippingDetails = shippingDetailsRepository.findById(id);
        Set<ShippingDetails> shippingDetailsSet;
        ShippingDetailsDto dto = null;

        if (optionalUser.isPresent()) {
            shippingDetailsSet = optionalUser.get().getShippingDetails();
        } else {
            throw new UsernameNotFoundException(username);
        }

        if (optionalShippingDetails.isPresent()) {
            for (ShippingDetails shippingDetails : shippingDetailsSet) {
                if (shippingDetails.getId().equals(id)) {
                    dto = shippingDetailsToDto(shippingDetails);
                }
            }
        } else {
            throw new BadRequestException("No shipping details found with id: " + id);
        }

        if (dto != null) {
            return dto;
        } else {
            throw new BadRequestException("User: " + username + " does not have shipping details with id: " + id);
        }
    }
}
