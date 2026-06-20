# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Layout

```
HealthApp-main/
├── Architecture/     Design documents only — no runnable code
│   ├── CLAUDE.md     Canonical schema reference; read before touching entities or schema
│   ├── database.sql  Authoritative MySQL schema (21 tables, trigger, seed data)
│   └── *.html        Visual schema and sequence diagrams (French/Arabic)
├── Backend/          Spring Boot backend (Java 17, Spring Boot 4.0.6)
└── FRONTEND/         Vue 3 + Vite SPA (Tailwind CSS)
```

**Always read `Architecture/CLAUDE.md` before adding or modifying entities, schema, or layer interactions.**

## Commands

### Backend (run from `Backend/`)

```bash
# Windows
mvnw.cmd spring-boot:run          # start on port 8081
mvnw.cmd clean package            # build JAR
mvnw.cmd test                     # all tests
mvnw.cmd test -Dtest=ClassName    # single test class

# Linux/Mac
./mvnw spring-boot:run
./mvnw test
```

MySQL must be running on **port 3307**, database `app_dentaire`, user `root`, no password.  
`ddl-auto=validate` — Hibernate validates the schema but will not create or modify tables; run `database.sql` first.

### Database setup

```bash
mysql -u root < Architecture/database.sql
```

Drops and recreates `app_dentaire`, creates 21 tables, installs the `AFTER INSERT ON patient` trigger (auto-creates `dossier_medical`), and loads seed data.

### Frontend (run from `FRONTEND/`)

```bash
npm install
npm run dev      # dev server → http://localhost:5173
npm run build    # production build → dist/
```

## Architecture — 5 Layers

```
Client (HTTP + JWT Bearer token)
      ↕ REST/JSON
Controller  — @RestController, @Valid on DTOs, returns ResponseEntity<DTO>
      ↕
Service     — @Service, @Transactional, Entity↔DTO mapping, all business logic
      ↕
Repository  — JpaRepository<Entity, Id> interfaces; @Query for JPQL
      ↕
Entity      — @Entity + Lombok (@Data/@NoArgsConstructor); FetchType.LAZY everywhere
      ↕
MySQL (app_dentaire)
```

**Hard rules:** Controller never calls Repository directly. Service owns all transactions and Entity↔DTO mapping.

## Current Implementation State

Base package: `com.appsante`

| Layer | Implemented |
|-------|-------------|
| Entities | `Utilisateur`, `Patient`, `Medecin`, `Receptionniste`, `Etablissement`, `DomaineMedical`, `Ville`, `Secteur` |
| Repositories | All of the above |
| Services | `AuthService`, `AdminService` |
| Controllers | `AuthController`, `AdminController`, `PublicController`, `GlobalExceptionHandler` |
| Security | `SecurityConfig`, `JwtUtil`, `JwtFilter`, `UserDetailsServiceImpl` |
| DTOs | Register/Login/Auth, CreateMedecin/Receptionniste requests; Medecin/Receptionniste/Etablissement/Domaine/Ville/Secteur responses |
| Config | `DataInitializer` — auto-creates `admin@appsante.ma` / `Admin1234` on first boot |

