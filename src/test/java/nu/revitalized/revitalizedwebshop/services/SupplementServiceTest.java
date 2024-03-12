package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.SupplementInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.SupplementPatchInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.*;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
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

    @InjectMocks
    private SupplementService supplementService;

    SupplementInputDto mockInputDto;
    Supplement mockSupplement1;
    Supplement mockSupplement2;
    Allergen mockAllergen1;
    Allergen mockAllergen2;
    Allergen mockAllergen3;
    Allergen mockAllergen4;
    Set<Allergen> mockAllergens1;
    Set<Allergen> mockAllergens2;
    Review mockReview1;
    Review mockReview2;
    Review mockReview3;
    Review mockReview4;
    Set<Review> mockReviews1;
    Set<Review> mockReviews2;


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

        // Allergen 1, 2, 3, 4
        Allergen mockAllergen1 = new Allergen();
        Allergen mockAllergen2 = new Allergen();
        Allergen mockAllergen3 = new Allergen();
        Allergen mockAllergen4 = new Allergen();
        mockAllergen1.setId(1L);
        mockAllergen2.setId(2L);
        mockAllergen3.setId(3L);
        mockAllergen4.setId(4L);
        mockAllergen1.setName("Allergen one");
        mockAllergen2.setName("Allergen two");
        mockAllergen3.setName("Allergen three");
        mockAllergen4.setName("Allergen four");

        // Allergens 1, 2
        Set<Allergen> mockAllergens1 = new HashSet<>();
        Set<Allergen> mockALlergens2 = new HashSet<>();
        mockAllergens1.add(mockAllergen1);
        mockAllergens1.add(mockAllergen2);
        mockALlergens2.add(mockAllergen3);
        mockALlergens2.add(mockAllergen4);

        // Review 1, 2, 3, 4
        Review mockReview1 = new Review();
        Review mockReview2 = new Review();
        Review mockReview3 = new Review();
        Review mockReview4 = new Review();
        mockReview1.setId(1L);
        mockReview2.setId(2L);
        mockReview3.setId(3L);
        mockReview4.setId(4L);
        mockReview1.setReview("Goed product");
        mockReview2.setReview("Slecht product");
        mockReview3.setReview("Goed product");
        mockReview4.setReview("Slecht product");
        mockReview1.setRating(10);
        mockReview2.setRating(1);
        mockReview3.setRating(9);
        mockReview4.setRating(3);
        mockReview1.setSupplement(mockSupplement1);
        mockReview2.setSupplement(mockSupplement1);
        mockReview3.setSupplement(mockSupplement2);
        mockReview4.setSupplement(mockSupplement2);
        mockReview1.setDate(CreateDate.createDate());
        mockReview2.setDate(CreateDate.createDate());
        mockReview3.setDate(CreateDate.createDate());
        mockReview4.setDate(CreateDate.createDate());

        // Reviews 1, 2
        List<Review> mockReviews1 = new ArrayList<>();
        List<Review> mockReviews2 = new ArrayList<>();
        mockReviews1.add(mockReview1);
        mockReviews1.add(mockReview2);
        mockReviews2.add(mockReview3);
        mockReviews2.add(mockReview4);

        // Supplement 1
        mockSupplement1 = new Supplement();
        mockSupplement1.setId(1L);
        mockSupplement1.setName("Creatine");
        mockSupplement1.setBrand("Energize Supps");
        mockSupplement1.setDescription("Creatine van energize is top");
        mockSupplement1.setPrice(26.99);
        mockSupplement1.setStock(0);
        mockSupplement1.setContains("500g");
        mockSupplement1.setAllergens(mockAllergens1);
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
        mockSupplement2.setAllergens(mockALlergens2);
        mockSupplement2.setReviews(mockReviews2);
    }

    @AfterEach
    void tearDown() {
        mockInputDto = null;
        mockSupplement1 = null;
        mockSupplement2 = null;
        mockAllergen1 = null;
        mockAllergen2 = null;
        mockAllergen3 = null;
        mockAllergen4 = null;
        mockAllergens1 = null;
        mockAllergens2 = null;
        mockReview1 = null;
        mockReview2 = null;
        mockReview3 = null;
        mockReview4 = null;
        mockReviews1 = null;
        mockReviews2 = null;
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
    @DisplayName("Should throw exception from getAllSupplements method when not found")
    void getAllSupplements_Exception_WhenNotFound() {
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
    @DisplayName("Should throw exception from getSupplementById method when not found")
    void getSupplementById_Exception_WhenNotFound() {
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
    @DisplayName("Should get all supplements without specified parameters")
    void getSupplementsByParam_Succes_WithoutParameters() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1, mockSupplement2
        List<Supplement> mockSupplements = new ArrayList<>();
        mockSupplements.add(mockSupplement1);
        mockSupplements.add(mockSupplement2);

        doReturn(mockSupplements).when(supplementRepository).findAll((Specification<Supplement>) any());

        // Act
        List<SupplementDto> result = supplementService.getSupplementsByParam(
                null, null, null, null, null, null, null, null, null, null, null, null);

        // Arrange
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should get all supplements with specified parameters")
    void getSupplementsByParam_Succes_WithParameters() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1, mockSupplement2
        List<Supplement> mockSupplements = new ArrayList<>();
        mockSupplements.add(mockSupplement1);
        mockSupplements.add(mockSupplement2);

        String name = "Crea";
        String brand = "Ener";
        Double minPrice = 10.0;
        Double maxPrice = 50.0;
        Integer minStock = 0;
        Integer maxStock = 30;

        doReturn(mockSupplements).when(supplementRepository).findAll((Specification<Supplement>) any());

        // Act
        List<SupplementDto> result = supplementService.getSupplementsByParam(
                name, brand, null, minPrice, maxPrice, null, minStock, maxStock, null, null,
                null, null
        );

        // Assert
        assertEquals(2, result.size());
        assertEquals(0, result.get(0).getStock());
    }

    @Test
    @DisplayName("Should throw exception from getAllSupplementsByParam method when not found")
    void getAllSupplementsByParam_Exception_WhenNotFound() {
        // Arrange
        String name = "Crea";
        String brand = "Ener";
        Double minPrice = 10.0;
        Double maxPrice = 50.0;
        Integer minStock = 0;
        Integer maxStock = 30;

        doReturn(new ArrayList<>()).when(supplementRepository).findAll((Specification<Supplement>) any());

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> supplementService.getSupplementsByParam(name, brand, null, minPrice, maxPrice, null,
                        minStock, maxStock, null, null, null, null));

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
    @DisplayName("Should trow exception from getOutOfStockSupplements method when not found")
    void getOutOfStockSupplements_Exception_WhenNotFound() {
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
    @DisplayName("Should trow exception from getInStockSupplements method when not found")
    void getInStockSupplements_Exception_WhenNotFound() {
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
    @DisplayName("Should throw exception from createSupplement method when already exists")
    void createSupplement_Exception_WhenAlreadyExists() {
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

    @Test
    @DisplayName("Should update existing supplement")
    void updateSupplement_Succes() {
        // Arrange
        // BeforeEach init SupplementInputDto: mockInputDto, Supplement: mockSupplement1
        Long id = 10L;
        mockSupplement1.setId(10L);

        doReturn(Optional.of(mockSupplement1)).when(supplementRepository).findById(id);
        doAnswer(invocation -> invocation.getArgument(0)).when(supplementRepository).save(any(Supplement.class));

        // Act
        SupplementDto result = supplementService.updateSupplement(id, mockInputDto);

        // Assert
        assertNotNull(result);
        assertEquals(mockSupplement1.getName(), result.getName());
        assertEquals(mockSupplement1.getStock(), result.getStock());
    }

    @Test
    @DisplayName("Should throw exception from updateSupplement method when not found")
    void updateSupplement_Exception_WhenNotFound() {
        // Arrange
        // BeforeEach init SupplementInputDto: mockInputDto
        Long id = 20L;
        doReturn(Optional.empty()).when(supplementRepository).findById(id);

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> supplementService.updateSupplement(id, mockInputDto));

        // Assert
        String expectedMessage = "Supplement with id: " + id + " not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Should patch exisiting supplement")
    void patchSupplement_Succes() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1
        Long id = 20L;
        SupplementPatchInputDto mockPatchInputDto = new SupplementPatchInputDto();
        mockPatchInputDto.setName("Patched name");
        mockPatchInputDto.setBrand("Patched brand");
        mockPatchInputDto.setDescription("Patched description");
        mockPatchInputDto.setPrice(200.99);
        mockPatchInputDto.setStock(100);
        mockPatchInputDto.setContains("Patched contains");

        doReturn(Optional.of(mockSupplement1)).when(supplementRepository).findById(id);
        doAnswer(invocation -> invocation.getArgument(0)).when(supplementRepository).save(any(Supplement.class));

        // Act
        SupplementDto result = supplementService.patchSupplement(id, mockPatchInputDto);

        // Assert
        assertNotNull(result);
        assertEquals(mockPatchInputDto.getName(), result.getName());
        assertEquals(mockPatchInputDto.getBrand(), result.getBrand());
        assertEquals(mockPatchInputDto.getDescription(), result.getDescription());
        assertEquals(mockPatchInputDto.getPrice(), result.getPrice());
        assertEquals(mockPatchInputDto.getStock(), result.getStock());
        assertEquals(mockPatchInputDto.getContains(), result.getContains());
    }

    @Test
    @DisplayName("Should throw exception from patchSupplement method when not found")
    void patchSupplement_Exception_WhenNotFound() {
        // Arrange
        Long id = 20L;
        SupplementPatchInputDto mockPatchInputDto = new SupplementPatchInputDto();
        doReturn(Optional.empty()).when(supplementRepository).findById(id);

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> supplementService.patchSupplement(id, mockPatchInputDto));

        // Assert
        String expectedMessage = "Supplement with id: " + id + " not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Should delete existing supplement")
    void deleteSupplement_Succes() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1
        Long id = 13L;
        mockSupplement1.setId(13L);
        String mockSupplementName = mockSupplement1.getName();

        doReturn(Optional.of(mockSupplement1)).when(supplementRepository).findById(id);

        // Act
        String confirmation = supplementService.deleteSupplement(id);

        // Assert
        String expectedMessage = "Supplement: " + mockSupplementName + " with id: " + id + " is removed";

        assertNotNull(confirmation);
        assertEquals(expectedMessage, confirmation);
        verify(supplementRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception from deleteSupplement method when not found")
    void deleteSupplement_Exception_WhenNotFound() {
        // Arrange
        Long id = 13L;
        doReturn(Optional.empty()).when(supplementRepository).findById(id);

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> supplementService.deleteSupplement(id));

        // Assert
        String expectedMessage = "Supplement with id: " + id + " not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(supplementRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should assign unique allergen to supplement")
    void assignAllergenToSupplement_Succes() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1
        Long supplementId = 5L;
        Long allergenId = 5L;
        mockSupplement1.setId(supplementId);
        Allergen mockAllergen = new Allergen();
        mockAllergen.setId(allergenId);
        mockAllergen.setName("New allergen");

        doReturn(Optional.of(mockSupplement1)).when(supplementRepository).findById(supplementId);
        doReturn(Optional.of(mockAllergen)).when(allergenRepository).findById(allergenId);
        doAnswer(invocation -> invocation.getArgument(0)).when(supplementRepository).save(any(Supplement.class));

        // Act
        SupplementDto result = supplementService.assignAllergenToSupplement(supplementId, allergenId);

        // Assert
        assertNotNull(result);
        assertTrue(result.getAllergens().contains(AllergenService.allergenToShortDto(mockAllergen)));
        verify(supplementRepository, times(1)).save(any(Supplement.class));
    }

    @Test
    @DisplayName("Should throw exception from assignAllergenToSupplement method when supplement not found")
    void assignAllergenToSupplement_Exception_WhenSupplementNotFound() {
        // Arrange
        Long supplementId = 5L;
        Long allergenId = 5L;
        doReturn(Optional.empty()).when(supplementRepository).findById(supplementId);

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> supplementService.assignAllergenToSupplement(supplementId, allergenId));

        // Assert
        String expectedMessage = "Supplement with id: " + supplementId + " not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(allergenRepository, never()).findById(anyLong());
        verify(supplementRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception from assignAllergenToSupplement method when allergen not found")
    void assignAllergenToSupplement_Exception_WhenAllergenNotFound() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1
        Long supplementId = 5L;
        Long allergenId = 5L;
        mockSupplement1.setId(supplementId);

        doReturn(Optional.of(mockSupplement1)).when(supplementRepository).findById(supplementId);
        doReturn(Optional.empty()).when(allergenRepository).findById(allergenId);

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> supplementService.assignAllergenToSupplement(supplementId, allergenId));

        // Assert
        String expectedMessage = "Allergen with id: " + allergenId + " not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(supplementRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception from assignAllergenToSupplement method when already contains allergen")
    void assignAllergenToSupplement_Exception_WhenAlreadyContainsAllergen() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1
        Long supplementId = 5L;
        Long allergenId = 5L;
        Allergen mockAllergen = new Allergen();
        mockAllergen.setId(allergenId);
        mockAllergen.setName("Existing Allergen");
        Set<Allergen> mockAllergens = new HashSet<>();
        mockAllergens.add(mockAllergen);

        mockSupplement1.setId(supplementId);
        mockSupplement1.setAllergens(mockAllergens);

        doReturn(Optional.of(mockSupplement1)).when(supplementRepository).findById(supplementId);
        doReturn(Optional.of(mockAllergen)).when(allergenRepository).findById(allergenId);

        // Act
        Exception exception = assertThrows(BadRequestException.class,
                () -> supplementService.assignAllergenToSupplement(supplementId, allergenId));

        // Assert
        String expectedMessage = "Supplement already contains allergen: " + mockAllergen.getName()
                + " with id: " + allergenId;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(supplementRepository, never()).save(any());
    }


    @Test
    void removeAllergenFromSupplement_Succes() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1
        Long supplementId = 5L;
        Long allergenId = 5L;
        Allergen mockAllergen = new Allergen();
        mockAllergen.setName("New allergen");
        mockAllergen.setId(allergenId);
        Set<Allergen> mockAllergens = new HashSet<>();
        mockAllergens.add(mockAllergen);

        mockSupplement1.setId(supplementId);
        mockSupplement1.setAllergens(mockAllergens);

        doReturn(Optional.of(mockSupplement1)).when(supplementRepository).findById(supplementId);
        doReturn(Optional.of(mockAllergen)).when(allergenRepository).findById(allergenId);
        doAnswer(invocation -> invocation.getArgument(0)).when(supplementRepository).save(any(Supplement.class));

        // Act
        SupplementDto result = supplementService.removeAllergenFromSupplement(supplementId, allergenId);

        // Assert
        assertNotNull(result);
        assertFalse(result.getAllergens().contains(AllergenService.allergenToShortDto(mockAllergen)));
        verify(supplementRepository, times(1)).save(any(Supplement.class));
    }

    @Test
    void removeAllergenFromSupplement_Exception_WhenSupplementNotFound() {
        // Arrange
        Long supplementId = 66L;
        Long allergenId = 66L;

        doReturn(Optional.empty()).when(supplementRepository).findById(supplementId);

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> supplementService.removeAllergenFromSupplement(supplementId, allergenId));

        // Assert
        String expectedMessage = "Supplement with id: " + supplementId + " not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(allergenRepository, never()).findById(anyLong());
        verify(supplementRepository, never()).save(any());
    }

    @Test
    void removeAllergenFromSupplement_Exception_WhenAllergenNotFound() {
        // Arrange
        // BeforeEach init Supplement: mockSupplement1
        Long supplementId = 66L;
        Long allergenId = 66L;
        mockSupplement1.setId(supplementId);

        doReturn(Optional.of(mockSupplement1)).when(supplementRepository).findById(supplementId);
        doReturn(Optional.empty()).when(allergenRepository).findById(allergenId);

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> supplementService.removeAllergenFromSupplement(supplementId, allergenId));

        // Assert
        String expectedMessage = "Allergen with id: " + allergenId + " not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(supplementRepository, never()).save(any());
    }

    @Test
    void removeAllergenFromSupplement_Exception_WhenNotContainsAllergen() {
        // Arrange
        Long supplementId = 66L;
        Long allergenId = 66L;
        Supplement mockSupplement = new Supplement();
        mockSupplement.setId(supplementId);
        Allergen mockAllergen = new Allergen();
        mockAllergen.setId(allergenId);
        mockAllergen.setName("Not Existing Allergen");

        doReturn(Optional.of(mockSupplement1)).when(supplementRepository).findById(supplementId);
        doReturn(Optional.of(mockAllergen)).when(allergenRepository).findById(allergenId);

        // Act
        Exception exception = assertThrows(BadRequestException.class,
                () -> supplementService.removeAllergenFromSupplement(supplementId, allergenId));

        // Assert
        String expectedMessage = "Supplement doesn't contain allergen: " + mockAllergen.getName()
                + " with id: " + allergenId;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(supplementRepository, never()).save(any());
    }
}