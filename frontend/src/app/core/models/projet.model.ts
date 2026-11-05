export interface Projet {
  id: string;
  titre: string;
  description: string;
  typeProjet: string;
  niveauRequis: string;
  competencesCibles: string[];
  anneeAcademique: string;
  categorieNom: string;
  createurNom: string;
  imageUrl?: string;
}
