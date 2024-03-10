package nu.revitalized.revitalizedwebshop.services;

import nu.revitalized.revitalizedwebshop.dtos.input.SupplementInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.*;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.helpers.CreateDate;
import nu.revitalized.revitalizedwebshop.models.Allergen;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.AllergenRepository;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
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

    public Supplement getSupplement1() {
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

    public Supplement getSupplement2() {
        Supplement supplement = new Supplement();
        supplement.setId(2L);
        supplement.setName("Protein Shake");
        supplement.setBrand("Energize Supps");
        supplement.setDescription("De beste eiwitshakes");
        supplement.setPrice(44.99);
        supplement.setStock(8);
        supplement.setContains("2500g");

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
        Supplement supplement = getSupplement1();
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
        Supplement supplement = getSupplement1();

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
        Supplement supplement = getSupplement1();

        // Act
        OrderItemDto result = SupplementService.supplementToOrderItemDto(supplement);

        // Assert
        assertEquals(supplement.getId(), result.getId());
        assertEquals(supplement.getName(), result.getName());
        assertEquals(supplement.getPrice(), result.getPrice());
        assertEquals(1, result.getQuantity());
    }

    @Test
    void getAllSupplements() {
        // Arrange
        List<Supplement> supplements = new ArrayList<>();
        Supplement supplement1 = getSupplement1();
        Supplement supplement2 = getSupplement2();
        supplements.add(supplement1);
        supplements.add(supplement2);
        when(supplementRepository.findAll()).thenReturn(supplements);

        // Act
        List<SupplementDto> result = supplementService.getAllSupplements();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(supplement1.getName(), result.get(0).getName());
        assertEquals(supplement2.getName(), result.get(1).getName());
    }

    @Test
    void getAllSupplements_Exception() {
        // Arrange
        when(supplementRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class, () -> supplementService.getAllSupplements());

        // Assert
        String expectedMessage = "No supplements are found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getSupplementById() {
        // Arrange
        Long id = 1L;
        Supplement supplement = getSupplement1();
        when(supplementRepository.findById(id)).thenReturn(Optional.of(supplement));

        // Act
        SupplementDto result = supplementService.getSupplementById(id);

        // Assert
        assertNotNull(result);
        assertEquals(supplement.getName(), result.getName());
    }

    @Test
    void getSupplementById_Exception() {
        // Arrange
        Long id = 3L;
        when(supplementRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> supplementService.getSupplementById(id));

        // Assert
        String expectedMessage = "Supplement with id: 3 not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
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