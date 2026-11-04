package bf.hackabf.backend.security;

import bf.hackabf.backend.entity.Utilisateur;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implémentation personnalisée de {@link UserDetails}.
 * Permet à Spring Security de comprendre notre entité métier {@link Utilisateur}
 * et d'extraire ses rôles pour gérer les autorisations.
 */
public class UserDetailsImpl implements UserDetails {

    private final Utilisateur utilisateur;

    public UserDetailsImpl(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    /**
     * Convertit les rôles de notre base de données (ERole) en autorités Spring Security.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return utilisateur.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getNom().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return utilisateur.getMotDePasse();
    }

    @Override
    public String getUsername() {
        return utilisateur.getEmail();
    }

    // --- Gestion du cycle de vie du compte (toujours vrai pour ce projet) ---

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return utilisateur.isEstActif();
    }
}
