package bf.hackabf.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

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

    @Column(name = "code_invitation", length = 12, unique = true, nullable = false)
    private String codeInvitation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chef_id", nullable = false)
    private Utilisateur chef;
}
