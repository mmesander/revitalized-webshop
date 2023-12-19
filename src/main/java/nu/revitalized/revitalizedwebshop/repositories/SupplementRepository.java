package nu.revitalized.revitalizedwebshop.repositories;

// Imports
import nu.revitalized.revitalizedwebshop.models.Supplement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplementRepository extends JpaRepository<Supplement, Long> {
    List<Supplement> findSupplementByName(String name);
    List<Supplement> findSupplementsByBrand(String brand);
    List<Supplement> findSupplementsByBrandAndName(String brand, String name);
    List<Supplement> findSupplementsByPriceBefore(Double price);
}
