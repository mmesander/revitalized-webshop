package nu.revitalized.revitalizedwebshop.services;

// Imports
import static nu.revitalized.revitalizedwebshop.helpers.CopyProperties.copyProperties;
import static nu.revitalized.revitalizedwebshop.specifications.DiscountSpecification.*;
import nu.revitalized.revitalizedwebshop.dtos.input.DiscountInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.DiscountDto;
import nu.revitalized.revitalizedwebshop.exceptions.RecordNotFoundException;
import nu.revitalized.revitalizedwebshop.models.Discount;
import nu.revitalized.revitalizedwebshop.repositories.DiscountRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {
    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    // Transfer Methods
    public static Discount dtoToDiscount(DiscountInputDto inputDto) {
        Discount discount = new Discount();

        copyProperties(inputDto, discount);

        return discount;
    }

    public static DiscountDto discountToDto(Discount discount) {
        DiscountDto discountDto = new DiscountDto();

        copyProperties(discount, discountDto);

        return discountDto;
    }

    // CRUD Methods
    public List<DiscountDto> getAllDiscounts() {
        List<Discount> discounts = discountRepository.findAll();
        List<DiscountDto> discountDtos = new ArrayList<>();

        for (Discount discount : discounts) {
            DiscountDto discountDto = discountToDto(discount);
            discountDtos.add(discountDto);
        }

        if (discountDtos.isEmpty()) {
            throw new RecordNotFoundException("No discounts found");
        } else {
            discountDtos.sort(Comparator.comparing(DiscountDto::getId));

            return discountDtos;
        }
    }

    public DiscountDto getDiscountById(Long id) {
        Optional<Discount> discount = discountRepository.findById(id);

        if (discount.isPresent()) {
            return discountToDto(discount.get());
        } else {
            throw new RecordNotFoundException("Discount with id: " + id + " not found");
        }
    }

    public List<DiscountDto> getAllDiscountsByParam(
            String name,
            Double value,
            Double minValue,
            Double maxValue
    ) {
        Specification<Discount> params = Specification.where
                (StringUtils.isBlank(name) ? null : getDiscountNameLikeFilter(name))
                .and(value == null ? null : getDiscountValueLikeFilter(value))
                .and(minValue == null ? null : getDiscountValueMoreThanFilter(minValue))
                .and(maxValue == null ? null : getDiscountValueLessThanFilter(maxValue));

        List<Discount> filteredDiscounts = discountRepository.findAll(params);
        List<DiscountDto> discountDtos = new ArrayList<>();

        for (Discount discount : filteredDiscounts) {
            DiscountDto discountDto = discountToDto(discount);
            discountDtos.add(discountDto);
        }

        if (discountDtos.isEmpty()) {
            throw new RecordNotFoundException("No discounts found with the specified filters");
        } else {
            discountDtos.sort(Comparator.comparing(DiscountDto::getValue).reversed());

            return discountDtos;
        }
    }

    public DiscountDto createDiscount() {}

    public DiscountDto updateDiscount() {}

    public DiscountDto patchDiscount() {}

    public String deleteDiscount() {}

    // Relation - User Methods
}
