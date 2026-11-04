package bf.hackabf.backend.repository;

import bf.hackabf.backend.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface de gestion des opérations de base de données pour l'entité {@link Utilisateur}.
 * Étend JpaRepository pour bénéficier des méthodes CRUD standard (save, findAll, delete, etc.).
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {

    /**
     * Recherche un utilisateur par son adresse email.
     *
     * @param email L'adresse email de l'utilisateur.
     * @return Un {@link Optional} contenant l'utilisateur s'il existe, sinon vide.
     */
    Optional<Utilisateur> findByEmail(String email);
}
