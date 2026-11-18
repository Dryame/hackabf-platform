package bf.hackabf.backend.service;

import bf.hackabf.backend.entity.Equipe;
import bf.hackabf.backend.entity.Hackathon;
import bf.hackabf.backend.entity.InscriptionHackathon;
import bf.hackabf.backend.entity.Utilisateur;
import bf.hackabf.backend.repository.EquipeRepository;
import bf.hackabf.backend.repository.HackathonRepository;
import bf.hackabf.backend.repository.InscriptionHackathonRepository;
import bf.hackabf.backend.repository.UtilisateurRepository;
import bf.hackabf.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class EquipeService {

    private static final String ALPHANUMERIC = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final SecureRandom random = new SecureRandom();
    private final EquipeRepository equipeRepository;
    private final HackathonRepository hackathonRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final InscriptionHackathonRepository inscriptionRepository;

    public EquipeService(EquipeRepository equipeRepository, HackathonRepository hackathonRepository,
                         UtilisateurRepository utilisateurRepository,
                         InscriptionHackathonRepository inscriptionRepository) {
        this.equipeRepository = equipeRepository;
        this.hackathonRepository = hackathonRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.inscriptionRepository = inscriptionRepository;
    }

    @Transactional
    public Equipe creerEquipe(String hackathonId, String nomEquipe, String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Utilisateur introuvable"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(() ->
                new ResourceNotFoundException("Hackathon introuvable"));
        InscriptionHackathon inscription = inscriptionRepository
                .findByEtudiantIdAndHackathonId(user.getId(), hackathonId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vous devez d'abord vous inscrire au hackathon."));

        Equipe equipe = new Equipe();
        equipe.setNom(nomEquipe);
        equipe.setCodeInvitation(generateUniqueCode());
        equipe.setHackathon(hackathon);
        equipe.setChef(user);
        Equipe savedEquipe = equipeRepository.save(equipe);

        inscription.setEquipe(savedEquipe);
        inscriptionRepository.save(inscription);

        return savedEquipe;
    }

    @Transactional
    public Equipe rejoindreEquipe(String codeInvitation, String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Utilisateur introuvable"));
        Equipe equipe = equipeRepository.findByCodeInvitation(codeInvitation).orElseThrow(() ->
                new ResourceNotFoundException("Code d'invitation invalide"));

        InscriptionHackathon inscription = inscriptionRepository.findByEtudiantIdAndHackathonId(user.getId(), equipe.getHackathon().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vous devez d'abord vous inscrire au hackathon."));
        
        if (inscription.getEquipe() != null) {
            throw new IllegalArgumentException("Vous êtes déjà dans une équipe.");
        }

        inscription.setEquipe(equipe);
        inscriptionRepository.save(inscription);
        return equipe;
    }

    public List<Equipe> getEquipesParHackathon(String hackathonId) {
        return equipeRepository.findByHackathonId(hackathonId);
    }

    public Optional<Equipe> findByCodeInvitation(String codeInvitation) {
        return equipeRepository.findByCodeInvitation(codeInvitation);
    }

    private String generateUniqueCode() {
        for (int attempt = 0; attempt < 20; attempt++) {
            StringBuilder code = new StringBuilder(10);
            for (int i = 0; i < 10; i++) {
                code.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
            }
            if (equipeRepository.findByCodeInvitation(code.toString()).isEmpty()) {
                return code.toString();
            }
        }
        throw new IllegalStateException("Impossible de générer un code d'invitation unique.");
    }
}
