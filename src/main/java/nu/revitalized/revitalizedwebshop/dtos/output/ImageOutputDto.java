package nu.revitalized.revitalizedwebshop.dtos.output;

// Imports
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;

@Getter
@Setter
public class ImageOutputDto {
    private byte[] image;
    private MediaType mediaType;
}
