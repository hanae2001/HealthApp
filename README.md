# AppSanté — Plateforme de gestion médicale

Application web fullstack de prise de rendez-vous médicaux en ligne (Maroc).

---

## Prérequis

| Outil | Version minimale |
|-------|-----------------|
| Java | 17 |
| Maven | 3.8+ (ou utiliser le wrapper `mvnw`) |
| Node.js | 18+ |
| Docker + Docker Compose | 24+ |
| MySQL | 8.0 (via Docker) |

---

## Démarrage rapide (Docker recommandé)

### 1. Cloner le projet

```bash
git clone <url-du-repo>
cd HealthApp-main
```

### 2. Initialiser la base de données

```bash
# Démarrer uniquement MySQL
docker-compose up -d db

# Attendre ~10 secondes que MySQL soit prêt, puis injecter le schéma
docker exec -i healthapp-main-db-1 mysql -u root app_dentaire < Architecture/database.sql
```

> Cela crée 21 tables, un trigger automatique, et charge les données de référence (villes, secteurs, domaines médicaux…).

### 3. Démarrer le backend

```bash
docker-compose up -d --build backend
```

Le backend démarre sur **http://localhost:8081**

> Premier démarrage : ~2 minutes (compilation Maven dans Docker).  
> Compte admin créé automatiquement : `admin@appsante.ma` / `Admin1234`

### 4. Démarrer le frontend

```bash
cd FRONTEND
npm install
npm run dev
```

Frontend accessible sur **http://localhost:5173**

---

## Configuration MySQL

| Paramètre | Valeur |
|-----------|--------|
| Hôte | `localhost` |
| Port | `3307` |
| Base | `app_dentaire` |
| Utilisateur | `root` |
| Mot de passe | *(vide)* |

---

## Comptes de test

| Rôle | Email | Mot de passe |
|------|-------|-------------|
| Admin | `admin@appsante.ma` | `Admin1234` |
| Patient | S'inscrire via `/#/login` (onglet Inscription) | — |
| Médecin | Créé par l'admin dans le panneau admin | — |
| Réceptionniste | Créé par l'admin dans le panneau admin | — |

---

## Structure du projet

```
HealthApp-main/
├── Architecture/      Schéma SQL + diagrammes (référence, ne pas modifier)
│   └── database.sql   Schéma MySQL complet (21 tables)
├── Backend/           Spring Boot 4.0.6 — Java 17
│   └── src/main/java/com/appsante/
│       ├── controller/    Endpoints REST
│       ├── service/       Logique métier
│       ├── repository/    Accès JPA
│       ├── entity/        Entités Hibernate
│       ├── dto/           Objets de transfert (request / response)
│       ├── security/      JWT + Spring Security 6
│       └── config/        Config + DataInitializer
└── FRONTEND/          Vue 3 + Vite + Tailwind CSS
    └── src/views/
        ├── LoginView.vue          Connexion + inscription patient
        ├── DashboardView.vue      Espace patient (RDV, dossier)
        ├── MedecinView.vue        Espace médecin
        ├── ReceptionnisteView.vue Espace réceptionniste
        └── AdminView.vue          Panneau admin
```

---

## Endpoints principaux

| Méthode | URL | Auth | Description |
|---------|-----|------|-------------|
| `POST` | `/api/auth/register` | Public | Inscription patient |
| `POST` | `/api/auth/login` | Public | Connexion → JWT |
| `GET` | `/api/public/villes` | Public | Liste des villes |
| `GET` | `/api/public/domaines` | Public | Spécialités médicales |
| `GET` | `/api/public/medecins?idEtablissement=` | Public | Médecins d'un cabinet |
| `GET` | `/api/public/disponibilites?idMedecin=` | Public | Créneaux disponibles |
| `POST` | `/api/patient/rdv` | PATIENT | Prendre un RDV |
| `GET` | `/api/patient/rdv` | PATIENT | Mes rendez-vous |
| `GET` | `/api/receptionniste/rdv` | RECEPTIONNISTE | RDV du cabinet |
| `PATCH` | `/api/receptionniste/rdv/{id}/statut` | RECEPTIONNISTE | Changer statut RDV |
| `POST` | `/api/admin/medecins` | ADMIN | Créer un médecin |
| `POST` | `/api/admin/receptionnistes` | ADMIN | Créer un réceptionniste |

---

## Commandes utiles

```bash
# Voir les logs du backend en direct
docker logs -f healthapp-main-backend-1

# Reconstruire le backend après modification Java
docker-compose up -d --build backend

# Arrêter tout
docker-compose down

# Accéder à MySQL
docker exec -it healthapp-main-db-1 mysql -u root app_dentaire
```

---

## Stack technique

| Couche | Technologie |
|--------|------------|
| Backend | Spring Boot 4.0.6, Java 17, Spring Security 6, JWT |
| Base de données | MySQL 8.0 |
| ORM | Hibernate / Spring Data JPA |
| Frontend | Vue 3, Vite, Tailwind CSS, Vue Router 4 |
| Conteneurisation | Docker, Docker Compose |
