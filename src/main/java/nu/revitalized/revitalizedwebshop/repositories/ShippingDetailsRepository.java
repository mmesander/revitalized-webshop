package nu.revitalized.revitalizedwebshop.repositories;

// Imports
import nu.revitalized.revitalizedwebshop.models.ShippingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShippingDetailsRepository extends JpaRepository<ShippingDetails, Long>, JpaSpecificationExecutor<ShippingDetails> {
}
