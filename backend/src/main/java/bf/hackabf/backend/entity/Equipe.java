package bf.hackabf.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "equipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nom;

    @Column(name = "code_invitation", unique = true, nullable = false)
    private String codeInvitation;

    @ManyToOne
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    @ManyToOne
    @JoinColumn(name = "chef_id", nullable = false)
    private Utilisateur chef;

    // Un membre peut être dans plusieurs équipes (si plusieurs hackathons), mais on gère ça via Inscription
}
