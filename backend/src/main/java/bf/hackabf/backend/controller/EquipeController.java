package bf.hackabf.backend.controller;

import bf.hackabf.backend.entity.Equipe;
import bf.hackabf.backend.service.EquipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    private final EquipeService equipeService;

    public EquipeController(EquipeService equipeService) {
        this.equipeService = equipeService;
    }

    @PostMapping("/{hackathonId}")
    public ResponseEntity<Equipe> creerEquipe(@PathVariable String hackathonId, @RequestBody Map<String, String> body) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(equipeService.creerEquipe(hackathonId, body.get("nom"), email));
    }

    @PostMapping("/rejoindre")
    public ResponseEntity<Equipe> rejoindreEquipe(@RequestBody Map<String, String> body) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(equipeService.rejoindreEquipe(body.get("code"), email));
    }

    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<Equipe>> getEquipes(@PathVariable String hackathonId) {
        return ResponseEntity.ok(equipeService.getEquipesParHackathon(hackathonId));
    }
}
