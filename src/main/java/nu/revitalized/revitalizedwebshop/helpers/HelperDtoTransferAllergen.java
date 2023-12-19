package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import nu.revitalized.revitalizedwebshop.dtos.input.AllergenInputDto;
import nu.revitalized.revitalizedwebshop.dtos.output.AllergenDto;
import nu.revitalized.revitalizedwebshop.models.Allergen;

public class HelperDtoTransferAllergen {
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
}
