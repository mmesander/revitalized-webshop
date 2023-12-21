package nu.revitalized.revitalizedwebshop.services;

import nu.revitalized.revitalizedwebshop.repositories.SupplementRepository;

public class SupplementService {
    private final SupplementRepository supplementRepository;

    public SupplementService(SupplementRepository supplementRepository) {
        this.supplementRepository = supplementRepository;
    }


}
