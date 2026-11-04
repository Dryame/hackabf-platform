package bf.hackabf.backend.repository;

import bf.hackabf.backend.entity.ERole;
import bf.hackabf.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNom(ERole nom);
}
