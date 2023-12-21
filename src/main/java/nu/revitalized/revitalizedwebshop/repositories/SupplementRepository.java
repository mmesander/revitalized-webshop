package nu.revitalized.revitalizedwebshop.repositories;

// Imports
import nu.revitalized.revitalizedwebshop.models.Supplement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplementRepository extends JpaRepository<Supplement, Long> {
    List<Supplement> findSupplementByNameContainsIgnoreCase(String name);
    List<Supplement> findSupplementsByBrandContainsIgnoreCase(String brand);
    List<Supplement> findSupplementsByBrandContainsIgnoreCaseAndNameContainsIgnoreCase(String brand, String name);
    List<Supplement> findSupplementsByPriceLessThanEqual(Double price);
}
