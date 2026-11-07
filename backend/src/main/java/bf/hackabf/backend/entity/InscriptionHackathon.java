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

    // Stocke les réponses du formulaire dynamique en JSON
    @Column(columnDefinition = "TEXT")
    private String reponses;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Utilisateur etudiant;

    @ManyToOne
    @JoinColumn(name = "hackathon_id")
    private Hackathon hackathon;
}
