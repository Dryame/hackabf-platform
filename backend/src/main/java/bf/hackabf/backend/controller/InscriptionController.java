package bf.hackabf.backend.controller;

import bf.hackabf.backend.entity.InscriptionHackathon;
import bf.hackabf.backend.service.InscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscriptions")
public class InscriptionController {

    private final InscriptionService inscriptionService;

    public InscriptionController(InscriptionService inscriptionService) {
        this.inscriptionService = inscriptionService;
    }

    @PostMapping("/{hackathonId}")
    public ResponseEntity<?> sInscrire(@PathVariable String hackathonId, @RequestBody(required = false) Map<String, String> reponses) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        inscriptionService.inscrire(hackathonId, email, reponses);
        return ResponseEntity.ok(Map.of("message", "Inscription réussie !"));
    }

    @GetMapping("/mes-inscriptions")
    public ResponseEntity<List<InscriptionHackathon>> getMesInscriptions() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(inscriptionService.getMesInscriptions(email));
    }

    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<InscriptionHackathon>> getInscriptionsHackathon(@PathVariable String hackathonId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(inscriptionService.getInscriptionsByHackathon(hackathonId, email));
    }
}
