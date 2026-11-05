/**
 * Interface représentant un Partenaire (Ministère, EPO, BLCC, etc.).
 */
export interface Partenaire {
  id: number;
  nom: string;
  logoUrl: string;
  siteWeb: string;
  typePartenaire: string;
  ordreAffichage: number;
}
