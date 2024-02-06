package nu.revitalized.revitalizedwebshop.repositories;

// Imports
import nu.revitalized.revitalizedwebshop.models.Allergen;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AllergenRepository extends JpaRepository<Allergen, Long> {
    List<Allergen> findAllergenByNameContainsIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
