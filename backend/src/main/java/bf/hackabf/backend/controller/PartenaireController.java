package bf.hackabf.backend.controller;

import bf.hackabf.backend.entity.Partenaire;
import bf.hackabf.backend.repository.PartenaireRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PartenaireController {

    private final PartenaireRepository partenaireRepository;

    public PartenaireController(PartenaireRepository partenaireRepository) {
        this.partenaireRepository = partenaireRepository;
    }

    // PUBLIC : Tout le monde peut voir les partenaires actifs
    @GetMapping("/public/partenaires")
    public ResponseEntity<List<Partenaire>> getPartenairesActifs() {
        return ResponseEntity.ok(partenaireRepository.findByEstActifTrueOrderByOrdreAffichageAsc());
    }

    // ADMIN : Ajouter un partenaire (EPO, BLCC, Ministère...)
    @PostMapping("/admin/partenaires")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Partenaire> createPartenaire(@RequestBody Partenaire partenaire) {
        return ResponseEntity.ok(partenaireRepository.save(partenaire));
    }
}
