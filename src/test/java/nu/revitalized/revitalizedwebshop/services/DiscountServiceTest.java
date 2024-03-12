package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.DiscountInputDto;
import nu.revitalized.revitalizedwebshop.dtos.input.DiscountPatchInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.DiscountDto;
import nu.revitalized.revitalizedwebshop.dtos.output.ShortDiscountDto;
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.exceptions.UsernameNotFoundException;
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
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    @DisplayName("Should get all active discounts")
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
    @DisplayName("Should throw exception from getAllActiveDiscounts method when not found")
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
    @DisplayName("Should get all inactive discounts")
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
    @DisplayName("Should throw exception from getAllInactiveDiscounts method when not found")
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
    @DisplayName("Should create new discount")
    void createDiscount_Succes() {
        // Arrange
        // BeforeEach init DiscountInputDto: mockInputDto

        // Act
        DiscountDto result = discountService.createDiscount(mockInputDto);

        // Assert
        assertNotNull(result);
        assertEquals(mockInputDto.getName(), result.getName());
    }

    @Test
    @DisplayName("Should update existing discount")
    void updateDiscount_Succes() {
        // Arrange
        // BeforeEach init DiscountInputDto: mockInputDto, Discount: mockDiscount1
        Long id = 10L;
        mockDiscount1.setId(10L);

        doReturn(Optional.of(mockDiscount1)).when(discountRepository).findById(id);
        doAnswer(invocation -> invocation.getArgument(0)).when(discountRepository).save(any(Discount.class));

        // Act
        DiscountDto result = discountService.updateDiscount(id, mockInputDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(mockInputDto.getName(), result.getName());
    }

    @Test
    @DisplayName("Should throw exception from updateDiscount method when not found")
    void updateDiscount_Exception_WhenNotFound() {
        // Arrange
        // BeforeEach init DiscountInputDto: mockInputDto
        Long id = 20L;
        doReturn(Optional.empty()).when(discountRepository).findById(id);

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> discountService.updateDiscount(id, mockInputDto));

        // Assert
        String expectedMessage = "Discount with id: " + id + " not found";
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Should patch existing discount")
    void patchDiscount_Succes() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1
        Long id = 20L;
        mockDiscount1.setId(id);
        DiscountPatchInputDto mockPatchInputDto = new DiscountPatchInputDto();
        mockPatchInputDto.setName("PatchedDiscount");
        mockPatchInputDto.setValue(50.25);

        doReturn(Optional.of(mockDiscount1)).when(discountRepository).findById(id);
        doAnswer(invocation -> invocation.getArgument(0)).when(discountRepository).save(any(Discount.class));

        // Act
        DiscountDto result = discountService.patchDiscount(id, mockPatchInputDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(mockPatchInputDto.getName(), result.getName());
        assertEquals(mockPatchInputDto.getValue(), result.getValue());
    }

    @Test
    @DisplayName("Should throw exception from patchDiscount method when not found")
    void patchDiscount_Exception_WhenNotFound() {
        // Arrange
        Long id = 20L;
        DiscountPatchInputDto mockPatchInputDto = new DiscountPatchInputDto();
        doReturn(Optional.empty()).when(discountRepository).findById(id);

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> discountService.patchDiscount(id, mockPatchInputDto));

        // Assert
        String expectedMessage = "Discount with id: " + id + " not found";
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Should delete existing discount")
    void deleteDiscount_Succes() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1
        Long id = 13L;
        mockDiscount1.setId(13L);
        String mockDiscountName = mockDiscount1.getName();
        doReturn(Optional.of(mockDiscount1)).when(discountRepository).findById(id);

        // Act
        String confirmation = discountService.deleteDiscount(id);

        // Assert
        String expectedMessage = "Discount: " + mockDiscountName + " with id: " + id + " is removed";

        assertNotNull(confirmation);
        assertEquals(expectedMessage, confirmation);
        verify(discountRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception from deleteDiscount method when not found")
    void deleteDiscount_Exception_WhenNotFound() {
        // Arrange
        Long id = 13L;
        doReturn(Optional.empty()).when(discountRepository).findById(id);

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> discountService.deleteDiscount(id));

        // Assert
        String expectedMessage = "Discount with id: " + id + " not found";
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
        verify(discountRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should throw exception from deleteDiscount method when contains users")
    void deleteDiscount_Exception_WhenContainsUser() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, User: mockUser1
        Set<User> mockDiscountUsers = new HashSet<>();
        Long id = 66L;
        mockDiscountUsers.add(mockUser1);
        mockDiscount1.setId(id);
        mockDiscount1.setUsers(mockDiscountUsers);
        doReturn(Optional.of(mockDiscount1)).when(discountRepository).findById(id);

        // Act
        Exception exception = assertThrows(BadRequestException.class,
                () -> discountService.deleteDiscount(id));

        // Assert
        String expectedMessage = "Can't remove an active discount, remove all users from discount first";
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
        verify(discountRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should assign existing discount to existing user")
    void assignUserToDiscount_Succes() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, User: mockUser1
        Long discountId = 5L;
        String username = mockUser1.getUsername();
        mockDiscount1.setId(discountId);

        doReturn(Optional.of(mockDiscount1)).when(discountRepository).findById(discountId);
        doReturn(Optional.of(mockUser1)).when(userRepository).findById(username);
        doAnswer(invocation -> invocation.getArgument(0)).when(discountRepository).save(any(Discount.class));

        // Act
        DiscountDto result = discountService.assignUserToDiscount(username, discountId);

        // Assert
        assertNotNull(result);
        assertTrue(result.getUsers().contains(username));
        verify(discountRepository, times(1)).save(any(Discount.class));
    }

    @Test
    @DisplayName("Should throw exception from assignUserToDiscount method when discount not found")
    void assignUserToDiscount_Exception_WhenDiscountNotFound() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, User: mockUser1
        Long discountId = 5L;
        String username = mockUser1.getUsername();
        doReturn(Optional.empty()).when(discountRepository).findById(discountId);


        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> discountService.assignUserToDiscount(username, discountId));

        // Assert
        String expectedMessage = "Discount with id: " + discountId + " not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, never()).findById(anyString());
        verify(discountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception from assignUserToDiscount method when user not found")
    void assignUserToDiscount_Exception_WhenUserNotFound() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, User: mockUser1
        Long discountId = 5L;
        String username = mockUser1.getUsername();
        mockDiscount1.setId(discountId);

        doReturn(Optional.of(mockDiscount1)).when(discountRepository).findById(discountId);
        doReturn(Optional.empty()).when(userRepository).findById(username);

        // Act
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> discountService.assignUserToDiscount(username, discountId));

        // Assert
        String expectedMessage = "Can't find user: " + username;
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
        verify(discountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception from assignUserToDiscount method when already contains user")
    void assignUserToDiscount_Exception_WhenAlreadyContainsUser() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, User: mockUser1
        Long discountId = 5L;
        String username = mockUser1.getUsername();
        mockDiscount1.setId(discountId);
        Set<User> mockUsers = new HashSet<>();
        Set<Discount> mockDiscounts = new HashSet<>();
        mockUsers.add(mockUser1);
        mockDiscounts.add(mockDiscount1);
        mockDiscount1.setUsers(mockUsers);
        mockUser1.setDiscounts(mockDiscounts);

        doReturn(Optional.of(mockDiscount1)).when(discountRepository).findById(discountId);
        doReturn(Optional.of(mockUser1)).when(userRepository).findById(username);

        // Act
        Exception exception = assertThrows(BadRequestException.class,
                () -> discountService.assignUserToDiscount(username, discountId));

        // Assert
        String expectedMessage = "User: " + username + " already has discount: " + mockDiscount1.getName();
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
        verify(discountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should remove existing user from existing discount")
    void removeUserFromDiscount_Succes() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, User: mockUser1
        Long discountId = 5L;
        String username = mockUser1.getUsername();
        Set<User> mockUsers = new HashSet<>();
        mockUsers.add(mockUser1);

        mockDiscount1.setId(discountId);
        mockDiscount1.setUsers(mockUsers);

        doReturn(Optional.of(mockDiscount1)).when(discountRepository).findById(discountId);
        doReturn(Optional.of(mockUser1)).when(userRepository).findById(username);
        doAnswer(invocation -> invocation.getArgument(0)).when(discountRepository).save(any(Discount.class));

        // Act
        String confirmation = discountService.removeUserFromDiscount(username, discountId);

        // Assert
        String expectedMessage = "Discount: " + mockDiscount1.getName() + " with id: " + discountId
                + " is removed from user: " + username;

        assertNotNull(confirmation);
        assertEquals(expectedMessage, confirmation);
        assertFalse(mockDiscount1.getUsers().contains(mockUser1));
    }

    @Test
    @DisplayName("Should throw exception from removeUserFromDiscount method when discount not found")
    void removeUserFromDiscount_Exception_WhenDiscountNotFound() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, User: mockUser1
        Long discountId = 5L;
        String username = mockUser1.getUsername();
        doReturn(Optional.empty()).when(discountRepository).findById(discountId);


        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> discountService.removeUserFromDiscount(username, discountId));

        // Assert
        String expectedMessage = "Discount with id: " + discountId + " not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(userRepository, never()).findById(anyString());
        verify(discountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception from removeUserFromDiscount method when user not found")
    void removeUserFromDiscount_Exception_WhenUserNotFound() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, User: mockUser1
        Long discountId = 5L;
        String username = mockUser1.getUsername();
        mockDiscount1.setId(discountId);

        doReturn(Optional.of(mockDiscount1)).when(discountRepository).findById(discountId);
        doReturn(Optional.empty()).when(userRepository).findById(username);

        // Act
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> discountService.assignUserToDiscount(username, discountId));

        // Assert
        String expectedMessage = "Can't find user: " + username;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(discountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception from removeUserFromDiscount method when not contains user")
    void removeUserFromDiscount_Exception_WhenNotContainsUser() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, User: mockUser1
        Long discountId = 5L;
        String username = mockUser1.getUsername();
        mockDiscount1.setId(discountId);

        doReturn(Optional.of(mockDiscount1)).when(discountRepository).findById(discountId);
        doReturn(Optional.of(mockUser1)).when(userRepository).findById(username);

        // Act
        Exception exception = assertThrows(BadRequestException.class,
                () -> discountService.removeUserFromDiscount(username, discountId));

        // Assert
        String expectedMessage = "User: " + username + " does not have discount: " + mockDiscount1.getName();
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
        verify(discountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get all authorized user discounts")
    void getAllAuthUserDiscounts_Succes() {
        // Arrange
        // BeforeEach init Discount: mockDiscount1, mockDiscount2, User: mockUser1
        String username = mockUser1.getUsername();
        mockUser1.setDiscounts(Set.of(mockDiscount1, mockDiscount2));

        doReturn(Optional.of(mockUser1)).when(userRepository).findById(username);

        // Act
        List<String> result = discountService.getAllAuthUserDiscounts(username);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("100.0% discount with code: TestDiscount100", result.get(0));
        assertEquals("75.0% discount with code: TestDiscount75", result.get(1));

    }

    @Test
    @DisplayName("Should throw exception from getAllAuthUserDiscounts method when user not found")
    void getAllAuthUserDiscounts_Exception_WhenUserNotFound() {
        // Arrange
        String username = "NoUser";
        doReturn(Optional.empty()).when(userRepository).findById(username);

        // Act
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> discountService.getAllAuthUserDiscounts(username));

        // Assert
        String expectedMessage = "Can't find user: " + username;
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Should throw exception from getAllAuthUserDiscounts method when discounts not found")
    void getAllAuthUserDiscounts_Exception_WhenDiscountsNotFound() {
        // Arrange
        // BeforeEach init User: mockUser1
        String username = mockUser1.getUsername();
        doReturn(Optional.of(mockUser1)).when(userRepository).findById(username);

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class,
                () -> discountService.getAllAuthUserDiscounts(username));

        // Assert
        String expectedMessage = "No discounts found for user: " + username;
        String actualMessage = exception.getMessage();

        assertNotNull(exception);
        assertEquals(expectedMessage, actualMessage);
    }
}