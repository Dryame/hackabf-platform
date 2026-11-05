export interface Hackathon {
  id: string;
  titre: string;
  description: string;
  dateDebut: string;
  dateFin: string;
  lieu: string;
  format: 'PRESENTIEL' | 'EN_LIGNE' | 'HYBRIDE';
  lienInscription: string;
  statut: 'A_VENIR' | 'EN_COURS' | 'TERMINE';
  categorieNom: string;
  createurNom: string;
  vainqueur?: string;
  imageVainqueur?: string;
  imageUrl?: string;
}
