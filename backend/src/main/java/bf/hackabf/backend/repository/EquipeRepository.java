package bf.hackabf.backend.repository;

import bf.hackabf.backend.entity.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, String> {
    Optional<Equipe> findByCodeInvitation(String codeInvitation);
    List<Equipe> findByHackathonId(String hackathonId);
}
