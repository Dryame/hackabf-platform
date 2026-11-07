package bf.hackabf.backend.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UtilisateurResponse {
    private String id;
    private String nomComplet;
    private String email;
    private String etablissement;
    private boolean estActif;
    private String statutAccreditation;
    private Set<String> roles;
}
