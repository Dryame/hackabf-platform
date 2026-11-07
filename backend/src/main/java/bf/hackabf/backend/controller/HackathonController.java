package bf.hackabf.backend.controller;

import bf.hackabf.backend.dto.HackathonRequest;
import bf.hackabf.backend.dto.HackathonResponse;
import bf.hackabf.backend.service.HackathonService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HackathonController {

    private final HackathonService hackathonService;

    public HackathonController(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    @GetMapping("/public/hackathons")
    public ResponseEntity<Page<HackathonResponse>> getAllHackathons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(hackathonService.getAllHackathons(page, size));
    }

    @GetMapping("/public/hackathons/vainqueurs")
    public ResponseEntity<List<HackathonResponse>> getVainqueurs() {
        return ResponseEntity.ok(hackathonService.getVainqueurs());
    }

    @GetMapping("/public/hackathons/{id}")
    public ResponseEntity<HackathonResponse> getHackathonById(@PathVariable String id) {
        return ResponseEntity.ok(hackathonService.getHackathonById(id));
    }

    @GetMapping("/hackathons/mes-publications")
    public ResponseEntity<List<HackathonResponse>> getMesHackathons() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(hackathonService.getHackathonsByUser(email));
    }

    @PutMapping("/hackathons/{id}/statut")
    @PreAuthorize("hasAnyRole('EDITEUR', 'ADMIN')")
    public ResponseEntity<HackathonResponse> changerStatut(@PathVariable String id, @RequestBody Map<String, String> body) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(hackathonService.changerStatut(id, body.get("statut"), body.get("codeSecret"), email));
    }

    @DeleteMapping("/hackathons/{id}")
    @PreAuthorize("hasAnyRole('EDITEUR', 'ADMIN')")
    public ResponseEntity<?> supprimerHackathon(@PathVariable String id, @RequestBody Map<String, String> body) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        hackathonService.supprimerHackathon(id, body.get("codeSecret"), email);
        return ResponseEntity.ok(Map.of("message", "Supprimé avec succès"));
    }

    @GetMapping("/admin/hackathons/en-attente")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HackathonResponse>> getEnAttente() {
        return ResponseEntity.ok(hackathonService.getHackathonsEnAttente());
    }

    @PutMapping("/admin/hackathons/{id}/valider")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HackathonResponse> valider(@PathVariable String id) {
        return ResponseEntity.ok(hackathonService.validerHackathon(id));
    }

    @PostMapping("/hackathons")
    @PreAuthorize("hasAnyRole('EDITEUR', 'ADMIN')")
    public ResponseEntity<HackathonResponse> createHackathon(@Valid @RequestBody HackathonRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(hackathonService.createHackathon(request, userEmail));
    }

    @PutMapping("/hackathons/{id}/vainqueur")
    @PreAuthorize("hasAnyRole('EDITEUR', 'ADMIN')")
    public ResponseEntity<HackathonResponse> designerVainqueur(@PathVariable String id, @RequestBody Map<String, String> body) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(hackathonService.designerVainqueur(id, body.get("vainqueur"), body.get("imageVainqueur"), email));
    }
}
