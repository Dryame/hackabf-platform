package bf.hackabf.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscriptions_hackathons", uniqueConstraints = {
        @UniqueConstraint(name = "uk_inscription_etudiant_hackathon_v2", columnNames = {"etudiant_id", "hackathon_id"})
})
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Correction : suppression de name="date_inscription" pour éviter le conflit de mapping
    private LocalDateTime dateInscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Utilisateur etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id", nullable = false)
    @JsonIgnore
    private Hackathon hackathon;

    @Column(columnDefinition = "TEXT")
    private String reponses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public LocalDateTime getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }
    public Utilisateur getEtudiant() { return etudiant; }
    public void setEtudiant(Utilisateur etudiant) { this.etudiant = etudiant; }
    public Hackathon getHackathon() { return hackathon; }
    public void setHackathon(Hackathon hackathon) { this.hackathon = hackathon; }
    public String getReponses() { return reponses; }
    public void setReponses(String reponses) { this.reponses = reponses; }
    public Equipe getEquipe() { return equipe; }
    public void setEquipe(Equipe equipe) { this.equipe = equipe; }
}
