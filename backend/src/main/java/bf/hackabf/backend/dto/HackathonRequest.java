package bf.hackabf.backend.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HackathonRequest {
    // Plus aucune règle @NotBlank ou @NotNull ici
    private String titre;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    @Size(max = 255, message = "Le lieu ne doit pas dépasser 255 caractères")
    private String lieu;
    private String format;
    private String lienInscription;
    private String imageUrl;
    private String typeInscription;
    private String formulaireConfig;
    private String codeSecretHex;
    private Long categorieId;
}
