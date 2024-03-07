package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {
    // Variables
    @SequenceGenerator(name = "reviews_seq", allocationSize = 1, initialValue = 10001)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_seq")
    private Long id;
    private String review;
    private Integer rating;
    private Date date;

    // Relations
    @ManyToOne
    @JoinColumn(name = "garment_id")
    private Garment garment;

    @ManyToOne
    @JoinColumn(name = "supplement_id")
    private Supplement supplement;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}