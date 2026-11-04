package bf.hackabf.backend.service;

import bf.hackabf.backend.dto.AuthResponse;
import bf.hackabf.backend.dto.LoginRequest;
import bf.hackabf.backend.dto.RegisterRequest;
import bf.hackabf.backend.entity.ERole;
import bf.hackabf.backend.entity.Role;
import bf.hackabf.backend.entity.Utilisateur;
import bf.hackabf.backend.repository.RoleRepository;
import bf.hackabf.backend.repository.UtilisateurRepository;
import bf.hackabf.backend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthService(UtilisateurRepository utilisateurRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse register(RegisterRequest request) {
        // Vérification si l'email existe déjà
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Erreur : Cet email est déjà utilisé.");
        }

        // Création du nouvel utilisateur
        Utilisateur user = new Utilisateur();
        user.setNomComplet(request.getNomComplet());
        user.setEmail(request.getEmail());
        user.setMotDePasse(passwordEncoder.encode(request.getMotDePasse())); // Hashage Bcrypt
        user.setEtablissement(request.getEtablissement());
        user.setNiveauEtude(request.getNiveauEtude());
        user.setVille(request.getVille());

        // Attribution du rôle par défaut (ETUDIANT)
        Role roleEtudiant = roleRepository.findByNom(ERole.ROLE_ETUDIANT)
                .orElseThrow(() -> new RuntimeException("Erreur : Le rôle par défaut n'existe pas."));
        Set<Role> roles = new HashSet<>();
        roles.add(roleEtudiant);
        user.setRoles(roles);

        utilisateurRepository.save(user);

        // Génération du token
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);

        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(LoginRequest request) {
        // Authentification via Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );

        // Si on arrive ici, c'est que le mot de passe est correct
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);

        return new AuthResponse(jwtToken);
    }
}
