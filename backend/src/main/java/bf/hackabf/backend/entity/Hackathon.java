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

    @Column(name = "vainqueur")
    private String vainqueur;
    
    @Column(name = "image_vainqueur", length = 500)
    private String imageVainqueur;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "type_inscription")
    private String typeInscription = "EXTERNE";

    // LE CHAMP QUI MANQUAIT POUR LE FORMULAIRE DYNAMIQUE
    @Column(columnDefinition = "TEXT")
    private String formulaireConfig;

    @Column(name = "code_secret_hex")
    private String codeSecretHex;

    @Column(name = "est_valide")
    private boolean estValide = false;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "createur_id")
    private Utilisateur createur;
}
