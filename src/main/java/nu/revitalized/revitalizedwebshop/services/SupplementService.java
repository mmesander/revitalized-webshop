package nu.revitalized.revitalizedwebshop.services;

import nu.revitalized.revitalizedwebshop.dtos.input.SupplementInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.SupplementDto;
import nu.revitalized.revitalizedwebshop.models.Supplement;
import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;
import static nu.revitalized.revitalizedwebshop.helpers.HelperCopyProperties.copyProperties;


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
}
