package nu.revitalized.revitalizedwebshop.services;

// Imports

import static nu.revitalized.revitalizedwebshop.helpers.NameFormatter.formatName;
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.specifications.ShippingDetailsSpecification.*;

import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShippingDetailsDto;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.ShippingDetails;
import nu.revitalized.revitalizedwebshop.repositories.ShippingDetailsRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShippingDetailsService {
    private final ShippingDetailsRepository shippingDetailsRepository;

    public ShippingDetailsService(ShippingDetailsRepository shippingDetailsRepository) {
        this.shippingDetailsRepository = shippingDetailsRepository;
    }


    // Transfer Methods
    public static ShippingDetails dtoToShippingDetails(ShippingDetailsInputDto inputDto) {
        ShippingDetails shippingDetails = new ShippingDetails();

        shippingDetails.setDetailsName(inputDto.getDetailsName().toUpperCase());
        shippingDetails.setCountry(formatName(inputDto.getCountry()));
        shippingDetails.setCity(formatName(inputDto.getCity()));
        shippingDetails.setZipCode(inputDto.getZipCode().toUpperCase());
        shippingDetails.setStreet(formatName(inputDto.getStreet()));
        shippingDetails.setEmail(inputDto.getEmail().toLowerCase());

        // Set name
        if (inputDto.getMiddleName() != null) {
            shippingDetails.setName(formatName(inputDto.getFirstName())
                    + " " + inputDto.getMiddleName().toLowerCase()
                    + " " + formatName(inputDto.getLastName()));
        } else {
            shippingDetails.setName(formatName(inputDto.getFirstName())
                    + " " + formatName(inputDto.getLastName()));
        }

        // Set houseNumber
        if (inputDto.getHouseNumberAddition() != null) {
            String houseNumber = inputDto.getHouseNumber() +
                    inputDto.getHouseNumberAddition().toUpperCase();
            shippingDetails.setHouseNumber(houseNumber);
        } else {
            shippingDetails.setHouseNumber(String.valueOf(inputDto.getHouseNumber()));
        }

        return shippingDetails;
    }

    public static ShippingDetailsDto shippingDetailsToDto(ShippingDetails shippingDetails) {
        ShippingDetailsDto shippingDetailsDto = new ShippingDetailsDto();

        copyProperties(shippingDetails, shippingDetailsDto);

        return shippingDetailsDto;
    }

    // CRUD Methods --> GET Methods
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
            return shippingDetailsDtos;
        }
    }

    public ShippingDetailsDto getShippingDetailsById(Long id) {
        Optional<ShippingDetails> shippingDetails = shippingDetailsRepository.findById(id);

        if (shippingDetails.isPresent()) {
            return shippingDetailsToDto(shippingDetails.get());
        } else {
            throw new RecordNotFoundException("No shipping details found with id: " + id);
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

    // CRUD Methods --> POST Methods
    public ShippingDetailsDto createShippingDetails(ShippingDetailsInputDto inputDto) {
        ShippingDetails shippingDetails = dtoToShippingDetails(inputDto);
        List<ShippingDetailsDto> dtos = getAllShippingDetails();
        boolean isUnique = true;

        for (ShippingDetailsDto shippingDetailsDto : dtos) {
            if (inputDto.getHouseNumberAddition() != null) {
                if (shippingDetailsDto.getStreet().equalsIgnoreCase(inputDto.getStreet())
                        && shippingDetailsDto.getHouseNumber().equalsIgnoreCase(inputDto.getHouseNumber()
                        + inputDto.getHouseNumberAddition())) {
                    isUnique = false;
                    break;
                }
            } else {
                if (shippingDetailsDto.getStreet().equalsIgnoreCase(inputDto.getStreet())
                        && shippingDetailsDto
                        .getHouseNumber()
                        .equalsIgnoreCase(String.valueOf(inputDto.getHouseNumber()))) {
                    isUnique = false;
                    break;
                }
            }
        }

        if (isUnique) {
            shippingDetailsRepository.save(shippingDetails);
            return shippingDetailsToDto(shippingDetails);
        } else {
            throw new InvalidInputException("Shipping details with address: " + inputDto.getStreet() + " "
                    + inputDto.getHouseNumber() + inputDto.getHouseNumberAddition() + " already exists.");
        }
    }

    // CRUD Methods --> PUT/PATCH Methods
    public ShippingDetailsDto updateShippingDetails(Long id, ShippingDetailsInputDto inputDto) {
        Optional<ShippingDetails> optionalShippingDetails = shippingDetailsRepository.findById(id);

        if (optionalShippingDetails.isPresent()) {
            ShippingDetails shippingDetails = optionalShippingDetails.get();

            shippingDetails.setDetailsName(inputDto.getDetailsName().toUpperCase());
            shippingDetails.setCountry(formatName(inputDto.getCountry()));
            shippingDetails.setCity(formatName(inputDto.getCity()));
            shippingDetails.setZipCode(inputDto.getZipCode().toUpperCase());
            shippingDetails.setStreet(formatName(inputDto.getStreet()));
            shippingDetails.setEmail(inputDto.getEmail().toLowerCase());

            // Set name
            if (inputDto.getMiddleName() != null) {
                shippingDetails.setName(formatName(inputDto.getFirstName())
                        + " " + inputDto.getMiddleName().toLowerCase()
                        + " " + formatName(inputDto.getLastName()));
            } else {
                shippingDetails.setName(formatName(inputDto.getFirstName())
                        + " " + formatName(inputDto.getLastName()));
            }

            // Set houseNumber
            if (inputDto.getHouseNumberAddition() != null) {
                String houseNumber = inputDto.getHouseNumber() +
                        inputDto.getHouseNumberAddition().toUpperCase();
                shippingDetails.setHouseNumber(houseNumber);
            } else {
                shippingDetails.setHouseNumber(String.valueOf(inputDto.getHouseNumber()));
            }

            ShippingDetails updatedShippingDetails = shippingDetailsRepository.save(shippingDetails);

            return shippingDetailsToDto(updatedShippingDetails);
        } else {
            throw new RecordNotFoundException("No shipping details found with id: " + id);
        }
    }

    public ShippingDetailsDto patchShippingDetails(Long id, ShippingDetailsInputDto inputDto) {
        Optional<ShippingDetails> optionalShippingDetails = shippingDetailsRepository.findById(id);
    }
}
