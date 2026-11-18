package bf.hackabf.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> 25db076 (fix(security): complete phase 1 critical hardening)

@Entity
@Table(name = "equipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nom;

<<<<<<< HEAD
<<<<<<< HEAD
    @Column(name = "code_invitation", unique = true, nullable = false)
    private String codeInvitation;

    @ManyToOne
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    @ManyToOne
    @JoinColumn(name = "chef_id", nullable = false)
    private Utilisateur chef;

    // Un membre peut être dans plusieurs équipes (si plusieurs hackathons), mais on gère ça via Inscription
=======
=======
    @Column(name = "code_invitation", length = 12, unique = true)
    private String codeInvitation;

>>>>>>> 6c1cd83 (refactor(backend): complete phase 2 hardening)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;
>>>>>>> 25db076 (fix(security): complete phase 1 critical hardening)
}
