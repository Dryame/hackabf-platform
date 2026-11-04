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

@Service
public class HackathonService {

    private final HackathonRepository hackathonRepository;
    private final CategorieRepository categorieRepository;
    private final UtilisateurRepository utilisateurRepository;

    public HackathonService(HackathonRepository hackathonRepository, 
                            CategorieRepository categorieRepository,
                            UtilisateurRepository utilisateurRepository) {
        this.hackathonRepository = hackathonRepository;
        this.categorieRepository = categorieRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public Page<HackathonResponse> getAllHackathons(int page, int size) {
        // On limite à 10 par défaut comme demandé
        return hackathonRepository.findAllByOrderByDateDebutDesc(PageRequest.of(page, size))
                .map(this::mapToResponse);
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
        hackathon.setCategorie(categorie);
        hackathon.setCreateur(createur);
        hackathon.setStatut("A_VENIR");

        Hackathon saved = hackathonRepository.save(hackathon);
        return mapToResponse(saved);
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
        
        if (hackathon.getCategorie() != null) {
            response.setCategorieNom(hackathon.getCategorie().getNom());
        }
        if (hackathon.getCreateur() != null) {
            response.setCreateurNom(hackathon.getCreateur().getNomComplet());
        }
        return response;
    }
}
