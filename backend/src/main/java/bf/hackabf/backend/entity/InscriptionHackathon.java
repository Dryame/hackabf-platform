package bf.hackabf.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscriptions_hackathons", uniqueConstraints = {
        @UniqueConstraint(name = "uk_inscription_etudiant_hackathon", columnNames = {"etudiant_id", "hackathon_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionHackathon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDateTime dateInscription = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String reponses;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Utilisateur etudiant;

    @ManyToOne
    @JoinColumn(name = "hackathon_id")
    private Hackathon hackathon;

<<<<<<< HEAD
    // Lien avec l'équipe (Optionnel, car on peut s'inscrire en solo)
    @ManyToOne
=======
    @ManyToOne(fetch = FetchType.LAZY)
>>>>>>> 25db076 (fix(security): complete phase 1 critical hardening)
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;
}
