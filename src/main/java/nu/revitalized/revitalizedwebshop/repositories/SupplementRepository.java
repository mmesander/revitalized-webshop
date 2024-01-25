package nu.revitalized.revitalizedwebshop.repositories;

// Imports
import nu.revitalized.revitalizedwebshop.models.Supplement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SupplementRepository extends JpaRepository<Supplement, Long>, JpaSpecificationExecutor<Supplement> {
    List<Supplement> findSupplementsByPriceLessThanEqual(Double price);

    @Query("SELECT s FROM Supplement s WHERE " +
            "(:name is null or lower(s.name) like %:name%) and " +
            "(:brand is null or lower(s.brand) like %:brand%) and " +
            "(:price is null or s.price <= :price) and " +
            "(:averageRating is null or s.averageRating >= :averageRating) and " +
            "(:contains is null or s.contains like %:contains%)")
    List<Supplement> findSupplementsByCriteria(
            @Param("name") String name,
            @Param("brand") String brand,
            @Param("price") Double price,
            @Param("averageRating") Double averageRating,
            @Param("contains") String contains
    );
}
