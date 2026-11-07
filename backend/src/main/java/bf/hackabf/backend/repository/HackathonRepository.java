package bf.hackabf.backend.repository;

import bf.hackabf.backend.entity.Hackathon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, String> {
    Page<Hackathon> findByEstValideTrueOrderByDateDebutDesc(Pageable pageable);
    List<Hackathon> findByEstValideTrueAndVainqueurIsNotNullOrderByDateFinDesc();
    List<Hackathon> findByCreateurEmailOrderByDateDebutDesc(String email);
    List<Hackathon> findByEstValideFalse();
}