**Live endpoints:**

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/api/auth/register` | Public | Creates `Utilisateur` + `Patient`; DB trigger auto-creates `dossier_medical` |
| `POST` | `/api/auth/login` | Public | Returns JWT + user info |
| `GET` | `/api/public/domaines` | Public | List medical specialties |
| `GET` | `/api/public/villes` | Public | List cities (sorted A–Z) |
| `GET` | `/api/public/secteurs?idVille=` | Public | Sectors for a city |
| `GET` | `/api/public/etablissements?idSecteur=` | Public | Establishments (optional filter) |
| `POST` | `/api/admin/medecins` | ADMIN | Create medecin account |
| `POST` | `/api/admin/receptionnistes` | ADMIN | Create receptionniste account |
| `GET` | `/api/admin/medecins` | ADMIN | List all medecins |
| `GET` | `/api/admin/receptionnistes` | ADMIN | List all receptionnistes |
| `GET` | `/api/admin/villes` | ADMIN | List cities |
| `GET` | `/api/admin/secteurs?idVille=` | ADMIN | Sectors for a city |
| `GET` | `/api/admin/etablissements?idSecteur=` | ADMIN | Establishments |
| `GET` | `/api/admin/domaines` | ADMIN | List specialties |

**Not yet implemented:**
- 13 remaining entities: `rendez_vous`, `disponibilite`, `dossier_medical`, `soin`, `note_clinique`, `ordonnance`, `medicament_ordonnance`, `radiographie`, `analyse`, `facture`, `paiement`, `region`, `type_etablissement`
- Services: `RendezVousService`, `ConsultationService`, `FacturationService`, `MailService`
- Controllers: `RendezVousController`, `ConsultationController`, `FacturationController`
- `config/MailConfig`, `scheduler/RappelRdvJob`

## Security

`SecurityConfig` uses `@EnableMethodSecurity` with `@PreAuthorize("hasRole('ADMIN')")` on `AdminController`. Role is stored as a JWT claim (`role`) and loaded by `UserDetailsServiceImpl` which prefixes it with `ROLE_` for Spring Security.

Public routes: `/api/auth/**` and GET `/api/public/**`. Everything else requires a valid Bearer token.

JWT secret and expiration live in `application.properties` (`jwt.secret`, `jwt.expiration=86400000`).

## Frontend

Vue 3 + Vite SPA using hash history routing. Auth state lives in `localStorage` (`token`, `user`).

| Route | Component | Notes |
|-------|-----------|-------|
| `/#/` | → `/login` | Redirect |
| `/#/register` | → `/login` | Redirect (register is a tab inside LoginView) |
| `/#/login` | `LoginView.vue` | Combined login + patient register; toggle between Patient / Professionnel login types |
| `/#/dashboard` | `DashboardView.vue` | Patient dashboard; requires auth + role=patient |
| `/#/admin` | `AdminView.vue` | Admin panel; requires auth + role=admin |

Router guard redirects authenticated users away from `/login` to their role-appropriate route (`/admin` or `/dashboard`), and redirects wrong-role access similarly.

`AdminView.vue` is a single-file component with an embedded sidebar, multi-step wizard for creating medecin/receptionniste accounts, and data tables. The wizard calls `/api/public/**` (no auth) for dropdowns and `/api/admin/**` (Bearer token) for mutations.

Shared CSS (animations, panel-left, form inputs, alerts) lives in `src/assets/main.css`. The `brand` Tailwind color is blue `#2563eb` (`brand-600`).

## Database Schema Quick Reference (21 tables)

**Geography:** `region` (12 Moroccan regions) → `ville` → `secteur`

**Establishment:** `type_etablissement` + `secteur` → `etablissement`

**Specialty:** `domaine_medical` (Dentaire, ORL, Cardiologie, Pédiatrie, etc.)

**Auth:** `utilisateur` — role ENUM: `patient` | `medecin` | `receptionniste` | `admin`

**Profiles** (each OneToOne FK → `utilisateur`):
- `patient` — has its own `email` field (for reminders, separate from `utilisateur.email`)
- `medecin` — FK to `etablissement` + `domaine_medical`; `numero_ordre` UNIQUE, `tarif_consultation`, `note_moyenne`/`nb_avis`
- `receptionniste`

**Scheduling:** `disponibilite` — weekly slots; UNIQUE on `(id_medecin, jour_semaine, heure_debut)`

**Medical record tree** (all FK to `dossier_medical.id_dossier`):
- `dossier_medical` — 1-per-patient; auto-created by DB trigger on `patient` INSERT
- `soin`, `note_clinique`, `ordonnance` + `medicament_ordonnance`, `radiographie`, `analyse`

**Billing:** `rendez_vous` → `facture` (format: `FAC-YYYY-NNN`) → `paiement`

**Slot conflict prevention:** UNIQUE KEY `(id_medecin, date_heure)` on `rendez_vous` — no application-level locking needed.

## Key Non-Obvious Design Decisions

- `facture.montant_total` is **never computed** from `soin.cout`; the receptionist enters it manually.
- `soin.id_rdv`, `ordonnance.id_rdv`, `facture.id_rdv` are nullable — care and billing can exist without an appointment.
- `medecin.note_moyenne` and `nb_avis` are plain columns updated by the Service after each review, not DB-computed.
- `utilisateur.date_creation` and `patient.date_inscription` use `insertable=false, updatable=false` — values are set by MySQL defaults, not Hibernate.
- MySQL `snake_case` columns → Java `camelCase` via `@Column(name="...")`.
- Only the `admin` role can create `medecin` and `receptionniste` accounts.
- Patient communication is email only (`RappelRdvJob` / Spring Mail).
- All entity relationships use `FetchType.LAZY` to prevent N+1 queries.
- `PublicController` calls repositories directly (no service layer) — this is intentional for simple read-only geography/catalog lookups with no business logic.
