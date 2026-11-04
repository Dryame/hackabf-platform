package bf.hackabf.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "partenaires")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partenaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String logoUrl;
    private String siteWeb;
    private String typePartenaire;
    private Integer ordreAffichage = 0;
    private boolean estActif = true;
}
