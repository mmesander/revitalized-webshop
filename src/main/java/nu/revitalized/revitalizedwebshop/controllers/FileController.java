package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.models.File;
import nu.revitalized.revitalizedwebshop.models.User;
import nu.revitalized.revitalizedwebshop.repositories.FileRepository;
import nu.revitalized.revitalizedwebshop.repositories.UserRepository;
import nu.revitalized.revitalizedwebshop.services.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

@RestController
public class FileController {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FileService fileService;

    public FileController(
            UserRepository userRepository,
            FileRepository fileRepository,
            FileService fileService
    ) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
    }

    // CRUD Requests
    @GetMapping("/image/{username}")
    public ResponseEntity<Object> downloadImage(
            @PathVariable("username") String username
    ) throws IOException {
        byte[] image = fileService.downloadImage(username);
        Optional<User> user = userRepository.findById(username);
        Optional<File> dbFile = fileRepository.findById(user.get().getFile().getId());
        MediaType mediaType = MediaType.valueOf(dbFile.get().getType());

        return ResponseEntity.ok().contentType(mediaType).body(image);
    }

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam String username
    ) throws IOException {
        String image = fileService.uploadImage(multipartFile, username);

        return ResponseEntity.ok().body("File has been uploaded, " + image);
    }

}
