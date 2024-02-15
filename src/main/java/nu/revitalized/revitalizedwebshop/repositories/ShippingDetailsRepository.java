package nu.revitalized.revitalizedwebshop.repositories;

// Imports
import nu.revitalized.revitalizedwebshop.models.ShippingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ShippingDetailsRepository extends JpaRepository<ShippingDetails, Long>, JpaSpecificationExecutor<ShippingDetails> {
    boolean existsByStreetIgnoreCaseAndHouseNumberAndUser_Username(String street, String houseNumber, String username);
    Optional<ShippingDetails> findByStreetIgnoreCaseAndHouseNumber(String street, String houseNumber);
}
