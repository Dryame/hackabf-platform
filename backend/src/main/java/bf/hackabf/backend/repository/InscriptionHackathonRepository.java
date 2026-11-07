package bf.hackabf.backend.repository;

import bf.hackabf.backend.entity.InscriptionHackathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionHackathonRepository extends JpaRepository<InscriptionHackathon, String> {
    List<InscriptionHackathon> findByHackathonId(String hackathonId);
    List<InscriptionHackathon> findByEtudiantId(String etudiantId);
    Optional<InscriptionHackathon> findByEtudiantIdAndHackathonId(String etudiantId, String hackathonId);
}
