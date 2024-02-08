package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    // Variables
    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;

    // Relations


    // Methods
}
