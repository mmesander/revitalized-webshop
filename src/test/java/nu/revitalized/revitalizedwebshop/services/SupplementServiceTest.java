package nu.revitalized.revitalizedwebshop.services;

import nu.revitalized.revitalizedwebshop.dtos.input.SupplementInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.*;
import nu.revitalized.revitalizedwebshop.helpers.CreateDate;
import nu.revitalized.revitalizedwebshop.models.Allergen;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.AllergenRepository;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;





@AutoConfigureMockMvc(addFilters = false)
class SupplementServiceTest {
    @Mock
    private SupplementRepository supplementRepository;

    @Mock
    private AllergenRepository allergenRepository;

    @Mock
    private AllergenService allergenService;

    @InjectMocks
    private SupplementService supplementService;

    public SupplementInputDto getSupplementInputDto() {
        SupplementInputDto inputDto = new SupplementInputDto();
        inputDto.setName("Pre Workout");
        inputDto.setBrand("Energize");
        inputDto.setDescription("Hiermee ga je als een raket");
        inputDto.setPrice(34.99);
        inputDto.setStock(10);
        inputDto.setContains("200g");

        return inputDto;
    }

    public Supplement getSupplement() {
        Supplement supplement = new Supplement();
        supplement.setId(1L);
        supplement.setName("Creatine");
        supplement.setBrand("Energize Supps");
        supplement.setDescription("Creatine van energize is top");
        supplement.setPrice(26.99);
        supplement.setStock(5);
        supplement.setContains("500g");

        Allergen allergen1 = new Allergen();
        allergen1.setId(1L);
        allergen1.setName("Allergen one");

        Allergen allergen2 = new Allergen();
        allergen2.setId(2L);
        allergen2.setName("Allergen two");

        Set<Allergen> allergens = new HashSet<>();
        allergens.add(allergen1);
        allergens.add(allergen2);

        supplement.setAllergens(allergens);

        Review review1 = new Review();
        review1.setId(1L);
        review1.setReview("Goed product");
        review1.setRating(10);
        review1.setSupplement(supplement);
        review1.setDate(CreateDate.createDate());

        Review review2 = new Review();
        review2.setId(2L);
        review2.setReview("Slecht product");
        review2.setRating(1);
        review2.setSupplement(supplement);
        review2.setDate(CreateDate.createDate());

        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);

        supplement.setReviews(reviews);

