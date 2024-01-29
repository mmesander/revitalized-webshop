package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.NameFormatter.formatName;
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShippingDetailsDto;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.ShippingDetails;
import nu.revitalized.revitalizedwebshop.repositories.ShippingDetailsRepository;
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

        shippingDetails.setShippingDetailsName(inputDto.getShippingDetailsName().toUpperCase());
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


}
