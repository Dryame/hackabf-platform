package bf.hackabf.backend.service;

import bf.hackabf.backend.dto.HackathonRequest;
import bf.hackabf.backend.dto.HackathonResponse;
import bf.hackabf.backend.entity.Categorie;
import bf.hackabf.backend.entity.Hackathon;
import bf.hackabf.backend.entity.Utilisateur;
import bf.hackabf.backend.repository.CategorieRepository;
import bf.hackabf.backend.repository.HackathonRepository;
import bf.hackabf.backend.repository.UtilisateurRepository;
import bf.hackabf.backend.exception.ForbiddenException;
import bf.hackabf.backend.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HackathonService {

    private final HackathonRepository hackathonRepository;
    private final CategorieRepository categorieRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public HackathonService(HackathonRepository hackathonRepository, CategorieRepository categorieRepository,
                            UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.hackathonRepository = hackathonRepository;
        this.categorieRepository = categorieRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<HackathonResponse> getAllHackathons(int page, int size) {
        return hackathonRepository.findByEstValideTrueOrderByDateDebutDesc(PageRequest.of(page, size)).map(this::mapToResponse);
    }

    public List<HackathonResponse> getVainqueurs() {
        return hackathonRepository.findByEstValideTrueAndVainqueurIsNotNullOrderByDateFinDesc().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public HackathonResponse getHackathonById(String id) {
        return mapToResponse(hackathonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Hackathon introuvable")));
    }

    public List<HackathonResponse> getHackathonsByUser(String email) {
        return hackathonRepository.findByCreateurEmailOrderByDateDebutDesc(email).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public HackathonResponse createHackathon(HackathonRequest request, String userEmail) {
        Utilisateur createur = utilisateurRepository.findByEmail(userEmail).orElseThrow(() ->
                new ResourceNotFoundException("Utilisateur introuvable"));
        Categorie categorie = categorieRepository.findById(request.getCategorieId()).orElseThrow(() ->
                new ResourceNotFoundException("Catégorie introuvable"));

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
        if (request.getCodeSecretHex() == null || request.getCodeSecretHex().isBlank()) {
            throw new IllegalArgumentException("Le code secret est obligatoire.");
        }
        hackathon.setCodeSecretHex(passwordEncoder.encode(request.getCodeSecretHex()));
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

    @Transactional
    public HackathonResponse validerHackathon(String id) {
        Hackathon hackathon = hackathonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Hackathon introuvable"));
        hackathon.setEstValide(true);
        return mapToResponse(hackathonRepository.save(hackathon));
    }

    @Transactional
    public HackathonResponse designerVainqueur(String id, String vainqueur, String imageVainqueur, String email) {
        Hackathon hackathon = hackathonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Hackathon introuvable"));
        verifyCreatorOrAdmin(hackathon, email);
        hackathon.setVainqueur(vainqueur);
        hackathon.setImageVainqueur(imageVainqueur);
        return mapToResponse(hackathonRepository.save(hackathon));
    }

    @Transactional
    public HackathonResponse changerStatut(String id, String nouveauStatut, String codeSecret, String email) {
        Hackathon hackathon = hackathonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Hackathon introuvable"));
        verifyCreatorOrAdmin(hackathon, email);
        if (hackathon.getCodeSecretHex() == null
                || codeSecret == null
                || !passwordEncoder.matches(codeSecret, hackathon.getCodeSecretHex())) {
            throw new IllegalArgumentException("Code secret invalide.");
        }
        hackathon.setStatut(nouveauStatut);
        return mapToResponse(hackathonRepository.save(hackathon));
    }

    @Transactional
    public void supprimerHackathon(String id, String codeSecret, String email) {
        Hackathon hackathon = hackathonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Hackathon introuvable"));
        verifyCreatorOrAdmin(hackathon, email);
        if (hackathon.getCodeSecretHex() == null
                || codeSecret == null
                || !passwordEncoder.matches(codeSecret, hackathon.getCodeSecretHex())) {
            throw new IllegalArgumentException("Code secret invalide.");
        }
        hackathonRepository.delete(hackathon);
    }

    @Transactional(readOnly = true)
    public List<HackathonResponse> getAllHackathonsAdmin() {
        return hackathonRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public void supprimerHackathonAdmin(String id) {
        if (!hackathonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hackathon introuvable");
        }
        hackathonRepository.deleteById(id);
    }

    @Transactional
    public HackathonResponse changerStatutAdmin(String id, String nouveauStatut) {
        Hackathon hackathon = hackathonRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Hackathon introuvable"));
        hackathon.setStatut(nouveauStatut);
        return mapToResponse(hackathonRepository.save(hackathon));
    }

    private void verifyCreatorOrAdmin(Hackathon hackathon, String email) {
        boolean creator = hackathon.getCreateur() != null
                && hackathon.getCreateur().getEmail().equals(email);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean admin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (!creator && !admin) {
            throw new ForbiddenException("Non autorisé.");
        }
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
