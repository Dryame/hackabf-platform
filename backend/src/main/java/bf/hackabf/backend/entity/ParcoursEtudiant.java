package bf.hackabf.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "parcours_etudiants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParcoursEtudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String statut = "EN_COURS";
    private String lienLivrable;
    private LocalDateTime dateMaj = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Utilisateur etudiant;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    private Projet projet;
}
