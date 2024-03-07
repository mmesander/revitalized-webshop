package nu.revitalized.revitalizedwebshop.helpers;

// Imports
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableId;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableOrderNumber;
import nu.revitalized.revitalizedwebshop.interfaces.IdentifiableUsername;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

public class UriBuilder {
    public static URI buildUriId(IdentifiableId uriObject) {
        return URI.create((
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + uriObject.getId()).toUriString())
        );
    }

    public static URI buildUriUsername(IdentifiableUsername uriObject) {
        return URI.create((
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + uriObject.getUsername()).toUriString())
        );
    }

    public static URI buildUriOrderNumber(IdentifiableOrderNumber uriObject) {
        return URI.create((
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + uriObject.getOrderNumber()).toUriString())
        );
    }
}