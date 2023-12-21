package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.AllergenInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.AllergenDto;
import nu.revitalized.revitalizedwebshop.models.Allergen;
import org.springframework.beans.BeanUtils;

public class HelperDtoTransfer {
    public static Allergen transferToAllergen(AllergenInputDto inputDto) {
        Allergen allergen = new Allergen();

        allergen.setName(inputDto.getName());

        return allergen;
    }

    public static AllergenDto transferToDto(Allergen allergen) {
        AllergenDto allergenDto = new AllergenDto();

        allergenDto.setId(allergen.getId());
        allergenDto.setName(allergen.getName());

        return allergenDto;
    }

    public static <Input, Output> void transferProperties(Input source, Output destination) {
        BeanUtils.copyProperties(source, destination);
    }
}
