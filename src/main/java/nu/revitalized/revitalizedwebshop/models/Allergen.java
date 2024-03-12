package nu.revitalized.revitalizedwebshop.models;

// Imports
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "allergens")
public class Allergen {
    // Variables
    @SequenceGenerator(name = "allergens_seq", allocationSize = 1, initialValue = 1001)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allergens_seq")
    private Long id;

    @Column(unique = true)
    private String name;

    // Relations
    @ManyToMany(mappedBy = "allergens")
    private Set<Supplement> supplements = new HashSet<>();
}