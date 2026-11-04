package bf.hackabf.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HackathonRequest {
    
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;
    
    @NotBlank(message = "La description est obligatoire")
    private String description;
    
    @NotNull(message = "La date de début est obligatoire")
    private LocalDateTime dateDebut;
    
    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime dateFin;
    
    private String lieu;
    private String format;
    private String lienInscription;
    
    @NotNull(message = "L'ID de la catégorie est obligatoire")
    private Long categorieId;
}
