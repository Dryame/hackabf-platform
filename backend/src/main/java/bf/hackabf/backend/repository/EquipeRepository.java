package bf.hackabf.backend.repository;

import bf.hackabf.backend.entity.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, String> {
    Optional<Equipe> findByCodeInvitation(String codeInvitation);
=======

import java.util.List;
import java.util.Optional;

public interface EquipeRepository extends JpaRepository<Equipe, String> {
>>>>>>> 25db076 (fix(security): complete phase 1 critical hardening)
    List<Equipe> findByHackathonId(String hackathonId);
    Optional<Equipe> findByCodeInvitation(String codeInvitation);
}
