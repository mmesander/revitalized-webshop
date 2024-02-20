package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Entity
@IdClass(AuthorityKey.class)
@Table(name = "authorities")
public class Authority implements Serializable {
    // Variables
    @Id
    @Column(nullable = false)
    private String username;

    @Id
    @Column
    private String authority;

    // Methods
    public Authority() {}

    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }
}
