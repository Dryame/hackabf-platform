package bf.hackabf.backend.controller;

import bf.hackabf.backend.dto.InscriptionFormulaireRequest;
import bf.hackabf.backend.dto.InscriptionResponse;
import bf.hackabf.backend.service.InscriptionFormulaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hackathons/{hackathonId}/inscriptions")
public class InscriptionFormulaireController {

    private final InscriptionFormulaireService inscriptionService;

    public InscriptionFormulaireController(InscriptionFormulaireService inscriptionService) {
        this.inscriptionService = inscriptionService;
    }

    @PostMapping
    public ResponseEntity<InscriptionResponse> soumettre(@PathVariable String hackathonId, @RequestBody InscriptionFormulaireRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(InscriptionResponse.from(
                inscriptionService.soumettreFormulaire(hackathonId, userEmail, request)));
    }

    // Nouvelle route : L'organisateur consulte les formulaires remplis
    @GetMapping
    public ResponseEntity<List<InscriptionResponse>> getInscriptions(@PathVariable String hackathonId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(inscriptionService.getInscriptionsOrganisateur(hackathonId, userEmail).stream()
                .map(InscriptionResponse::from)
                .toList());
    }
}
