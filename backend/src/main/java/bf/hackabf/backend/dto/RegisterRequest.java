package bf.hackabf.backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nomComplet;
    private String email;
    private String motDePasse;
    private String etablissement;
    private String niveauEtude;
    private String ville;
}
