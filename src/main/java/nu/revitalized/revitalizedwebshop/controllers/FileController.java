package nu.revitalized.revitalizedwebshop.controllers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.output.ImageOutputDto;
import nu.revitalized.revitalizedwebshop.services.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // CRUD Requests
    @GetMapping("/products/{productId}/image")
    public ResponseEntity<Object> downloadImage(
            @PathVariable("productId") Long productId
    ) {
        ImageOutputDto image = fileService.downloadImage(productId);

        return ResponseEntity.ok().contentType(image.getMediaType()).body(image.getImage());
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