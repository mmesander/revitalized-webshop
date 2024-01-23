package nu.revitalized.revitalizedwebshop.repositories;

// Imports
import nu.revitalized.revitalizedwebshop.models.Supplement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SupplementRepository extends JpaRepository<Supplement, Long> {
    List<Supplement> findSupplementByNameContainsIgnoreCase(String name);
    List<Supplement> findSupplementsByBrandContainsIgnoreCase(String brand);
    List<Supplement> findSupplementsByBrandContainsIgnoreCaseAndNameContainsIgnoreCase(String brand, String name);
    List<Supplement> findSupplementsByPriceLessThanEqual(Double price);

    @Query("SELECT s FROM Supplement s WHERE " +
            "(:brand is null or s.brand like %:brand%) and " +
            "(:name is null or s.name like %:name%) and " +
            "(:price is null or s.price = :price) and " +
            "(:averageRating is null or s.averageRating = :averageRating) and " +
            "(:contains is null or s.contains like %:contains%)")
    List<Supplement> findSupplementsByCriteria(
            @Param("brand") String brand,
            @Param("name") String name,
            @Param("price") BigDecimal price,
            @Param("averageRating") BigDecimal averageRating,
            @Param("contains") String contains
    );
}
