package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableId;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableOrderNumber;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableUsername;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

public class UriBuilder {
    public static URI buildUriId(IdentifiableId uriObject) {
        URI uri = URI.create((
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + uriObject.getId()).toUriString())
        );

        return uri;
    }

    public static URI buildUriUsername(IdentifiableUsername uriObject) {
        URI uri = URI.create((
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + uriObject.getUsername()).toUriString())
        );

        return uri;
    }

    public static URI buildUriOrderNumber(IdentifiableOrderNumber uriObject) {
        URI uri = URI.create((
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + uriObject.getOrderNumber()).toUriString())
        );

        return uri;
    }
}

