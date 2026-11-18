package bf.hackabf.backend.service;

import bf.hackabf.backend.entity.Equipe;
import bf.hackabf.backend.entity.Hackathon;
<<<<<<< HEAD
import bf.hackabf.backend.entity.InscriptionHackathon;
import bf.hackabf.backend.entity.Utilisateur;
import bf.hackabf.backend.repository.EquipeRepository;
import bf.hackabf.backend.repository.HackathonRepository;
import bf.hackabf.backend.repository.InscriptionHackathonRepository;
import bf.hackabf.backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
=======
import bf.hackabf.backend.exception.ResourceNotFoundException;
import bf.hackabf.backend.repository.EquipeRepository;
import bf.hackabf.backend.repository.HackathonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;
>>>>>>> 6c1cd83 (refactor(backend): complete phase 2 hardening)

@Service
public class EquipeService {

<<<<<<< HEAD
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
=======
    private static final String ALPHANUMERIC = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final SecureRandom random = new SecureRandom();
    private final EquipeRepository equipeRepository;
    private final HackathonRepository hackathonRepository;

    public EquipeService(EquipeRepository equipeRepository, HackathonRepository hackathonRepository) {
        this.equipeRepository = equipeRepository;
        this.hackathonRepository = hackathonRepository;
    }

    @Transactional
    public Equipe createEquipe(String hackathonId, String nom) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new ResourceNotFoundException("Hackathon introuvable"));
        Equipe equipe = new Equipe();
        equipe.setNom(nom);
        equipe.setHackathon(hackathon);
        equipe.setCodeInvitation(generateUniqueCode());
        return equipeRepository.save(equipe);
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
>>>>>>> 6c1cd83 (refactor(backend): complete phase 2 hardening)
    }
}
