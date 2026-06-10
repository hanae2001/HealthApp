# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Status

This repository is in the **design/planning phase**. Documentation defines the architecture, database schema, and interaction flows for a dental cabinet management application. No application code exists yet.

The planned backend is **Spring Boot** (Java) with **MySQL**, secured by **JWT**, built on **Spring Data JPA**, **Spring Security**, **Spring Mail**, and **Lombok**.

The schema was generalized from dental-only to **multi-specialty**: `cabinet` → `etablissement`, `dentiste` → `medecin`, with new lookup tables `region`, `domaine_medical`, and `type_etablissement`.

## Reference Documents

- [database.sql](database.sql) — Authoritative MySQL schema: 21 tables, trigger, and seed data
- [architecture-springboot.html](architecture-springboot.html) — Detailed Spring Boot project structure, layer rules, JPA mapping examples, and step-by-step build order
- [Archtecture v2.html](Archtecture%20v2.html) — Full sequence diagram across 6 actors and 6 workflow phases (French)
- [Database.html](Database.html) — Visual schema reference (Arabic/French)

## Database Setup

```bash
mysql -u root -p < database.sql
```

Drops and recreates `app_dentaire`, creates all 18 tables, installs the trigger, and loads seed data (Moroccan cities/sectors, 3 cabinets, 1 dentist, 1 receptionist, 1 patient).

## Spring Boot Project Initialization

