package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.ShippingDetailsInputDto;
import nu.revitalized.revitalizedwebshop.models.ShippingDetails;
import nu.revitalized.revitalizedwebshop.repositories.ShippingDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class ShippingDetailsService {
    private final ShippingDetailsRepository shippingDetailsRepository;

    public ShippingDetailsService(ShippingDetailsRepository shippingDetailsRepository) {
        this.shippingDetailsRepository = shippingDetailsRepository;
    }


    // Transfer Methods
    public static ShippingDetails dtoToShippingDetails(ShippingDetailsInputDto inputDto) {
        ShippingDetails shippingDetails = new ShippingDetails();

        shippingDetails.setShippingDetailsName(inputDto.getShippingDetailsName());


        if (inputDto.getMiddleName() != null) {
            shippingDetails.setName(firstName + " " + inputDto.getMiddleName().toLowerCase() + " " + lastName);
        } else {
            shippingDetails.setName(inputDto.getFirstName() + " " + inputDto.getLastName());
        }
    }
}
