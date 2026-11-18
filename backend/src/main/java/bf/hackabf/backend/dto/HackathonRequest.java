package bf.hackabf.backend.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
>>>>>>> 6c1cd83 (refactor(backend): complete phase 2 hardening)
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HackathonRequest {
    // Plus aucune règle @NotBlank ou @NotNull ici
    private String titre;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
<<<<<<< HEAD
=======
    @Size(max = 255, message = "Le lieu ne doit pas dépasser 255 caractères")
>>>>>>> 6c1cd83 (refactor(backend): complete phase 2 hardening)
    private String lieu;
    private String format;
    private String lienInscription;
    private String imageUrl;
    private String typeInscription;
    private String formulaireConfig;
    private String codeSecretHex;
    private Long categorieId;
}
