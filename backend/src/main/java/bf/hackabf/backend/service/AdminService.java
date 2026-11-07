package bf.hackabf.backend.service;

import bf.hackabf.backend.dto.UtilisateurResponse;
import bf.hackabf.backend.entity.ERole;
import bf.hackabf.backend.entity.Role;
import bf.hackabf.backend.entity.Utilisateur;
import bf.hackabf.backend.repository.RoleRepository;
import bf.hackabf.backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;

    public AdminService(UtilisateurRepository utilisateurRepository, RoleRepository roleRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
    }

    public List<UtilisateurResponse> getAllUtilisateurs() {
        return utilisateurRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public UtilisateurResponse changerRole(String id, String roleName) {
        Utilisateur user = utilisateurRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        ERole eRole = ERole.valueOf(roleName);
        Role role = roleRepository.findByNom(eRole).orElseThrow(() -> new RuntimeException("Rôle introuvable"));
        
        user.getRoles().clear();
        user.getRoles().add(role);
        return mapToResponse(utilisateurRepository.save(user));
    }

    public UtilisateurResponse toggleStatus(String id) {
        Utilisateur user = utilisateurRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        user.setEstActif(!user.isEstActif());
        return mapToResponse(utilisateurRepository.save(user));
    }

    private UtilisateurResponse mapToResponse(Utilisateur user) {
        UtilisateurResponse res = new UtilisateurResponse();
        res.setId(user.getId());
        res.setNomComplet(user.getNomComplet());
        res.setEmail(user.getEmail());
        res.setEtablissement(user.getEtablissement());
        res.setEstActif(user.isEstActif());
        res.setStatutAccreditation(user.getStatutAccreditation());
        res.setRoles(user.getRoles().stream().map(r -> r.getNom().name()).collect(Collectors.toSet()));
        return res;
    }
}
