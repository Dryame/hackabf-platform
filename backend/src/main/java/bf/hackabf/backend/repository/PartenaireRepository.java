package bf.hackabf.backend.repository;

import bf.hackabf.backend.entity.Partenaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartenaireRepository extends JpaRepository<Partenaire, Long> {
    List<Partenaire> findByEstActifTrueOrderByOrdreAffichageAsc();
}
