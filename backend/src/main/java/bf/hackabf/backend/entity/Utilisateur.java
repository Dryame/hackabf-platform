package bf.hackabf.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utilisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasse; 

    private String nomComplet;
    private String etablissement;
    private String niveauEtude;
    private String ville;
    private String lienGithub;

    @Column(name = "statut_accreditation")
    private String statutAccreditation = "NON_DEMANDE";

    private boolean estActif = true;

    // Relation Many-to-Many avec les rôles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "utilisateur_roles",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