Generate the project at [start.spring.io](https://start.spring.io) with these dependencies:
**Spring Web, Spring Data JPA, Spring Security, MySQL Driver, Spring Mail, Lombok, Validation**

Add manually in `pom.xml`:
```xml
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt</artifactId>
</dependency>
```

Key `application.properties` settings:
- `spring.jpa.hibernate.ddl-auto=validate` — Hibernate verifies the schema without modifying it
- MySQL URL, credentials, and mail server config

## Architecture — 6 Layers

```
Client (HTTP + JWT)
      ↕ REST/JSON
Controller  — routes, @Valid on DTOs, returns ResponseEntity<DTO>. No business logic.
      ↕
Service     — all business rules, @Transactional, Entity↔DTO conversion
      ↕
Repository  — JpaRepository<Entity, Id> interfaces, @Query for custom JPQL
      ↕
Entity      — @Entity classes mapping the 18 MySQL tables, FetchType.LAZY everywhere
      ↕
MySQL (app_dentaire)
```

**Rule:** Controller never calls Repository directly. Repository contains no business logic.

## Planned Package Structure

```
src/main/java/com/dentaire/
├── config/          SecurityConfig.java, MailConfig.java
├── entity/          18 files, one per table
├── repository/      JpaRepository interfaces
├── dto/
│   ├── request/     RegisterRequest, LoginRequest, RdvRequest, ConsultationRequest,
│   │                SoinRequest, NoteCliniqueRequest, OrdonnanceRequest,
│   │                FactureRequest, PaiementRequest
│   └── response/    TokenResponse, PatientResponse, RdvResponse,
│                    DossierResponse, FactureResponse, ErrorResponse
├── service/         AuthService, AdminService, RendezVousService,
│                    ConsultationService, FacturationService, MailService
├── controller/      AuthController, AdminController, RendezVousController,
│                    ConsultationController, FacturationController,
│                    GlobalExceptionHandler
├── security/        JwtUtil.java, JwtFilter.java, UserDetailsServiceImpl.java
└── scheduler/       RappelRdvJob.java  (@Scheduled appointment reminder emails)
AppDentaireApplication.java   (main + @EnableScheduling)
```

## 5 Business Modules

| Module | Key service | Key controller | Notes |
|---|---|---|---|
| **Auth** | AuthService | AuthController | JWT login/register, bcrypt passwords |
| **Admin** | AdminService | AdminController | Creates medecin & receptionniste accounts only |
| **Rendez-vous** | RendezVousService | RendezVousController | Slot conflict prevented by UNIQUE KEY on (id_medecin, date_heure) |
| **Consultation** | ConsultationService | ConsultationController | Writes to dossier_medical tree |
| **Facturation** | FacturationService | FacturationController | montant_total set manually; montant_paye accumulated via paiement rows |

## System Architecture

**Actors:** Patient, Réceptionniste, Médecin, Admin, Système MySQL, Laboratoire

**Appointment statuses:** `planifié` → `confirmé` → `terminé` | `annulé`

**6-phase workflow:**
1. **Inscription** — creates `utilisateur` + `patient` + auto-triggers empty `dossier_medical`
2. **Gestion dentistes** — Admin-only: creates `dentiste` accounts
3. **Gestion réceptionnistes** — Admin-only: creates `receptionniste` accounts
4. **Rendez-vous** — Patient or receptionist books; slot conflict blocked by DB UNIQUE constraint
5. **Consultation** — Dentist fills `dossier_medical` header, then records `note_clinique`, `soin`, `ordonnance`/`medicament_ordonnance`, `radiographie`, `analyse`; ends by setting RDV status to `terminé`
6. **Facturation** — Receptionist creates `facture`; patient pays via `paiement` (partial/installment supported)

## Database Schema (MySQL, 18 tables)

**Geography:** `region` (12 régions du Maroc) → `ville` → `secteur`

**Establishment:** `type_etablissement` + `secteur` → `etablissement` (cabinet, clinique, hôpital, etc.)

**Specialty:** `domaine_medical` (Dentaire, ORL, Cardiologie, etc.)

**Auth:** `utilisateur` (email, bcrypt password, role ENUM: patient/medecin/receptionniste/admin, actif flag)

**Role profiles** (each UNIQUE FK to `utilisateur`):
- `patient` — has separate `email` field for reminder delivery
- `medecin` — FK to `etablissement` + `domaine_medical`, stores `numero_ordre` UNIQUE, `tarif_consultation`, `note_moyenne`
- `receptionniste`

**Patient search journey:** domaine → région → ville → secteur → type_etablissement (optional) → etablissement → medecin → rendez-vous

**Scheduling:** `disponibilite` — médecin weekly slots (ENUM day + time range, UNIQUE per medecin+day+start)

**Medical record tree** (all FK to `dossier_medical.id_dossier`):
- `dossier_medical` — 1-per-patient, auto-created by trigger on `patient` INSERT
- `soin` — treatments; `dent_concernee` uses FDI numbering (dental); `cout NOT NULL DEFAULT 0`
- `note_clinique` — diagnostic, plan_traitement, observations
- `ordonnance` + `medicament_ordonnance` (line items)
- `radiographie` — ENUM type (panoramique/rétro-alvéolaire/bitewing/cone_beam), stored as URL/S3 path
- `analyse` — lab requests with `date_demande` and `date_resultat`

**Billing:** `rendez_vous` → `facture` (numbered `FAC-YYYY-NNN`) → `paiement` (espèces/carte/virement/chèque)

**FK chain:** `utilisateur` ← `patient` ← `dossier_medical` ← {soin, note_clinique, ordonnance, radiographie, analyse}

## Key Design Decisions

- `dossier_medical` is created automatically via `AFTER INSERT ON patient` trigger — all medical fields are NULL until the médecin fills them at first consultation.
- `soin.id_rdv`, `ordonnance.id_rdv`, and `facture.id_rdv` are nullable — care and billing can be recorded without a linked appointment.
- `facture.montant_total` is set manually by the receptionist; never auto-computed from `soin.cout`.
- Only Admin can create `medecin` and `receptionniste` accounts.
- All patient communication is email only — no SMS.
- `medecin.note_moyenne` and `nb_avis` are stored columns (not computed) — updated by the application layer.
- Use `FetchType.LAZY` on all JPA relations to avoid N+1 queries across the deep medical record tree.
- MySQL column names use `snake_case`; map to Java `camelCase` with `@Column(name="...")`.
