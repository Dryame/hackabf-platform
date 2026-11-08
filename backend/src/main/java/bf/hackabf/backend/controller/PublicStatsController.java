package bf.hackabf.backend.controller;

import bf.hackabf.backend.repository.HackathonRepository;
import bf.hackabf.backend.repository.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/public/stats")
public class PublicStatsController {

    private final HackathonRepository hackathonRepository;
    private final UtilisateurRepository utilisateurRepository;

    public PublicStatsController(HackathonRepository hackathonRepository, UtilisateurRepository utilisateurRepository) {
        this.hackathonRepository = hackathonRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Long>> getStats() {
        // On compte uniquement les hackathons validés et les utilisateurs actifs
        long totalHackathons = hackathonRepository.findByEstValideTrueOrderByDateDebutDesc(org.springframework.data.domain.PageRequest.of(0, 1)).getTotalElements();
        long totalUtilisateurs = utilisateurRepository.count();
        
        // Chiffre fictif pour les partenaires (à adapter plus tard)
        long totalPartenaires = 3; 

        return ResponseEntity.ok(Map.of(
            "hackathons", totalHackathons,
            "utilisateurs", totalUtilisateurs,
            "partenaires", totalPartenaires
        ));
    }
}
