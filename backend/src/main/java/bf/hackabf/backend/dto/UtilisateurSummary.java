package bf.hackabf.backend.dto;

import bf.hackabf.backend.entity.Utilisateur;
import lombok.Getter;

@Getter
public class UtilisateurSummary {
    private final String id;
    private final String nomComplet;
    private final String email;

    public UtilisateurSummary(String id, String nomComplet, String email) {
        this.id = id;
        this.nomComplet = nomComplet;
        this.email = email;
    }

    public static UtilisateurSummary from(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        return new UtilisateurSummary(utilisateur.getId(), utilisateur.getNomComplet(), utilisateur.getEmail());
    }
}
