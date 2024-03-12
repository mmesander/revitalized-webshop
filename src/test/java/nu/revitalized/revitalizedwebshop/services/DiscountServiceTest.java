package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.DiscountInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.DiscountDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShortDiscountDto;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Discount;
import nu.revitalized.revitalizedwebshop.models.User;
import nu.revitalized.revitalizedwebshop.repositories.DiscountRepository;
import nu.revitalized.revitalizedwebshop.repositories.UserRepository;
import nu.revitalized.revitalizedwebshop.specifications.DiscountSpecification;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private DiscountService discountService;

    DiscountInputDto mockInputDto;
    Discount mockDiscount1;
    Discount mockDiscount2;
    User mockUser1;
    User mockUser2;

    @BeforeEach
    void init() {
        // Discount InputDto
        mockInputDto = new DiscountInputDto();
        mockInputDto.setName("TestDiscount50");
        mockInputDto.setValue(50.0);

        // Discount 1
        mockDiscount1 = new Discount();
        mockDiscount1.setId(1L);
        mockDiscount1.setName("TestDiscount75");
        mockDiscount1.setValue(75.0);

        // Discount 2
        mockDiscount2 = new Discount();
        mockDiscount2.setId(2L);
        mockDiscount2.setName("TestDiscount100");
        mockDiscount2.setValue(100.0);

        // User 1
        mockUser1 = new User();
        mockUser1.setUsername("Testuser One");

        mockUser2 = new User();
        mockUser2.setUsername("Testuser Two");
    }

    @AfterEach
    void tearDown() {
        mockInputDto = null;
        mockDiscount1 = null;
        mockDiscount2 = null;
        mockUser1 = null;
        mockUser2 = null;
    }

    @Test
    @DisplayName("Should transfer inputDto to discount")
    void dtoToDiscount() {
        // Arrange
        // BeforeEach init DiscountInputDto: mockInputDto

        // Act
        Discount result = DiscountService.dtoToDiscount(mockInputDto);

        // Assert
        assertNotNull(result);
        assertEquals(mockInputDto.getName(), result.getName());
        assertEquals(mockInputDto.getValue(), result.getValue());
    }

    @Test
    @DisplayName("Should transfer discount to discountDto")
    void discountToDto() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1

        // Act
        DiscountDto result = DiscountService.discountToDto(mockDiscount1);

        // Assert
        assertNotNull(result);
        assertEquals(mockDiscount1.getId(), result.getId());
        assertEquals(mockDiscount1.getName(), result.getName());
        assertEquals(mockDiscount1.getValue(), result.getValue());
    }

    @Test
    @DisplayName("Should transfer discount to discountShortDto")
    void discountToShortDto() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1

        // Act
        ShortDiscountDto result = DiscountService.discountToShortDto(mockDiscount1);

        // Assert
        assertNotNull(result);
        assertEquals(mockDiscount1.getName(), result.getName());
        assertEquals(mockDiscount1.getValue(), result.getValue());
    }

    @Test
    @DisplayName("Should get all discounts")
    void getAllDiscounts_Succes() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, mockDiscount2
        List<Discount> mockDiscounts = new ArrayList<>();
        mockDiscounts.add(mockDiscount1);
        mockDiscounts.add(mockDiscount2);
        doReturn(mockDiscounts).when(discountRepository).findAll();

        // Act
        List<DiscountDto> result = discountService.getAllDiscounts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockDiscount1.getId(), result.get(0).getId());
        assertEquals(mockDiscount1.getName(), result.get(0).getName());
        assertEquals(mockDiscount1.getValue(), result.get(0).getValue());
        assertEquals(mockDiscount2.getId(), result.get(1).getId());
        assertEquals(mockDiscount2.getName(), result.get(1).getName());
        assertEquals(mockDiscount2.getValue(), result.get(1).getValue());
    }
    @Test
    @DisplayName("Should throw exception for getAllDiscounts method when not found")
    void getAllDiscounts_Exception_WhenNotFound() {
        // Arrange
        doReturn(new ArrayList<>()).when(discountRepository).findAll();

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> discountService.getAllDiscounts());

        // Assert
        String expectedMessage = "No discounts found";
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    @DisplayName("Should get discount by id")
    void getDiscountById_Succes() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1
        Long id = 1L;
        doReturn(Optional.of(mockDiscount1)).when(discountRepository).findById(id);

        // Act
        DiscountDto result = discountService.getDiscountById(id);

        // Assert
        assertNotNull(result);
        assertEquals(mockDiscount1.getId(), result.getId());
        assertEquals(mockDiscount1.getName(), result.getName());
        assertEquals(mockDiscount1.getValue(), result.getValue());
    }

    @Test
    @DisplayName("Should throw exception from getDiscountById method when not found")
    void getDiscountById_Exception_WhenNotFound() {
        // Arrange
        Long id = 3L;
        doReturn(Optional.empty()).when(discountRepository).findById(id);

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> discountService.getDiscountById(id));

        // Assert
        String expectedMessage = "Discount with id: " + id + " not found";
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Should get all discounts without specified parameters")
    void getAllDiscountsByParam_Succes_WithoutParameters() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, mockDiscount2
        List<Discount> mockDiscounts = new ArrayList<>();
        mockDiscounts.add(mockDiscount1);
        mockDiscounts.add(mockDiscount2);
        doReturn(mockDiscounts).when(discountRepository).findAll(any(DiscountSpecification.class));

        // Act
        List<DiscountDto> result = discountService.getAllDiscountsByParam(
                null, null, null, null);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should get all discounts with specified parameters")
    void getAllDiscountsByParam_Succes_WithParameters() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, mockDiscount2
        List<Discount> mockDiscounts = new ArrayList<>();
        mockDiscounts.add(mockDiscount1);
        mockDiscounts.add(mockDiscount2);
        doReturn(mockDiscounts).when(discountRepository).findAll(any(DiscountSpecification.class));

        // Act
        List<DiscountDto> result = discountService.getAllDiscountsByParam(
                null, null, 75.0, null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should throw exception from getAllDiscountsByParam method when not found")
    void getAllDiscountsByParam_Exception_WhenNotFound() {
        // Arrange
        doReturn(new ArrayList<>()).when(discountRepository).findAll(any(DiscountSpecification.class));

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> discountService.getAllDiscountsByParam(
                        null, null, null, null));

        // Assert
        String expectedMessage = "No discounts found with the specified filters";
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getAllActiveDiscounts_Succes() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, mockDiscount2, User: mockUser1
        Set<User> mockDiscountUsers = new HashSet<>();
        mockDiscountUsers.add(mockUser1);

        mockDiscount1.setUsers(mockDiscountUsers);

        List<Discount> mockDiscounts = new ArrayList<>();
        mockDiscounts.add(mockDiscount1);
        mockDiscounts.add(mockDiscount2);

        doReturn(mockDiscounts).when(discountRepository).findAll();

        // Act
        List<DiscountDto> result = discountService.getAllActiveDiscounts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockDiscount1.getName(), result.get(0).getName());
    }

    @Test
    @DisplayName("Should throw exception from")
    void getAllActiveDiscounts_Exception_WhenNotFound() {
        // Arrange
        doReturn(new ArrayList<>()).when(discountRepository).findAll();

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> discountService.getAllActiveDiscounts());

        // Assert
        String expectedMessage = "No active discounts found";
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getAllInactiveDiscounts_Succes() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, mockDiscount2, User: mockUser1
        Set<User> mockDiscountUsers = new HashSet<>();
        mockDiscountUsers.add(mockUser1);

        mockDiscount1.setUsers(mockDiscountUsers);

        List<Discount> mockDiscounts = new ArrayList<>();
        mockDiscounts.add(mockDiscount1);
        mockDiscounts.add(mockDiscount2);

        doReturn(mockDiscounts).when(discountRepository).findAll();

        // Act
        List<DiscountDto> result = discountService.getAllInactiveDiscounts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockDiscount2.getName(), result.get(0).getName());
    }

    @Test
    @DisplayName("Should throw exception from")
    void getAllInactiveDiscounts_Exception_WhenNotFound() {
        // Arrange
        doReturn(new ArrayList<>()).when(discountRepository).findAll();

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> discountService.getAllInactiveDiscounts());

        // Assert
        String expectedMessage = "No inactive discounts found";
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void createDiscount_Succes() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    void updateDiscount_Succes() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @DisplayName("Should throw exception from")
    void updateDiscount_Exception_WhenNotFound() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    void patchDiscount_Succes() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @DisplayName("Should throw exception from")
    void patchDiscount_Exception_WhenNotFound() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    void deleteDiscount_Succes() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @DisplayName("Should throw exception from")
    void deleteDiscount_Exception_WhenNotFound() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    void assignUserToDiscount_Succes() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @DisplayName("Should throw exception from")
    void assignUserToDiscount_Exception_WhenDiscountNotFound() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @DisplayName("Should throw exception from")
    void assignUserToDiscount_Exception_WhenUserNotFound() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @DisplayName("Should throw exception from")
    void assignUserToDiscount_Exception_WhenAlreadyContainsUser() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    void removeUserFromDiscount_Succes() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @DisplayName("Should throw exception from")
    void removeUserFromDiscount_Exception_WhenDiscountNotFound() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @DisplayName("Should throw exception from")
    void removeUserFromDiscount_Exception_WhenUserNotFound() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @DisplayName("Should throw exception from")
    void removeUserFromDiscount_Exception_WhenNotContainsUser() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    void getAllAuthUserDiscounts_Succes() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @DisplayName("Should throw exception from")
    void getAllAuthUserDiscounts_Exception_WhenUserNotFound() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @DisplayName("Should throw exception from")
    void getAllAuthUserDiscounts_Exception_WhenDiscountsNotFound() {
        // Arrange

        // Act

        // Assert
    }
}