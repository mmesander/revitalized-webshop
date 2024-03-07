package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.repositories.FileRepository;
import nu.revitalized.revitalizedwebshop.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageDataService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public ImageDataService(
            FileRepository fileRepository,
            UserRepository userRepository
    ) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    public String uploadImage(MultipartFile multipartFile) {}
}
