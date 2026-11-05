package bf.hackabf.backend.repository;

import bf.hackabf.backend.entity.Hackathon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, String> {
    Page<Hackathon> findAllByOrderByDateDebutDesc(Pageable pageable);
    
    // Récupérer les 3 derniers hackathons qui ont un vainqueur
    List<Hackathon> findAllByVainqueurIsNotNullOrderByDateFinDesc();
    List<Hackathon> findByCreateurEmailOrderByDateDebutDesc(String email);
}
