package bf.hackabf.backend.service;

import bf.hackabf.backend.dto.HackathonRequest;
import bf.hackabf.backend.dto.HackathonResponse;
import bf.hackabf.backend.entity.Categorie;
import bf.hackabf.backend.entity.Hackathon;
import bf.hackabf.backend.entity.Utilisateur;
import bf.hackabf.backend.repository.CategorieRepository;
import bf.hackabf.backend.repository.HackathonRepository;
import bf.hackabf.backend.repository.UtilisateurRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HackathonService {

    private final HackathonRepository hackathonRepository;
    private final CategorieRepository categorieRepository;
    private final UtilisateurRepository utilisateurRepository;

    public HackathonService(HackathonRepository hackathonRepository, CategorieRepository categorieRepository, UtilisateurRepository utilisateurRepository) {
        this.hackathonRepository = hackathonRepository;
        this.categorieRepository = categorieRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public Page<HackathonResponse> getAllHackathons(int page, int size) {
        return hackathonRepository.findByEstValideTrueOrderByDateDebutDesc(PageRequest.of(page, size)).map(this::mapToResponse);
    }

    public List<HackathonResponse> getVainqueurs() {
        return hackathonRepository.findByEstValideTrueAndVainqueurIsNotNullOrderByDateFinDesc().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public HackathonResponse getHackathonById(String id) {
        return mapToResponse(hackathonRepository.findById(id).orElseThrow(() -> new RuntimeException("Hackathon introuvable")));
    }

    public List<HackathonResponse> getHackathonsByUser(String email) {
        return hackathonRepository.findByCreateurEmailOrderByDateDebutDesc(email).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public HackathonResponse createHackathon(HackathonRequest request, String userEmail) {
        Utilisateur createur = utilisateurRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        
        Categorie categorie = null;
        if (request.getCategorieId() != null) {
            categorie = categorieRepository.findById(request.getCategorieId()).orElse(null);
        }

        Hackathon hackathon = new Hackathon();
        hackathon.setTitre(request.getTitre() != null && !request.getTitre().isEmpty() ? request.getTitre() : "Hackathon sans titre");
        hackathon.setDescription(request.getDescription());
        hackathon.setDateDebut(request.getDateDebut());
        hackathon.setDateFin(request.getDateFin());
        hackathon.setLieu(request.getLieu());
        hackathon.setFormat(request.getFormat() != null ? request.getFormat() : "PRESENTIEL");
        hackathon.setLienInscription(request.getLienInscription());
        hackathon.setImageUrl(request.getImageUrl());
        hackathon.setTypeInscription(request.getTypeInscription() != null ? request.getTypeInscription() : "EXTERNE");
        hackathon.setFormulaireConfig(request.getFormulaireConfig());
        hackathon.setCodeSecretHex(request.getCodeSecretHex());
        hackathon.setCategorie(categorie);
        hackathon.setCreateur(createur);
        hackathon.setStatut("A_VENIR");
        hackathon.setEstValide(true);

        Hackathon saved = hackathonRepository.save(hackathon);
        return mapToResponse(saved);
    }

    public List<HackathonResponse> getHackathonsEnAttente() {
        return hackathonRepository.findByEstValideFalse().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public HackathonResponse validerHackathon(String id) {
        Hackathon hackathon = hackathonRepository.findById(id).orElseThrow(() -> new RuntimeException("Hackathon introuvable"));
        hackathon.setEstValide(true);
        return mapToResponse(hackathonRepository.save(hackathon));
    }

    public HackathonResponse designerVainqueur(String id, String vainqueur, String imageVainqueur, String email) {
        Hackathon hackathon = hackathonRepository.findById(id).orElseThrow(() -> new RuntimeException("Hackathon introuvable"));
        if (!hackathon.getCreateur().getEmail().equals(email)) throw new RuntimeException("Non autorisé.");
        hackathon.setVainqueur(vainqueur);
        hackathon.setImageVainqueur(imageVainqueur);
        return mapToResponse(hackathonRepository.save(hackathon));
    }

    public HackathonResponse changerStatut(String id, String nouveauStatut, String codeSecret, String email) {
        Hackathon hackathon = hackathonRepository.findById(id).orElseThrow(() -> new RuntimeException("Hackathon introuvable"));
        if (!hackathon.getCreateur().getEmail().equals(email)) throw new RuntimeException("Non autorisé.");
        if (!hackathon.getCodeSecretHex().equals(codeSecret)) throw new RuntimeException("Code secret invalide.");
        hackathon.setStatut(nouveauStatut);
        return mapToResponse(hackathonRepository.save(hackathon));
    }

    public void supprimerHackathon(String id, String codeSecret, String email) {
        Hackathon hackathon = hackathonRepository.findById(id).orElseThrow(() -> new RuntimeException("Hackathon introuvable"));
        if (!hackathon.getCreateur().getEmail().equals(email)) throw new RuntimeException("Non autorisé.");
        if (!hackathon.getCodeSecretHex().equals(codeSecret)) throw new RuntimeException("Code secret invalide.");
        hackathonRepository.delete(hackathon);
    }

    // ==========================================
    // NOUVELLES MÉTHODES ADMIN
    // ==========================================
    public List<HackathonResponse> getAllHackathonsAdmin() {
        return hackathonRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public void supprimerHackathonAdmin(String id) {
        if (!hackathonRepository.existsById(id)) throw new RuntimeException("Hackathon introuvable");
        hackathonRepository.deleteById(id);
    }

    public HackathonResponse changerStatutAdmin(String id, String nouveauStatut) {
        Hackathon hackathon = hackathonRepository.findById(id).orElseThrow(() -> new RuntimeException("Hackathon introuvable"));
        hackathon.setStatut(nouveauStatut);
        return mapToResponse(hackathonRepository.save(hackathon));
    }

    private HackathonResponse mapToResponse(Hackathon hackathon) {
        HackathonResponse response = new HackathonResponse();
        response.setId(hackathon.getId());
        response.setTitre(hackathon.getTitre());
        response.setDescription(hackathon.getDescription());
        response.setDateDebut(hackathon.getDateDebut());
        response.setDateFin(hackathon.getDateFin());
        response.setLieu(hackathon.getLieu());
        response.setFormat(hackathon.getFormat());
        response.setLienInscription(hackathon.getLienInscription());
        response.setStatut(hackathon.getStatut());
        response.setVainqueur(hackathon.getVainqueur());
        response.setImageVainqueur(hackathon.getImageVainqueur());
        response.setImageUrl(hackathon.getImageUrl());
        response.setTypeInscription(hackathon.getTypeInscription());
        response.setFormulaireConfig(hackathon.getFormulaireConfig());
        response.setEstValide(hackathon.isEstValide());
        if (hackathon.getCategorie() != null) response.setCategorieNom(hackathon.getCategorie().getNom());
        if (hackathon.getCreateur() != null) response.setCreateurNom(hackathon.getCreateur().getNomComplet());
        return response;
    }
}
