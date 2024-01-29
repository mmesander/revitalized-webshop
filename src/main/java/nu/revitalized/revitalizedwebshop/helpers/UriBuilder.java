package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

public class UriBuilder {
    public static URI buildUri(Identifiable uriObject) {
        URI uri = URI.create((
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + uriObject.getId()).toUriString())
                );

        return uri;
    }
}
