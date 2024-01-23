package nu.revitalized.revitalizedwebshop.dtos.input;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchInputDto {
    private String name;
    private String brand;
    private Double price;
    private Double averageRating;
    private String contains;
}
