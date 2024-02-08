package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import nu.revitalized.revitalizedwebshop.dtos.input.UserInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.UserDto;
import nu.revitalized.revitalizedwebshop.models.User;
import nu.revitalized.revitalizedwebshop.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // Transfer Methods
    public static User dtoToUser(UserInputDto inputDto) {
        User user = new User();

        user.setUsername(inputDto.getUsername().toLowerCase());
        user.setPassword(inputDto.getPassword());
        user.setEmail(inputDto.getEmail());

        return user;
    }

    public static UserDto userToDto(User user) {
        UserDto userDto = new UserDto();

        copyProperties(user, userDto);

        return userDto;
    }
}
