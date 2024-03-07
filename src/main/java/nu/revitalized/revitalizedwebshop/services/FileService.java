package nu.revitalized.revitalizedwebshop.services;

// Imports
import nu.revitalized.revitalizedwebshop.exceptions.BadRequestException;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.*;
import nu.revitalized.revitalizedwebshop.repositories.FileRepository;
import nu.revitalized.revitalizedwebshop.repositories.GarmentRepository;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;
import static nu.revitalized.revitalizedwebshop.helpers.BuildIdNotFound.buildIdNotFound;
import static nu.revitalized.revitalizedwebshop.utils.FileUtil.compressFile;
import static nu.revitalized.revitalizedwebshop.utils.FileUtil.decompressFile;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final SupplementRepository supplementRepository;
    private final GarmentRepository garmentRepository;

    public FileService(
            FileRepository fileRepository,
            SupplementRepository supplementRepository,
            GarmentRepository garmentRepository
    ) {
        this.fileRepository = fileRepository;
        this.supplementRepository = supplementRepository;
        this.garmentRepository = garmentRepository;
    }

    public String uploadImage(MultipartFile multipartFile, Long productId) throws IOException {
        Optional<Supplement> optionalSupplement = supplementRepository.findById(productId);
        Optional<Garment> optionalGarment = garmentRepository.findById(productId);

        if (optionalSupplement.isEmpty() && optionalGarment.isEmpty()) {
            throw new RecordNotFoundException(buildIdNotFound("Product", productId));
        }

        File file = new File();
        file.setName(multipartFile.getName());
        file.setType(multipartFile.getContentType());
        file.setFile(compressFile(multipartFile.getBytes()));

        String confirmation;

        if (optionalSupplement.isPresent()) {
            Supplement supplement = optionalSupplement.get();
            if (supplement.getFile() != null) {
                throw new BadRequestException("Product: " + supplement.getName() + " with id: " + productId
                        + " is already assigned to an image");
            }
            file.setSupplement(supplement);
            File savedImage = fileRepository.save(file);
            supplement.setFile(savedImage);
            confirmation = savedImage.getName();
        } else {
            Garment garment = optionalGarment.get();
            if (garment.getFile() != null) {
                throw new BadRequestException("Product: " + garment.getName() + " with id: " + productId
                        + " is already assigned to an image");
            }
            file.setGarment(garment);
            File savedImage = fileRepository.save(file);
            garment.setFile(savedImage);
            confirmation = savedImage.getName();
        }

        return confirmation;
    }

    public byte[] downloadImage(Long productId) {
        Optional<Supplement> optionalSupplement = supplementRepository.findById(productId);
        Optional<Garment> optionalGarment = garmentRepository.findById(productId);

        if (optionalSupplement.isEmpty() && optionalGarment.isEmpty()) {
            throw new RecordNotFoundException(buildIdNotFound("Product", productId));
        }

        File file;

        if (optionalSupplement.isPresent()) {
            Supplement supplement = optionalSupplement.get();
            if (supplement.getFile() == null) {
                throw new BadRequestException("Product with id: " + productId + " does not have an image");
            }
            file = optionalSupplement.get().getFile();
        } else {
            Garment garment = optionalGarment.get();
            if (garment.getFile() == null) {
                throw new BadRequestException("Product with id: " + productId + " does not have an image");
            }
            file = optionalGarment.get().getFile();
        }

        return decompressFile(file.getFile());
    }

    public String deleteImage(Long productId) {
        Optional<Supplement> optionalSupplement = supplementRepository.findById(productId);
        Optional<Garment> optionalGarment = garmentRepository.findById(productId);

        if (optionalSupplement.isEmpty() && optionalGarment.isEmpty()) {
            throw new RecordNotFoundException(buildIdNotFound("Product", productId));
        }

        String confirmation;

        if (optionalSupplement.isPresent()) {
            Supplement supplement = optionalSupplement.get();
            if (supplement.getFile() == null) {
                throw new BadRequestException("Product with id: " + productId + " does not have an image");
            }
            confirmation = "Image with id: " + supplement.getFile().getId() + " is removed from product: " + productId;
            fileRepository.deleteById(supplement.getFile().getId());
            supplementRepository.save(supplement);
        } else {
            Garment garment = optionalGarment.get();
            if (garment.getFile() == null) {
                throw new BadRequestException("Product with id: " + productId + " does not have an image");
            }
            confirmation = "Image with id: " + garment.getFile().getId() + " is removed from product: " + productId;
            fileRepository.deleteById(garment.getFile().getId());
            garmentRepository.save(garment);
        }

        return confirmation;
    }
}
