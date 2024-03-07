package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "image_data")
public class ImageData {
    // Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    @Lob
    private byte[] imageData;

    // Relations
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Constructors
    public ImageData() {
    }

    public ImageData(String name, String type, byte[] imageData, User user) {
        this.name = name;
        this.type = type;
        this.imageData = imageData;
        this.user = user;
    }
}
