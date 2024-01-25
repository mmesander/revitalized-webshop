package nu.revitalized.revitalizedwebshop.repositories;

// Imports
import nu.revitalized.revitalizedwebshop.models.Supplement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SupplementRepository extends JpaRepository<Supplement, Long>, JpaSpecificationExecutor<Supplement> {
}
