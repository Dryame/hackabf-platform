package bf.hackabf.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscriptions_hackathons")
@Data
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

    // Lien avec l'équipe (Optionnel, car on peut s'inscrire en solo)
    @ManyToOne
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;
}
