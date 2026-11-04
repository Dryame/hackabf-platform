package bf.hackabf.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "hackathons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hackathon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String lieu;
    private String format;
    
    @Column(length = 500)
    private String lienInscription;
    
    private String statut = "A_VENIR";

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "createur_id")
    private Utilisateur createur;
}
