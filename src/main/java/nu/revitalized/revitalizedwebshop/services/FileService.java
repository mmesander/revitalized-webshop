package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.exceptions.UsernameNotFoundException;
import nu.revitalized.revitalizedwebshop.models.File;
import nu.revitalized.revitalizedwebshop.models.User;
import nu.revitalized.revitalizedwebshop.repositories.FileRepository;
import nu.revitalized.revitalizedwebshop.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import static nu.revitalized.revitalizedwebshop.utils.FileUtil.compressFile;
import static nu.revitalized.revitalizedwebshop.utils.FileUtil.decompressFile;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public FileService(
            FileRepository fileRepository,
            UserRepository userRepository
    ) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    public String uploadImage(MultipartFile multipartFile, String username) throws IOException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        File file = new File();
        file.setName(multipartFile.getName());
        file.setType(multipartFile.getContentType());
        file.setFile(compressFile(multipartFile.getBytes()));
        file.setUser(user);

        File savedImage = fileRepository.save(file);
        user.setFile(savedImage);
        userRepository.save(user);

        return savedImage.getName();
    }

    public byte[] downloadImage(String username) throws IOException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        File file = user.getFile();

        return decompressFile(file.getFile());
    }
}
