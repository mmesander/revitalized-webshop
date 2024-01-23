package nu.revitalized.revitalizedwebshop.repositories;

// Imports
import nu.revitalized.revitalizedwebshop.models.Garment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GarmentRepository extends JpaRepository<Garment, Long> {
    List<Garment> findGarmentsByColorContainsIgnoreCase(String color);
    List<Garment> findGarmentsBySizeContainsIgnoreCase(String size);
    List<Garment> findGarmentsByColorContainsIgnoreCaseAndSizeContainsIgnoreCase(String color, String size);
    List<Garment> findGarmentByPriceBefore(Double price);
}
