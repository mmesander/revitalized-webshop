package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.output.ShippingDetailsDto;
import nu.revitalized.revitalizedwebshop.services.ShippingDetailsService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShippingDetailsController {
    private final ShippingDetailsService shippingDetailsService;

    public ShippingDetailsController(ShippingDetailsService shippingDetailsService) {
        this.shippingDetailsService = shippingDetailsService;
    }


    // CRUD Requests -- GET Requests
    @GetMapping("/shipping-details")
    public ResponseEntity<List<ShippingDetailsDto>> getAllShippingDetails() {
        List<ShippingDetailsDto> dtos = shippingDetailsService.getAllShippingDetails();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/shipping-details/{id}")
    public ResponseEntity<ShippingDetailsDto> getShippingDetailsById(
            @PathVariable("id") Long id
    ) {
        ShippingDetailsDto dto = shippingDetailsService.getShippingDetailsById(id);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/shipping-details/search")
    public ResponseEntity<List<ShippingDetailsDto>> getShippingDetailsByParam(
         @RequestParam(required = false) String detailsName,
         @RequestParam(required = false) String name,
         @RequestParam(required = false) String country,
         @RequestParam(required = false) String city,
         @RequestParam(required = false) String zipCode,
         @RequestParam(required = false) String street,
         @RequestParam(required = false) String houseNumber,
         @RequestParam(required = false) String email
    ) {
        List<ShippingDetailsDto> dtos = shippingDetailsService.getShippingDetailsByParam(
                detailsName, name, country, city, zipCode, street, houseNumber, email);

        return ResponseEntity.ok().body(dtos);
    }
}
