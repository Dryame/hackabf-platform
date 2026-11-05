package bf.hackabf.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HackathonResponse {
    private String id;
    private String titre;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String lieu;
    private String format;
    private String lienInscription;
    private String statut;
    private String categorieNom;
    private String createurNom;
    private String vainqueur;
    private String imageVainqueur;
    private String imageUrl;
}
