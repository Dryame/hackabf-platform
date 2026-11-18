package bf.hackabf.backend.dto;

import bf.hackabf.backend.entity.Equipe;
import lombok.Getter;

@Getter
public class EquipeResponse {
    private final String id;
    private final String nom;
    private final String hackathonId;

    public EquipeResponse(String id, String nom, String hackathonId) {
        this.id = id;
        this.nom = nom;
        this.hackathonId = hackathonId;
    }

    public static EquipeResponse from(Equipe equipe) {
        if (equipe == null) {
            return null;
        }
        return new EquipeResponse(
                equipe.getId(),
                equipe.getNom(),
                equipe.getHackathon() != null ? equipe.getHackathon().getId() : null
        );
    }
}
