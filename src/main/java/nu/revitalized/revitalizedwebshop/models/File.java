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

    @OneToOne
    @JoinColumn(name = "supplement_id", referencedColumnName = "id")
    private Supplement supplement;

    @OneToOne
    @JoinColumn(name = "garment_id", referencedColumnName = "id")
    private Garment garment;
}
