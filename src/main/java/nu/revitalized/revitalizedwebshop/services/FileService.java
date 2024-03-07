package nu.revitalized.revitalizedwebshop.services;

// Imports
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

        String returnValue;

        if (optionalSupplement.isPresent()) {
            Supplement supplement = optionalSupplement.get();
            file.setSupplement(supplement);
            File savedImage = fileRepository.save(file);
            supplement.setFile(savedImage);
            returnValue = savedImage.getName();
        } else {
            Garment garment = optionalGarment.get();
            file.setGarment(garment);
            File savedImage = fileRepository.save(file);
            garment.setFile(savedImage);
            returnValue = savedImage.getName();
        }

        return returnValue;
    }

    public byte[] downloadImage(Long productId) {
        Optional<Supplement> optionalSupplement = supplementRepository.findById(productId);
        Optional<Garment> optionalGarment = garmentRepository.findById(productId);

        if (optionalSupplement.isEmpty() && optionalGarment.isEmpty()) {
            throw new RecordNotFoundException(buildIdNotFound("Product", productId));
        }
        File file;

        if (optionalSupplement.isPresent()) {
            file = optionalSupplement.get().getFile();
        } else {
            file = optionalGarment.get().getFile();
        }

        return decompressFile(file.getFile());
    }
}
