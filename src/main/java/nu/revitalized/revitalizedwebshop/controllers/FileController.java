package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.models.File;
import nu.revitalized.revitalizedwebshop.models.Garment;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.FileRepository;
import nu.revitalized.revitalizedwebshop.repositories.GarmentRepository;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import nu.revitalized.revitalizedwebshop.services.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

@RestController
public class FileController {
    private final SupplementRepository supplementRepository;
    private final GarmentRepository garmentRepository;
    private final FileRepository fileRepository;
    private final FileService fileService;

    public FileController(
            SupplementRepository supplementRepository,
            GarmentRepository garmentRepository,
            FileRepository fileRepository,
            FileService fileService
    ) {
        this.supplementRepository = supplementRepository;
        this.garmentRepository = garmentRepository;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
    }

    // CRUD Requests
    @GetMapping("/products/{productId}/image")
    public ResponseEntity<Object> downloadImage(
            @PathVariable("productId") Long productId
    ) throws IOException {
        byte[] image = fileService.downloadImage(productId);
        Optional<Garment> garment = garmentRepository.findById(productId);
        Optional<Supplement> supplement = supplementRepository.findById(productId);
        Optional<File> dbFile;
        MediaType mediaType = null;

        if (supplement.isPresent()) {
            dbFile = fileRepository.findById(supplement.get().getFile().getId());
            mediaType = MediaType.valueOf(dbFile.get().getType());
        }

        if (garment.isPresent()) {
            dbFile = fileRepository.findById(garment.get().getFile().getId());
            mediaType = MediaType.valueOf(dbFile.get().getType());
        }

        return ResponseEntity.ok().contentType(mediaType).body(image);
    }

    @PostMapping("/products/{productId}/image")
    public ResponseEntity<String> uploadImage(
            @RequestParam("image") MultipartFile multipartFile,
            @PathVariable Long productId
    ) throws IOException {
        String image = fileService.uploadImage(multipartFile, productId);

        return ResponseEntity.ok().body("File: " + image + " has been uploaded to product: " + productId);
    }

    @DeleteMapping("/products/{productId}/image")
    public ResponseEntity<String> deleteImage(
            @PathVariable("productId") Long productId
    ) {
        String confirmation = fileService.deleteImage(productId);

        return ResponseEntity.ok().body(confirmation);
    }
}