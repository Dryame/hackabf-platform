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
    private final NotificationService notificationService;

    public HackathonService(HackathonRepository hackathonRepository, 
                            CategorieRepository categorieRepository,
                            UtilisateurRepository utilisateurRepository,
                            NotificationService notificationService) {
        this.hackathonRepository = hackathonRepository;
        this.categorieRepository = categorieRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.notificationService = notificationService;
    }

    public Page<HackathonResponse> getAllHackathons(int page, int size) {
        return hackathonRepository.findAllByOrderByDateDebutDesc(PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    public List<HackathonResponse> getVainqueurs() {
        return hackathonRepository.findAllByVainqueurIsNotNullOrderByDateFinDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public HackathonResponse getHackathonById(String id) {
        Hackathon hackathon = hackathonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hackathon introuvable avec l'id : " + id));
        return mapToResponse(hackathon);
    }

    public List<HackathonResponse> getHackathonsByUser(String email) {
        return hackathonRepository.findByCreateurEmailOrderByDateDebutDesc(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public HackathonResponse createHackathon(HackathonRequest request, String userEmail) {
        Utilisateur createur = utilisateurRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Categorie categorie = categorieRepository.findById(request.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));

        Hackathon hackathon = new Hackathon();
        hackathon.setTitre(request.getTitre());
        hackathon.setDescription(request.getDescription());
        hackathon.setDateDebut(request.getDateDebut());
        hackathon.setDateFin(request.getDateFin());
        hackathon.setLieu(request.getLieu());
        hackathon.setFormat(request.getFormat());
        hackathon.setLienInscription(request.getLienInscription());
        hackathon.setImageUrl(request.getImageUrl());
        hackathon.setCategorie(categorie);
        hackathon.setCreateur(createur);
        hackathon.setStatut("A_VENIR");

        Hackathon saved = hackathonRepository.save(hackathon);
        
        notificationService.envoyerEmailNouveauHackathon(saved.getTitre(), saved.getDescription(), "etudiant.test@hackabf.bf");

        return mapToResponse(saved);
    }

    public HackathonResponse designerVainqueur(String id, String vainqueur, String imageVainqueur, String email) {
        Hackathon hackathon = hackathonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hackathon introuvable"));
        
        // Sécurité : Seul le créateur ou un admin peut désigner le vainqueur
        if (!hackathon.getCreateur().getEmail().equals(email)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à modifier ce hackathon.");
        }

        hackathon.setVainqueur(vainqueur);
        hackathon.setImageVainqueur(imageVainqueur);
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
        
        if (hackathon.getCategorie() != null) {
            response.setCategorieNom(hackathon.getCategorie().getNom());
        }
        if (hackathon.getCreateur() != null) {
            response.setCreateurNom(hackathon.getCreateur().getNomComplet());
        }
        return response;
    }
}
