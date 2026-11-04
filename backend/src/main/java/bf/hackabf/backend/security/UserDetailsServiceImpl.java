package bf.hackabf.backend.security;

import bf.hackabf.backend.entity.Utilisateur;
import bf.hackabf.backend.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service utilisé par Spring Security lors du processus d'authentification
 * pour charger les informations d'un utilisateur depuis la base de données.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    // Injection par constructeur (Bonne pratique Spring : favorise l'immutabilité et les tests)
    public UserDetailsServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Charge un utilisateur par son email.
     *
     * @param email L'email de l'utilisateur tentant de se connecter.
     * @return Un objet UserDetails contenant les informations de l'utilisateur.
     * @throws UsernameNotFoundException si aucun utilisateur ne correspond à l'email.
     */
    @Override
    @Transactional(readOnly = true) // Optimisation pour les requêtes de lecture
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable avec l'email : " + email));

        return new UserDetailsImpl(utilisateur);
    }
}
