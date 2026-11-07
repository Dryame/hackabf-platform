package bf.hackabf.backend.controller;

import bf.hackabf.backend.dto.UtilisateurResponse;
import bf.hackabf.backend.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')") // Sécurité : Seul l'Admin peut accéder à ce contrôleur
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/utilisateurs")
    public ResponseEntity<List<UtilisateurResponse>> getAllUtilisateurs() {
        return ResponseEntity.ok(adminService.getAllUtilisateurs());
    }

    @PutMapping("/utilisateurs/{id}/role")
    public ResponseEntity<UtilisateurResponse> changerRole(@PathVariable String id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(adminService.changerRole(id, body.get("role")));
    }

    @PutMapping("/utilisateurs/{id}/status")
    public ResponseEntity<UtilisateurResponse> toggleStatus(@PathVariable String id) {
        return ResponseEntity.ok(adminService.toggleStatus(id));
    }
}
