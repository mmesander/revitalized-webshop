package nu.revitalized.revitalizedwebshop.repositories;

// Imports
import nu.revitalized.revitalizedwebshop.models.Garment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GarmentRepository extends JpaRepository<Garment, Long>, JpaSpecificationExecutor<Garment> {
    boolean existsByNameIgnoreCase(String name);
}
