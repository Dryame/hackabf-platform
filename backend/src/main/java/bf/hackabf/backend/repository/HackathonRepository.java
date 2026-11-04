package bf.hackabf.backend.repository;

import bf.hackabf.backend.entity.Hackathon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, String> {
    // Remplacement de la List par Page pour gérer la pagination
    Page<Hackathon> findAllByOrderByDateDebutDesc(Pageable pageable);
}
