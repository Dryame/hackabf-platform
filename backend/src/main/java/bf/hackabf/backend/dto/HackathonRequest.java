package bf.hackabf.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HackathonRequest {
    // Plus aucune règle @NotBlank ou @NotNull ici
    private String titre;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String lieu;
    private String format;
    private String lienInscription;
    private String imageUrl;
    private String typeInscription;
    private String formulaireConfig;
    private String codeSecretHex;
    private Long categorieId;
}
