# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Layout

```
AppDentaire/
├── Architecture/       Design-only documents (no runnable code)
│   ├── CLAUDE.md       Detailed architecture reference — read for schema/layer design
│   ├── database.sql    Authoritative MySQL schema (21 tables, trigger, seed data)
│   ├── Database.html   Visual schema reference (Arabic/French)
│   ├── architecture-springboot.html  Layer rules, JPA examples, build order
│   └── Archtecture v2.html           Full sequence diagram (6 actors, 6 phases)
├── APPSANTE/           Spring Boot backend (active implementation)
└── FRONTEND/           Static HTML frontend (Tailwind CSS, no framework)
```

**Always read `Architecture/CLAUDE.md` before adding or modifying entities, schema, or layer interactions** — it contains the canonical schema reference and architectural constraints.

## Maven Commands (run from `APPSANTE/`)

```bash
# Windows
mvnw.cmd spring-boot:run
mvnw.cmd clean package
mvnw.cmd test
mvnw.cmd test -Dtest=ClassName

# Linux/Mac
./mvnw spring-boot:run
./mvnw test
```

App runs on **port 8081**. MySQL expected on **port 3307**, database `app_dentaire`, user `root` (no password). Hibernate is set to `ddl-auto=validate` — schema must exist before startup; it will not auto-create tables.

## Database Setup

```bash
mysql -u root -p < Architecture/database.sql
```

Drops and recreates `app_dentaire`, creates 21 tables, installs the `AFTER INSERT ON patient` trigger (auto-creates `dossier_medical`), and loads seed data.

## Architecture — 5 Layers

```
Client (HTTP + JWT Bearer token)
      ↕ REST/JSON
Controller  — @RestController, @Valid on DTOs, returns ResponseEntity<DTO>
      ↕
Service     — @Service, @Transactional, Entity↔DTO mapping, all business logic
      ↕
Repository  — JpaRepository<Entity, Id> interfaces only; @Query for JPQL
      ↕
Entity      — @Entity + Lombok (@Data/@NoArgsConstructor); mirrors 21 MySQL tables; FetchType.LAZY everywhere
      ↕
MySQL (app_dentaire)
```

**Hard rules:** Controller never calls Repository directly. Service owns all transactions and Entity↔DTO mapping.

## Current Implementation State

Base package is `com.appsante`. Spring Boot 4.0.6 / Java 17.

**What exists:**

| Layer | Implemented | Planned total |
|-------|-------------|---------------|
| Entities | `Utilisateur`, `Patient` | 21 |
| Repositories | `UtilisateurRepository`, `PatientRepository` | 21 |
| Services | `AuthService` | 6 |
| Controllers | `AuthController`, `GlobalExceptionHandler` | 5 + handler |
| DTOs | `RegisterRequest`, `RegisterResponse`, `ErrorResponse` | 9+ |
| Security | `SecurityConfig` (BCrypt, CORS, stateless) | + `JwtUtil`, `JwtFilter`, `UserDetailsServiceImpl` |

**Implemented endpoint:**
- `POST /api/auth/register` — creates `Utilisateur` + `Patient` in one transaction; DB trigger auto-creates `dossier_medical`

**Next phases (not yet implemented):**
- `security/` package: `JwtUtil`, `JwtFilter`, `UserDetailsServiceImpl`
- `POST /api/auth/login` returning a JWT
- 19 remaining entities and repositories
- Services: `AdminService`, `RendezVousService`, `ConsultationService`, `FacturationService`, `MailService`
- Controllers: `AdminController`, `RendezVousController`, `ConsultationController`, `FacturationController`
- `config/MailConfig`, `scheduler/RappelRdvJob` (@Scheduled appointment reminder emails)

**Missing `pom.xml` dependencies:**
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <version>0.12.6</version>
</dependency>
<!-- also jjwt-impl and jjwt-jackson at runtime scope -->
```

## Frontend

Vue 3 + Vite SPA in `FRONTEND/` with Tailwind CSS (npm, not CDN).

```bash
# Run from FRONTEND/
npm install
npm run dev      # dev server → http://localhost:5173
npm run build    # production build → dist/
```

Routing uses Vue Router with hash history (`/#/`, `/#/register`, `/#/login`).

| View | File | Notes |
|------|------|-------|
| Landing page | `src/views/HomeView.vue` | Static; doctors/features/testimonials as data arrays |
| Inscription | `src/views/RegisterView.vue` | Calls `POST http://localhost:8081/api/auth/register`; redirects to `/login` on success |
| Connexion | `src/views/LoginView.vue` | UI ready; shows JWT warning — backend `/api/auth/login` not yet implemented |

Shared CSS (animations, panel-left, form inputs, alerts) lives in `src/assets/main.css`. The `brand` Tailwind color extends blue (`#2563eb` = `brand-600`). The old static `.html` files remain in `FRONTEND/` as reference but are no longer the entry points.

## Database Schema Quick Reference (21 tables)

**Geography chain:** `region` (12 Moroccan regions) → `ville` → `secteur`

**Establishment:** `type_etablissement` + `secteur` → `etablissement`

**Specialty:** `domaine_medical` (Dentaire, ORL, Cardiologie, Pédiatrie, etc.)

**Auth:** `utilisateur` — role ENUM: `patient` | `medecin` | `receptionniste` | `admin`

**Profiles** (each OneToOne FK → `utilisateur`):
- `patient` — has its own `email` field (for reminders, separate from `utilisateur.email`)
- `medecin` — FK to `etablissement` + `domaine_medical`; stores `numero_ordre` (UNIQUE), `tarif_consultation`, `note_moyenne`/`nb_avis`
- `receptionniste`

**Scheduling:** `disponibilite` — weekly time slots; UNIQUE on `(id_medecin, jour_semaine, heure_debut)`

**Medical record tree** (all FK to `dossier_medical.id_dossier`):
- `dossier_medical` — 1-per-patient; auto-created by DB trigger on patient INSERT
- `soin`, `note_clinique`, `ordonnance` + `medicament_ordonnance`, `radiographie`, `analyse`

**Billing:** `rendez_vous` → `facture` (number format: FAC-YYYY-NNN) → `paiement`

**Slot conflict prevention:** UNIQUE KEY `(id_medecin, date_heure)` on `rendez_vous` — no application-level locking needed.

## Key Non-Obvious Design Decisions

- `facture.montant_total` is **never computed** from `soin.cout`; the receptionist enters it manually.
- `soin.id_rdv`, `ordonnance.id_rdv`, `facture.id_rdv` are nullable — care and billing can exist without an appointment.
- `medecin.note_moyenne` and `nb_avis` are plain columns updated by the Service after each review, not DB-computed.
- MySQL `snake_case` columns → Java `camelCase` via `@Column(name="...")`.
- Only the `admin` role can create `medecin` and `receptionniste` accounts.
- Patient communication is email only (`RappelRdvJob` / Spring Mail).
- All entity relationships use `FetchType.LAZY` to prevent N+1 queries.
