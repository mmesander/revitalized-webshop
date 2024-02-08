package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nu.revitalized.revitalizedwebshop.security.Authority;

import java.util.HashSet;
import java.util.Set;

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
    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "username",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();


    // Methods
    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
    }
}