        return supplement;
    }

    @Test
    @DisplayName("Should transfer inputDto to supplement")
    void dtoToSupplement() {
        // Arrange
        SupplementInputDto inputDto = getSupplementInputDto();

        // Act
        Supplement result = SupplementService.dtoToSupplement(inputDto);

        // Assert
        assertEquals(inputDto.getName(), result.getName());
        assertEquals(inputDto.getBrand(), result.getBrand());
        assertEquals(inputDto.getDescription(), result.getDescription());
        assertEquals(inputDto.getPrice(), result.getPrice());
        assertEquals(inputDto.getStock(), result.getStock());
        assertEquals(inputDto.getContains(), result.getContains());
        assertNull(result.getAllergens());
    }

    @Test
    @DisplayName("Should transfer supplement to supplementDto")
    void supplementToDto() {
        // Arrange
        Supplement supplement = getSupplement();
        Set<ShortAllergenDto> shortAllergenDtos = new TreeSet<>(Comparator.comparing(ShortAllergenDto::getId));
        List<ReviewDto> reviewDtos = new ArrayList<>();


        for (Allergen allergen : supplement.getAllergens()) {
            ShortAllergenDto shortAllergenDto = AllergenService.allergenToShortDto(allergen);
            shortAllergenDtos.add(shortAllergenDto);
        }

        for (Review review : supplement.getReviews()) {
            ReviewDto reviewDto = ReviewService.reviewToDto(review);
            reviewDtos.add(reviewDto);
        }
        reviewDtos.sort(Comparator.comparing(ReviewDto::getDate).reversed());

        // Act
        SupplementDto result = SupplementService.supplementToDto(supplement);


        // Assert
        assertEquals(supplement.getId(), result.getId());
        assertEquals(supplement.getName(), result.getName());
        assertEquals(supplement.getBrand(), result.getBrand());
        assertEquals(supplement.getDescription(), result.getDescription());
        assertEquals(supplement.getPrice(), result.getPrice());
        assertEquals(supplement.getStock(), result.getStock());
        assertEquals(supplement.getContains(), result.getContains());
        assertEquals(shortAllergenDtos.isEmpty(), result.getAllergens().isEmpty());
        if (!shortAllergenDtos.isEmpty() && !result.getAllergens().isEmpty()) {
            ShortAllergenDto expectedDto = shortAllergenDtos.iterator().next();
            ShortAllergenDto actualDto = result.getAllergens().iterator().next();
            assertEquals(expectedDto.getId(), actualDto.getId());
            assertEquals(expectedDto.getName(), actualDto.getName());
        }
        assertEquals(reviewDtos.isEmpty(), result.getReviews().isEmpty());
        if (!reviewDtos.isEmpty() && !result.getReviews().isEmpty()) {
            ReviewDto expectedDto = reviewDtos.iterator().next();
            ReviewDto actualDto = result.getReviews().iterator().next();
            assertEquals(expectedDto.getId(), actualDto.getId());
        }
    }

    @Test
    void supplementToShortDto() {
        // Arrange
        Supplement supplement = getSupplement();

        // Act
        ShortSupplementDto result = SupplementService.supplementToShortDto(supplement);

        // Assert
        assertEquals(supplement.getId(), result.getId());
        assertEquals(supplement.getName(), result.getName());
        assertEquals(supplement.getBrand(), result.getBrand());
        assertEquals(supplement.getDescription(), result.getDescription());
        assertEquals(supplement.getPrice(), result.getPrice());
        assertEquals(supplement.getStock(), result.getStock());
        assertEquals(supplement.getContains(), result.getContains());
    }

    @Test
    void supplementToOrderItemDto() {
        // Arrange
        Supplement supplement = getSupplement();

        // Act
        OrderItemDto result = SupplementService.supplementToOrderItemDto(supplement);

        // Assert
        assertEquals(supplement.getId(), result.getId());
        assertEquals(supplement.getName(), result.getName());
        assertEquals(supplement.getPrice(), result.getPrice());
        assertEquals(1, result.getQuantity());
    }
//
//    @Test
//    void getAllSupplements() {
//        // Arrange
//
//        // Act
//
//        // Assert
//    }
//
//    @Test
//    void getSupplementById() {
//        // Arrange
//
//        // Act
//
//        // Assert
//    }
//
//    @Test
//    void getSupplementsByParam() {
//        // Arrange
//
//        // Act
//
//        // Assert
//    }
//
//    @Test
//    void getOutOfStockSupplements() {
//        // Arrange
//
//        // Act
//
//        // Assert
//    }
//
//    @Test
//    void getInStockSupplements() {
//        // Arrange
//
//        // Act
//
//        // Assert
//    }
//
//    @Test
//    void createSupplement() {
//        // Arrange
//
//        // Act
//
//        // Assert
//    }
//
//    @Test
//    void updateSupplement() {
//        // Arrange
//
//        // Act
//
//        // Assert
//    }
//
//    @Test
//    void patchSupplement() {
//        // Arrange
//
//        // Act
//
//        // Assert
//    }
//
//    @Test
//    void deleteSupplement() {
//        // Arrange
//
//        // Act
//
//        // Assert
//    }
//
//    @Test
//    void assignAllergenToSupplement() {
//        // Arrange
//
//        // Act
//
//        // Assert
//    }
//
//    @Test
//    void removeAllergenFromSupplement() {
//        // Arrange
//
//        // Act
//
//        // Assert
//    }
}