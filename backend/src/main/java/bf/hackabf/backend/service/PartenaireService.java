package bf.hackabf.backend.service;

import bf.hackabf.backend.entity.Partenaire;
import bf.hackabf.backend.exception.ResourceNotFoundException;
import bf.hackabf.backend.repository.PartenaireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PartenaireService {
    private final PartenaireRepository partenaireRepository;

    public PartenaireService(PartenaireRepository partenaireRepository) {
        this.partenaireRepository = partenaireRepository;
    }

    public List<Partenaire> getPartenairesActifs() {
        return partenaireRepository.findByEstActifTrueOrderByOrdreAffichageAsc();
    }

    @Transactional
    public Partenaire createPartenaire(Partenaire partenaire) {
        return partenaireRepository.save(partenaire);
    }

    @Transactional
    public void deletePartenaire(Long id) {
        Partenaire partenaire = partenaireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partenaire introuvable"));
        partenaireRepository.delete(partenaire);
    }
}
