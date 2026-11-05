/**
 * Payload pour la requête de connexion.
 */
export interface LoginRequest {
  email: string;
  motDePasse: string;
}

/**
 * Payload pour la requête d'inscription.
 */
export interface RegisterRequest {
  nomComplet: string;
  email: string;
  motDePasse: string;
  etablissement: string;
  niveauEtude: string;
  ville: string;
}

/**
 * Réponse du backend contenant le Token JWT.
 */
export interface AuthResponse {
  token: string;
}
