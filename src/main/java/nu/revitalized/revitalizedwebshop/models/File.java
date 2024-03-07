package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "files")
public class File {
    // Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    @Lob
    private byte[] file;

    // Relations
    @OneToOne
    @JoinColumn(name = "user_username", referencedColumnName = "username")
    private User user;

    // Constructors
    public File() {
    }

    public File(String name, String type, byte[] file, User user) {
        this.name = name;
        this.type = type;
        this.file = file;
        this.user = user;
    }
}
