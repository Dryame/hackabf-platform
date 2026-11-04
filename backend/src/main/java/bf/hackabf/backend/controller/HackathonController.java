package bf.hackabf.backend.controller;

import bf.hackabf.backend.dto.HackathonRequest;
import bf.hackabf.backend.dto.HackathonResponse;
import bf.hackabf.backend.service.HackathonService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class HackathonController {

    private final HackathonService hackathonService;

    public HackathonController(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    // Route PUBLIQUE avec pagination (page=0, size=10 par défaut)
    @GetMapping("/public/hackathons")
    public ResponseEntity<Page<HackathonResponse>> getAllHackathons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(hackathonService.getAllHackathons(page, size));
    }

    // Route PRIVÉE (Editeur/Admin uniquement)
    @PostMapping("/hackathons")
    @PreAuthorize("hasAnyRole('EDITEUR', 'ADMIN')")
    public ResponseEntity<HackathonResponse> createHackathon(@Valid @RequestBody HackathonRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(hackathonService.createHackathon(request, userEmail));
    }
}
