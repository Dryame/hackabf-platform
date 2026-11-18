package bf.hackabf.backend.dto;

import bf.hackabf.backend.entity.Inscription;
import bf.hackabf.backend.entity.InscriptionHackathon;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InscriptionResponse {
    private final String id;
    private final LocalDateTime dateInscription;
    private final String reponses;
    private final UtilisateurSummary etudiant;
    private final String hackathonId;
    private final EquipeResponse equipe;

    public InscriptionResponse(String id, LocalDateTime dateInscription, String reponses,
                               UtilisateurSummary etudiant, String hackathonId, EquipeResponse equipe) {
        this.id = id;
        this.dateInscription = dateInscription;
        this.reponses = reponses;
        this.etudiant = etudiant;
        this.hackathonId = hackathonId;
        this.equipe = equipe;
    }

    public static InscriptionResponse from(Inscription inscription) {
        if (inscription == null) {
            return null;
        }
        return new InscriptionResponse(
                inscription.getId(),
                inscription.getDateInscription(),
                inscription.getReponses(),
                UtilisateurSummary.from(inscription.getEtudiant()),
                inscription.getHackathon() != null ? inscription.getHackathon().getId() : null,
                EquipeResponse.from(inscription.getEquipe())
        );
    }

    public static InscriptionResponse from(InscriptionHackathon inscription) {
        if (inscription == null) {
            return null;
        }
        return new InscriptionResponse(
                inscription.getId(),
                inscription.getDateInscription(),
                inscription.getReponses(),
                UtilisateurSummary.from(inscription.getEtudiant()),
                inscription.getHackathon() != null ? inscription.getHackathon().getId() : null,
                null
        );
    }
}
