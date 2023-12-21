package nu.revitalized.revitalizedwebshop.services;

import nu.revitalized.revitalizedwebshop.dtos.input.SupplementInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SupplementDto;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;

import static nu.revitalized.revitalizedwebshop.helpers.CopyPropertiesHelper.copyProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class SupplementService {
    private final SupplementRepository supplementRepository;

    public SupplementService(SupplementRepository supplementRepository) {
        this.supplementRepository = supplementRepository;
    }


    // Transfer Methods
    public Supplement transferToSupplement(SupplementInputDto inputDto) {
        Supplement supplement = new Supplement();

        copyProperties(inputDto, supplement);

        return supplement;
    }

    public SupplementDto transferToSupplementDto(Supplement supplement) {
        SupplementDto supplementDto = new SupplementDto();

        copyProperties(supplement, supplementDto);

        return supplementDto;
    }


    // Get Methods
    public List<SupplementDto> getAllSupplements() {
        List<Supplement> supplements = supplementRepository.findAll();
        List<SupplementDto> supplementDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            SupplementDto supplementDto = transferToSupplementDto(supplement);
            supplementDtos.add(supplementDto);
        }

        if (supplementDtos.isEmpty()) {
            throw new RecordNotFoundException("No supplements are found");
        } else {
            return supplementDtos;
        }
    }

    public SupplementDto getSupplementById(Long id) {
        Optional<Supplement> supplement = supplementRepository.findById(id);

        if (supplement.isPresent()) {
            return transferToSupplementDto(supplement.get());
        } else {
            throw new RecordNotFoundException("No supplement found with id: " + id);
        }
    }

    public List<SupplementDto> getSupplementsByBrandAndName(String brand, String name) {
        List<Supplement> supplements = supplementRepository.
                findSupplementsByBrandContainsIgnoreCaseAndNameContainsIgnoreCase(brand, name);
        List<SupplementDto> supplementDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            SupplementDto supplementDto = transferToSupplementDto(supplement);
            supplementDtos.add(supplementDto);
        }

        if (supplementDtos.isEmpty()) {
            throw new RecordNotFoundException("No supplements found with name: " + name + " and brand: " + brand);
        } else {
            return supplementDtos;
        }
    }

    public List<SupplementDto> getSupplementsByBrand(String brand) {
        List<Supplement> supplements = supplementRepository.findSupplementsByBrandContainsIgnoreCase(brand);
        List<SupplementDto> supplementDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            SupplementDto supplementDto = transferToSupplementDto(supplement);
            supplementDtos.add(supplementDto);
        }

        if (supplementDtos.isEmpty()) {
            throw new RecordNotFoundException("No supplements found with brand: " + brand);
        } else {
            return supplementDtos;
        }
    }

    public List<SupplementDto> getSupplementsByName(String name) {
        List<Supplement> supplements = supplementRepository.findSupplementByNameContainsIgnoreCase(name);
        List<SupplementDto> supplementDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            SupplementDto supplementDto = transferToSupplementDto(supplement);
            supplementDtos.add(supplementDto);
        }

        if (supplementDtos.isEmpty()) {
            throw new RecordNotFoundException("No supplements found with name: " + name);
        } else {
            return supplementDtos;
        }
    }

    public List<SupplementDto> getSupplementsByPrice(Double price) {
        List<Supplement> supplements = supplementRepository.findSupplementsByPriceLessThanEqual(price);
        List<SupplementDto> supplementDtos = new ArrayList<>();

        for (Supplement supplement : supplements) {
            SupplementDto supplementDto = transferToSupplementDto(supplement);
            supplementDtos.add(supplementDto);
        }

        if (supplementDtos.isEmpty()) {
            throw new RecordNotFoundException("No supplements found with a price lower or equal to " + price);
        } else {
            return supplementDtos;
        }
    }


    // Create Methods
    public SupplementDto createSupplement(SupplementInputDto inputDto) {
        Supplement supplement = transferToSupplement(inputDto);

        supplementRepository.save(supplement);

        return transferToSupplementDto(supplement);
    }
}
