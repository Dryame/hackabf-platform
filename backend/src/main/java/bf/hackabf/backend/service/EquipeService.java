package bf.hackabf.backend.service;

import bf.hackabf.backend.entity.Equipe;
import bf.hackabf.backend.entity.Hackathon;
import bf.hackabf.backend.entity.InscriptionHackathon;
import bf.hackabf.backend.entity.Utilisateur;
import bf.hackabf.backend.repository.EquipeRepository;
import bf.hackabf.backend.repository.HackathonRepository;
import bf.hackabf.backend.repository.InscriptionHackathonRepository;
import bf.hackabf.backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EquipeService {

    private final EquipeRepository equipeRepository;
    private final HackathonRepository hackathonRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final InscriptionHackathonRepository inscriptionRepository;

    public EquipeService(EquipeRepository equipeRepository, HackathonRepository hackathonRepository, UtilisateurRepository utilisateurRepository, InscriptionHackathonRepository inscriptionRepository) {
        this.equipeRepository = equipeRepository;
        this.hackathonRepository = hackathonRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.inscriptionRepository = inscriptionRepository;
    }

    public Equipe creerEquipe(String hackathonId, String nomEquipe, String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(() -> new RuntimeException("Hackathon introuvable"));

        Equipe equipe = new Equipe();
        equipe.setNom(nomEquipe);
        // Génération d'un code unique (ex: ALPHA-X7F)
        equipe.setCodeInvitation(nomEquipe.toUpperCase().replace(" ", "-") + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        equipe.setHackathon(hackathon);
        equipe.setChef(user);
        Equipe savedEquipe = equipeRepository.save(equipe);

        // On lie l'inscription existante à cette équipe
        InscriptionHackathon inscription = inscriptionRepository.findByEtudiantIdAndHackathonId(user.getId(), hackathonId)
                .orElseThrow(() -> new RuntimeException("Vous devez d'abord vous inscrire au hackathon."));
        inscription.setEquipe(savedEquipe);
        inscriptionRepository.save(inscription);

        return savedEquipe;
    }

    public Equipe rejoindreEquipe(String codeInvitation, String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        Equipe equipe = equipeRepository.findByCodeInvitation(codeInvitation).orElseThrow(() -> new RuntimeException("Code d'invitation invalide"));

        InscriptionHackathon inscription = inscriptionRepository.findByEtudiantIdAndHackathonId(user.getId(), equipe.getHackathon().getId())
                .orElseThrow(() -> new RuntimeException("Vous devez d'abord vous inscrire au hackathon."));
        
        if (inscription.getEquipe() != null) {
            throw new RuntimeException("Vous êtes déjà dans une équipe.");
        }

        inscription.setEquipe(equipe);
        inscriptionRepository.save(inscription);
        return equipe;
    }

    public List<Equipe> getEquipesParHackathon(String hackathonId) {
        return equipeRepository.findByHackathonId(hackathonId);
    }
}
