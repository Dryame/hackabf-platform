package bf.hackabf.backend.service;

import bf.hackabf.backend.entity.Hackathon;
import bf.hackabf.backend.entity.InscriptionHackathon;
import bf.hackabf.backend.entity.Utilisateur;
import bf.hackabf.backend.repository.HackathonRepository;
import bf.hackabf.backend.repository.InscriptionHackathonRepository;
import bf.hackabf.backend.repository.UtilisateurRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InscriptionService {

    private final InscriptionHackathonRepository inscriptionRepository;
    private final HackathonRepository hackathonRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InscriptionService(InscriptionHackathonRepository inscriptionRepository, HackathonRepository hackathonRepository, UtilisateurRepository utilisateurRepository) {
        this.inscriptionRepository = inscriptionRepository;
        this.hackathonRepository = hackathonRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public void inscrire(String hackathonId, String email, Map<String, String> reponses) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new RuntimeException("Hackathon introuvable"));

        if (inscriptionRepository.findByEtudiantIdAndHackathonId(user.getId(), hackathonId).isPresent()) {
            throw new RuntimeException("Vous êtes déjà inscrit à ce hackathon.");
        }

        InscriptionHackathon inscription = new InscriptionHackathon();
        inscription.setEtudiant(user);
        inscription.setHackathon(hackathon);
        
        // On convertit la Map de réponses en JSON
        try {
            if (reponses != null && !reponses.isEmpty()) {
                inscription.setReponses(objectMapper.writeValueAsString(reponses));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur de format des réponses");
        }

        inscriptionRepository.save(inscription);
    }

    public List<InscriptionHackathon> getInscriptionsByHackathon(String hackathonId, String email) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new RuntimeException("Hackathon introuvable"));
        if (!hackathon.getCreateur().getEmail().equals(email)) throw new RuntimeException("Non autorisé.");
        return inscriptionRepository.findByHackathonId(hackathonId);
    }

    public List<InscriptionHackathon> getMesInscriptions(String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return inscriptionRepository.findByEtudiantId(user.getId());
    }
}
