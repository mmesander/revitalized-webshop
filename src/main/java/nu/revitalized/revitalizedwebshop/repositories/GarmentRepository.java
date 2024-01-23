package nu.revitalized.revitalizedwebshop.repositories;

// Imports
import nu.revitalized.revitalizedwebshop.models.Garment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GarmentRepository extends JpaRepository<Garment, Long> {

    @Query("SELECT g FROM Garment g WHERE " +
            "(:name is null or lower(g.name) like %:name%) and " +
            "(:brand is null or lower(g.brand) like %:brand%) and " +
            "(:price is null or g.price <= :price) and " +
            "(:averageRating is null or g.averageRating >= :averageRating) and " +
            "(:size is null or lower(g.size) like %:size%) and " +
            "(:color is null or lower(g.color) like %:color%)")
    List<Garment> findGarmentsByCriteria(
            @Param("name") String name,
            @Param("brand") String brand,
            @Param("price") Double price,
            @Param("averageRating") Double averageRating,
            @Param("size") String size,
            @Param("color") String color
    );
}
