package nu.revitalized.revitalizedwebshop.services;

import nu.revitalized.revitalizedwebshop.dtos.input.SupplementInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.*;
import nu.revitalized.revitalizedwebshop.exceptions.InvalidInputException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.helpers.CreateDate;
import nu.revitalized.revitalizedwebshop.models.Allergen;
import nu.revitalized.revitalizedwebshop.models.Review;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.AllergenRepository;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



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

    SupplementInputDto mockInputDto;
    Supplement mockSupplement1;
    Supplement mockSupplement2;

    @BeforeEach
    void init() {
        // Supplement InputDto
        mockInputDto = new SupplementInputDto();
        mockInputDto.setName("Pre Workout");
        mockInputDto.setBrand("Energize");
        mockInputDto.setDescription("Hiermee ga je als een raket");
        mockInputDto.setPrice(34.99);
        mockInputDto.setStock(10);
        mockInputDto.setContains("200g");

        // Supplement 1
        mockSupplement1 = new Supplement();
        mockSupplement1.setId(1L);
        mockSupplement1.setName("Creatine");
        mockSupplement1.setBrand("Energize Supps");
        mockSupplement1.setDescription("Creatine van energize is top");
        mockSupplement1.setPrice(26.99);
        mockSupplement1.setStock(0);
        mockSupplement1.setContains("500g");

        Allergen mockAllergen1 = new Allergen();
        mockAllergen1.setId(1L);
        mockAllergen1.setName("Allergen one");

        Allergen mockAllergen2 = new Allergen();
        mockAllergen2.setId(2L);
        mockAllergen2.setName("Allergen two");

        Set<Allergen> mockAllergens1 = new HashSet<>();
        mockAllergens1.add(mockAllergen1);
        mockAllergens1.add(mockAllergen2);

        mockSupplement1.setAllergens(mockAllergens1);

        Review mockReview1 = new Review();
        mockReview1.setId(1L);
        mockReview1.setReview("Goed product");
        mockReview1.setRating(10);
        mockReview1.setSupplement(mockSupplement1);
        mockReview1.setDate(CreateDate.createDate());

        Review mockReview2 = new Review();
        mockReview2.setId(2L);
        mockReview2.setReview("Slecht product");
        mockReview2.setRating(1);
        mockReview2.setSupplement(mockSupplement1);
        mockReview2.setDate(CreateDate.createDate());

        List<Review> mockReviews1 = new ArrayList<>();
        mockReviews1.add(mockReview1);
        mockReviews1.add(mockReview2);

        mockSupplement1.setReviews(mockReviews1);

        // Supplement 2
        mockSupplement2 = new Supplement();
        mockSupplement2.setId(2L);
        mockSupplement2.setName("Creatine Blend");
        mockSupplement2.setBrand("Energize Supps");
        mockSupplement2.setDescription("De beste creatine");
        mockSupplement2.setPrice(44.99);
        mockSupplement2.setStock(20);
        mockSupplement2.setContains("800g");

        Allergen mockAllergen3 = new Allergen();
        mockAllergen3.setId(3L);
        mockAllergen3.setName("Allergen one");

        Allergen mockAllergen4 = new Allergen();
        mockAllergen4.setId(4L);
        mockAllergen4.setName("Allergen two");

        Set<Allergen> mockALlergens2 = new HashSet<>();
        mockALlergens2.add(mockAllergen3);
        mockALlergens2.add(mockAllergen4);

        mockSupplement2.setAllergens(mockALlergens2);

        Review mockReview3 = new Review();
        mockReview3.setId(3L);
        mockReview3.setReview("Goed product");
        mockReview3.setRating(9);
        mockReview3.setSupplement(mockSupplement2);
        mockReview3.setDate(CreateDate.createDate());

        Review mockReview4 = new Review();
        mockReview4.setId(4L);
        mockReview4.setReview("Slecht product");
        mockReview4.setRating(3);
        mockReview4.setSupplement(mockSupplement2);
        mockReview4.setDate(CreateDate.createDate());

        List<Review> mockReviews2 = new ArrayList<>();
        mockReviews2.add(mockReview3);
        mockReviews2.add(mockReview4);

        mockSupplement2.setReviews(mockReviews2);
    }

    @AfterEach
    void tearDown() {
        mockInputDto = null;
        mockSupplement1 = null;
        mockSupplement2 = null;
    }


    @Test
    @DisplayName("Should transfer inputDto to supplement")
    void dtoToSupplement() {
        // Arrange
        // BeforeEach init SupplementInputDto: mockInputDto

        // Act
        Supplement result = SupplementService.dtoToSupplement(mockInputDto);

        // Assert
        assertEquals(mockInputDto.getName(), result.getName());
        assertEquals(mockInputDto.getBrand(), result.getBrand());
        assertEquals(mockInputDto.getDescription(), result.getDescription());
        assertEquals(mockInputDto.getPrice(), result.getPrice());
        assertEquals(mockInputDto.getStock(), result.getStock());
        assertEquals(mockInputDto.getContains(), result.getContains());
        assertNull(result.getAllergens());
    }

    @Test
    @DisplayName("Should transfer supplement to supplementDto")
    void supplementToDto() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1
        Set<ShortAllergenDto> MockShortAllergenDtos = new TreeSet<>(Comparator.comparing(ShortAllergenDto::getId));
        List<ReviewDto> mockReviewDtos = new ArrayList<>();


        for (Allergen mockALlergen : mockSupplement1.getAllergens()) {
            ShortAllergenDto mockShortAllergenDto = AllergenService.allergenToShortDto(mockALlergen);
            MockShortAllergenDtos.add(mockShortAllergenDto);
        }

        for (Review mockReview : mockSupplement1.getReviews()) {
            ReviewDto mockReviewDto = ReviewService.reviewToDto(mockReview);
            mockReviewDtos.add(mockReviewDto);
        }
        mockReviewDtos.sort(Comparator.comparing(ReviewDto::getDate).reversed());

        // Act
        SupplementDto result = SupplementService.supplementToDto(mockSupplement1);


        // Assert
        assertEquals(mockSupplement1.getId(), result.getId());
        assertEquals(mockSupplement1.getName(), result.getName());
        assertEquals(mockSupplement1.getBrand(), result.getBrand());
        assertEquals(mockSupplement1.getDescription(), result.getDescription());
        assertEquals(mockSupplement1.getPrice(), result.getPrice());
        assertEquals(mockSupplement1.getStock(), result.getStock());
        assertEquals(mockSupplement1.getContains(), result.getContains());
        assertEquals(MockShortAllergenDtos.isEmpty(), result.getAllergens().isEmpty());
        if (!MockShortAllergenDtos.isEmpty() && !result.getAllergens().isEmpty()) {
            ShortAllergenDto expectedDto = MockShortAllergenDtos.iterator().next();
            ShortAllergenDto actualDto = result.getAllergens().iterator().next();
            assertEquals(expectedDto.getId(), actualDto.getId());
            assertEquals(expectedDto.getName(), actualDto.getName());
        }
        assertEquals(mockReviewDtos.isEmpty(), result.getReviews().isEmpty());
        if (!mockReviewDtos.isEmpty() && !result.getReviews().isEmpty()) {
            ReviewDto expectedDto = mockReviewDtos.iterator().next();
            ReviewDto actualDto = result.getReviews().iterator().next();
            assertEquals(expectedDto.getId(), actualDto.getId());
        }
    }

    @Test
    @DisplayName("Should transfer supplement to shortSupplementDto")
    void supplementToShortDto() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1

        // Act
        ShortSupplementDto result = SupplementService.supplementToShortDto(mockSupplement1);

        // Assert
        assertEquals(mockSupplement1.getId(), result.getId());
        assertEquals(mockSupplement1.getName(), result.getName());
        assertEquals(mockSupplement1.getBrand(), result.getBrand());
        assertEquals(mockSupplement1.getDescription(), result.getDescription());
        assertEquals(mockSupplement1.getPrice(), result.getPrice());
        assertEquals(mockSupplement1.getStock(), result.getStock());
        assertEquals(mockSupplement1.getContains(), result.getContains());
    }

    @Test
    @DisplayName("Should transfer supplement to orderItemDto")
    void supplementToOrderItemDto() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1

        // Act
        OrderItemDto result = SupplementService.supplementToOrderItemDto(mockSupplement1);

        // Assert
        assertEquals(mockSupplement1.getId(), result.getId());
        assertEquals(mockSupplement1.getName(), result.getName());
        assertEquals(mockSupplement1.getPrice(), result.getPrice());
        assertEquals(1, result.getQuantity());
    }

    @Test
    @DisplayName("Should get all supplements")
    void getAllSupplements_Succes() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1, mockSupplement2
        List<Supplement> mockSupplements = new ArrayList<>();
        mockSupplements.add(mockSupplement1);
        mockSupplements.add(mockSupplement2);
        doReturn(mockSupplements).when(supplementRepository).findAll();

        // Act
        List<SupplementDto> result = supplementService.getAllSupplements();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(mockSupplement1.getName(), result.get(0).getName());
        assertEquals(mockSupplement2.getName(), result.get(1).getName());
    }

    @Test
    @DisplayName("Should throw exception from getAllSupplements method")
    void getAllSupplements_Exception() {
        // Arrange
        doReturn(new ArrayList<>()).when(supplementRepository).findAll();

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class, () -> supplementService.getAllSupplements());

        // Assert
        String expectedMessage = "No supplements are found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Should get supplement by id")
    void getSupplementById_Succes() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1
        Long id = 1L;
        doReturn(Optional.of(mockSupplement1)).when(supplementRepository).findById(id);

        // Act
        SupplementDto result = supplementService.getSupplementById(id);

        // Assert
        assertNotNull(result);
        assertEquals(mockSupplement1.getName(), result.getName());
    }

    @Test
    @DisplayName("Should throw exception from getSupplementById method")
    void getSupplementById_Exception() {
        // Arrange
        Long id = 3L;
        doReturn(Optional.empty()).when(supplementRepository).findById(id);

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> supplementService.getSupplementById(id));

        // Assert
        String expectedMessage = "Supplement with id: 3 not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Should get all supplements by parameters")
    void getSupplementsByParam_Succes() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1, mockSupplement2
        List<Supplement> mockSupplements = new ArrayList<>();
        mockSupplements.add(mockSupplement1);
        mockSupplements.add(mockSupplement2);

        String name = "Crea";
        String brand = "Ener";
        Double price = null;
        Double minPrice = 10.0;
        Double maxPrice = 50.0;
        Integer stock = null;
        Integer minStock = 0;
        Integer maxStock = 30;
        Double averageRating = null;
        Double minRating = null;
        Double maxRating = null;
        String contains = null;

        doReturn(mockSupplements).when(supplementRepository).findAll((Specification<Supplement>) any());

        // Act
        List<SupplementDto> result = supplementService.getSupplementsByParam(
                name, brand, price, minPrice, maxPrice, stock, minStock, maxStock, averageRating, minRating,
                maxRating, contains
        );

        // Assert
        assertEquals(2, result.size(), "Size should be equal");
        assertEquals(0, result.get(0).getStock());
    }

    @Test
    @DisplayName("Should throw exception from getAllSupplementsByParam method")
    void getAllSupplementsByParam_Exception() {
        // Arrange
        String name = "Crea";
        String brand = "Ener";
        Double price = null;
        Double minPrice = 10.0;
        Double maxPrice = 50.0;
        Integer stock = null;
        Integer minStock = 0;
        Integer maxStock = 30;
        Double averageRating = null;
        Double minRating = null;
        Double maxRating = null;
        String contains = null;
        doReturn(new ArrayList<>()).when(supplementRepository).findAll((Specification<Supplement>) any());

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> supplementService.getSupplementsByParam(name, brand, price, minPrice, maxPrice, stock,
                        minStock, maxStock, averageRating, minRating, maxRating, contains));

        // Assert
        String expectedMessage = "No supplements found with the specified filters";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Should get all out of stock supplements")
    void getOutOfStockSupplements_Succes() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1, mockSupplement2
        List<Supplement> mockSupplements = new ArrayList<>();
        mockSupplements.add(mockSupplement1); // Out of stock
        mockSupplements.add(mockSupplement2); // In stock
        doReturn(mockSupplements).when(supplementRepository).findAll();

        // Act
        List<SupplementDto> result = supplementService.getOutOfStockSupplements();

        // Assert
        assertEquals(1, result.size());
        assertEquals(mockSupplement1.getName(), result.get(0).getName());
    }

    @Test
    @DisplayName("Should trow exception from getOutOfStockSupplements method")
    void getOutOfStockSupplements_Exception() {
        doReturn(new ArrayList<>()).when(supplementRepository).findAll();

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> supplementService.getOutOfStockSupplements());

        // Assert
        String expectedMessage = "No supplements out of stock found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Should get all in stock supplements")
    void getInOfStockSupplements_Succes() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1, mockSupplement2
        List<Supplement> mockSupplements = new ArrayList<>();
        mockSupplements.add(mockSupplement1); // Out of stock
        mockSupplements.add(mockSupplement2); // In stock
        doReturn(mockSupplements).when(supplementRepository).findAll();

        // Act
        List<SupplementDto> result = supplementService.getInStockSupplements();

        // Assert
        assertEquals(1, result.size());
        assertEquals(mockSupplement2.getName(), result.get(0).getName());
    }

    @Test
    @DisplayName("Should trow exception from getInStockSupplements method")
    void getInStockSupplements_Exception() {
        doReturn(new ArrayList<>()).when(supplementRepository).findAll();

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> supplementService.getInStockSupplements());

        // Assert
        String expectedMessage = "No supplements in stock found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Should create new not existing supplement")
    void createSupplement_Succes() {
        // Arrange
        // BeforeEach init SupplementInputDto: mockInputDto
        mockInputDto.setName("New Supplement");
        doReturn(false).when(supplementRepository).existsByNameIgnoreCase(mockInputDto.getName());

        // Act
        SupplementDto result = supplementService.createSupplement(mockInputDto);

        // Assert
        assertNotNull(result);
        assertEquals(mockInputDto.getName(), result.getName());
    }

    @Test
    @DisplayName("Should throw exception from createSupplement method")
    void createSupplement_Exception() {
        // Arrange
        // BeforeEach init SupplementInputDto: mockInputDto
        mockInputDto.setName("Existing Supplement");
        doReturn(true).when(supplementRepository).existsByNameIgnoreCase(mockInputDto.getName());

        // Act
        Exception exception = assertThrows(InvalidInputException.class,
                () -> supplementService.createSupplement(mockInputDto));

        // Assert
        String expectedMessage = "Supplement with name: " + mockInputDto.getName() + " already exists.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
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