# 🚀 HackaBF - Plateforme de Référentiel Tech & Hackathons

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-green)
![Angular](https://img.shields.io/badge/Angular-18-red)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![Security](https://img.shields.io/badge/Security-ECC_|_JWT-success)

## 📖 À propos du projet

**HackaBF** est une plateforme centralisée conçue pour dynamiser et structurer l'écosystème technologique du Burkina Faso. Elle sert de référentiel national pour les hackathons, les projets de fin d'études (PFA/PFE) et les défis de perfectionnement.

Le projet met un point d'honneur sur **la sécurité de niveau industriel**, en utilisant la cryptographie asymétrique (Courbes Elliptiques) pour protéger les données et authentifier les utilisateurs, tout en offrant une architecture conteneurisée et hautement observable.

### ✨ Fonctionnalités Principales
*   **Référentiel de Hackathons :** Catalogue public filtrable par catégories (Data, Cyber, Web, etc.) et par ville.
*   **Gestion des Rôles :** Système d'accréditation strict (Étudiant, Éditeur, Admin).
*   **Inscription Fluide :** Redirection sécurisée vers les liens d'inscription officiels sans friction.
*   **Partenaires Institutionnels :** Mise en avant des acteurs clés (Ministère de la Transition Digitale, EPO, BLCC).
*   **Monitoring en Temps Réel :** Suivi des performances de l'API et de la base de données via Prometheus et Grafana.

---

## 🛠️ Stack Technique

### Backend
*   **Langage :** Java 21
*   **Framework :** Spring Boot 3.x (Spring Web, Spring Data JPA, Spring Security)
*   **Base de données :** PostgreSQL 15
*   **Documentation API :** Swagger / OpenAPI 3

### Sécurité & Cryptographie
*   **Chiffrement des données sensibles :** Bouncy Castle (ECIES - Elliptic Curve Integrated Encryption Scheme).
*   **Authentification :** Tokens JWT signés avec l'algorithme **ES256 (ECDSA)**.
*   **Hachage des mots de passe :** BCrypt.

### Frontend
*   **Framework :** Angular 18+ (Standalone Components)
*   **Design & UI :** Tailwind CSS, Spartan UI (Headless Components)
*   **Typage :** TypeScript strict

### Infrastructure & DevOps
*   **Conteneurisation :** Docker & Docker Compose
*   **Monitoring :** Prometheus (Collecte) & Grafana (Visualisation)
*   **Serveur Web (Frontend) :** Nginx

---

## 📂 Architecture du Dépôt

Le projet est structuré sous forme de Monorepo :

```text
hackabf-platform/
│
├── backend/               # Code source Spring Boot (API REST)
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/              # Code source Angular (Interface Utilisateur)
│   ├── src/
│   ├── Dockerfile
│   └── nginx.conf
│
├── docker-compose.yml     # Orchestration de l'infrastructure complète
├── prometheus.yml         # Configuration de la collecte des métriques
└── README.md
