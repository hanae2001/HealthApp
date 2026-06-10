-- ============================================================
--  AppDentaire — Schéma complet MySQL
--  21 tables | Généralisation multi-spécialités
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;
DROP DATABASE IF EXISTS app_dentaire;
CREATE DATABASE app_dentaire CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE app_dentaire;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 1. REGION (12 régions administratives du Maroc)
-- ============================================================
CREATE TABLE region (
  id_region  INT          PRIMARY KEY AUTO_INCREMENT,
  nom_region VARCHAR(100) NOT NULL,
  UNIQUE KEY unique_region (nom_region)
);

-- ============================================================
-- 2. VILLE
-- ============================================================
CREATE TABLE ville (
  id_ville  INT          PRIMARY KEY AUTO_INCREMENT,
  nom_ville VARCHAR(100) NOT NULL,
  id_region INT          NOT NULL,
  UNIQUE KEY unique_ville (nom_ville),
  FOREIGN KEY (id_region) REFERENCES region(id_region)
);

-- ============================================================
-- 3. SECTEUR (quartier / zone)
-- ============================================================
CREATE TABLE secteur (
  id_secteur  INT          PRIMARY KEY AUTO_INCREMENT,
  nom_secteur VARCHAR(100) NOT NULL,
  id_ville    INT          NOT NULL,
  UNIQUE KEY unique_secteur_ville (nom_secteur, id_ville),
  FOREIGN KEY (id_ville) REFERENCES ville(id_ville)
);

-- ============================================================
-- 4. TYPE_ETABLISSEMENT (cabinet, clinique, hôpital…)
-- ============================================================
CREATE TABLE type_etablissement (
  id_type  INT         PRIMARY KEY AUTO_INCREMENT,
  nom_type VARCHAR(50) NOT NULL,
  UNIQUE KEY unique_type (nom_type)
);

-- ============================================================
-- 5. ETABLISSEMENT  (remplace cabinet — tous types confondus)
-- ============================================================
CREATE TABLE etablissement (
  id_etablissement  INT           PRIMARY KEY AUTO_INCREMENT,
  nom_etablissement VARCHAR(150)  NOT NULL,
  id_type           INT           NOT NULL,
  id_secteur        INT           NOT NULL,
  adresse           TEXT,
  telephone         VARCHAR(20),
  email             VARCHAR(100),
  site_web          VARCHAR(255),
  latitude          DECIMAL(10,8),
  longitude         DECIMAL(11,8),
  actif             BOOLEAN       DEFAULT TRUE,
  FOREIGN KEY (id_type)    REFERENCES type_etablissement(id_type),
  FOREIGN KEY (id_secteur) REFERENCES secteur(id_secteur)
);

-- ============================================================
-- 6. DOMAINE_MEDICAL (spécialités médicales)
-- ============================================================
CREATE TABLE domaine_medical (
  id_domaine  INT          PRIMARY KEY AUTO_INCREMENT,
  nom_domaine VARCHAR(100) NOT NULL,
  icone       VARCHAR(50),
  description TEXT,
  actif       BOOLEAN      DEFAULT TRUE,
  UNIQUE KEY unique_domaine (nom_domaine)
);

-- ============================================================
-- 7. UTILISATEUR  (authentification + rôles)
-- ============================================================
CREATE TABLE utilisateur (
  id_utilisateur INT          PRIMARY KEY AUTO_INCREMENT,
  email          VARCHAR(100) UNIQUE NOT NULL,
  mot_de_passe   VARCHAR(255) NOT NULL,
  role           ENUM('patient','medecin','receptionniste','admin') NOT NULL,
  actif          BOOLEAN      DEFAULT TRUE,
  date_creation  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 8. PATIENT
-- ============================================================
CREATE TABLE patient (
  id_patient       INT          PRIMARY KEY AUTO_INCREMENT,
  id_utilisateur   INT          UNIQUE,
  nom              VARCHAR(50)  NOT NULL,
  prenom           VARCHAR(50)  NOT NULL,
  email            VARCHAR(100),                -- pour les emails de rappel RDV
  date_naissance   DATE,
  telephone        VARCHAR(20),
  adresse          TEXT,
  groupe_sanguin   VARCHAR(5),
  allergies        TEXT,
  date_inscription DATE         DEFAULT (CURRENT_DATE),
  FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

-- ============================================================
-- 9. MEDECIN  (remplace dentiste — toutes spécialités)
-- ============================================================
CREATE TABLE medecin (
  id_medecin         INT           PRIMARY KEY AUTO_INCREMENT,
  id_utilisateur     INT           UNIQUE,
  id_etablissement   INT           NULL,
  id_domaine         INT           NOT NULL,
  nom                VARCHAR(50)   NOT NULL,
  prenom             VARCHAR(50)   NOT NULL,
  telephone          VARCHAR(20),
  email              VARCHAR(100),
  numero_ordre       VARCHAR(50)   UNIQUE,
  experience_ans     INT,
  tarif_consultation DECIMAL(10,2),
  note_moyenne       DECIMAL(2,1)  DEFAULT 0,
  nb_avis            INT           DEFAULT 0,
  actif              BOOLEAN       DEFAULT TRUE,
  FOREIGN KEY (id_utilisateur)   REFERENCES utilisateur(id_utilisateur),
  FOREIGN KEY (id_etablissement) REFERENCES etablissement(id_etablissement),
  FOREIGN KEY (id_domaine)       REFERENCES domaine_medical(id_domaine)
);

-- ============================================================
-- 10. RECEPTIONNISTE
-- ============================================================
CREATE TABLE receptionniste (
  id_receptionniste INT         PRIMARY KEY AUTO_INCREMENT,
  id_utilisateur    INT         UNIQUE,
  nom               VARCHAR(50) NOT NULL,
  prenom            VARCHAR(50) NOT NULL,
  telephone         VARCHAR(20),
  FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

-- ============================================================
-- 11. DISPONIBILITE  (horaires de travail du médecin)
-- ============================================================
CREATE TABLE disponibilite (
  id_dispo     INT  PRIMARY KEY AUTO_INCREMENT,
  id_medecin   INT  NOT NULL,
  jour_semaine ENUM('lundi','mardi','mercredi','jeudi','vendredi','samedi') NOT NULL,
  heure_debut  TIME NOT NULL,
  heure_fin    TIME NOT NULL,
  UNIQUE KEY unique_dispo (id_medecin, jour_semaine, heure_debut),
  FOREIGN KEY (id_medecin) REFERENCES medecin(id_medecin)
);

-- ============================================================
-- 12. DOSSIER MÉDICAL  (créé automatiquement à l'inscription)
-- ============================================================
CREATE TABLE dossier_medical (
  id_dossier            INT  PRIMARY KEY AUTO_INCREMENT,
  id_patient            INT  NOT NULL,
  date_creation         DATE DEFAULT (CURRENT_DATE),
  antecedents_medicaux  TEXT,
  antecedents_dentaires TEXT,
  notes_generales       TEXT,
  UNIQUE KEY unique_dossier_patient (id_patient),
  FOREIGN KEY (id_patient) REFERENCES patient(id_patient)
);

-- Trigger : crée un dossier vide dès qu'un patient est inséré
DELIMITER $$
CREATE TRIGGER after_patient_insert
AFTER INSERT ON patient
FOR EACH ROW
BEGIN
  INSERT INTO dossier_medical (id_patient, date_creation)
  VALUES (NEW.id_patient, CURRENT_DATE);
END$$
DELIMITER ;

-- ============================================================
-- 13. RENDEZ-VOUS
-- ============================================================
CREATE TABLE rendez_vous (
  id_rdv      INT          PRIMARY KEY AUTO_INCREMENT,
  id_patient  INT          NOT NULL,
  id_medecin  INT          NOT NULL,
  date_heure  DATETIME     NOT NULL,
  duree       INT          DEFAULT 30,
  motif       VARCHAR(255),
  statut      ENUM('planifié','confirmé','annulé','terminé') DEFAULT 'planifié',
  notes       TEXT,
  created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
  updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY unique_creneau (id_medecin, date_heure),        -- pas de double réservation
  FOREIGN KEY (id_patient) REFERENCES patient(id_patient),
  FOREIGN KEY (id_medecin) REFERENCES medecin(id_medecin)
);

-- ============================================================
-- 14. SOIN
-- ============================================================
CREATE TABLE soin (
  id_soin        INT           PRIMARY KEY AUTO_INCREMENT,
  id_dossier     INT           NOT NULL,
  id_medecin     INT           NOT NULL,
  id_rdv         INT,
  type_soin      VARCHAR(100)  NOT NULL,
  dent_concernee VARCHAR(20),                               -- numérotation FDI (dentaire)
  date_soin      DATE          NOT NULL,
  cout           DECIMAL(10,2) NOT NULL DEFAULT 0.00,       -- NOT NULL + DEFAULT 0 pour SUM()
  statut         ENUM('en cours','terminé') DEFAULT 'terminé',
  FOREIGN KEY (id_dossier) REFERENCES dossier_medical(id_dossier),
  FOREIGN KEY (id_medecin) REFERENCES medecin(id_medecin),
  FOREIGN KEY (id_rdv)     REFERENCES rendez_vous(id_rdv)
);

-- ============================================================
-- 15. NOTE CLINIQUE
-- ============================================================
CREATE TABLE note_clinique (
  id_note         INT      PRIMARY KEY AUTO_INCREMENT,
  id_dossier      INT      NOT NULL,
  id_medecin      INT      NOT NULL,
  id_rdv          INT,
  date_note       DATETIME DEFAULT CURRENT_TIMESTAMP,
  diagnostic      TEXT,
  plan_traitement TEXT,
  observations    TEXT,
  FOREIGN KEY (id_dossier) REFERENCES dossier_medical(id_dossier),
  FOREIGN KEY (id_medecin) REFERENCES medecin(id_medecin),
  FOREIGN KEY (id_rdv)     REFERENCES rendez_vous(id_rdv)
);

-- ============================================================
-- 16. ORDONNANCE
-- ============================================================
CREATE TABLE ordonnance (
  id_ordonnance  INT  PRIMARY KEY AUTO_INCREMENT,
  id_dossier     INT  NOT NULL,
  id_medecin     INT  NOT NULL,
  id_rdv         INT,
  date_emission  DATE NOT NULL,
  validite_jours INT  DEFAULT 30,
  notes          TEXT,
  FOREIGN KEY (id_dossier) REFERENCES dossier_medical(id_dossier),
  FOREIGN KEY (id_medecin) REFERENCES medecin(id_medecin),
  FOREIGN KEY (id_rdv)     REFERENCES rendez_vous(id_rdv)
);

-- ============================================================
-- 17. MÉDICAMENT ORDONNANCE  (lignes de l'ordonnance)
-- ============================================================
CREATE TABLE medicament_ordonnance (
  id             INT          PRIMARY KEY AUTO_INCREMENT,
  id_ordonnance  INT          NOT NULL,
  nom_medicament VARCHAR(100) NOT NULL,
  dosage         VARCHAR(100),
  frequence      VARCHAR(100),
  duree          VARCHAR(100),
  instructions   TEXT,
  FOREIGN KEY (id_ordonnance) REFERENCES ordonnance(id_ordonnance)
);

-- ============================================================
-- 18. RADIOGRAPHIE
-- ============================================================
CREATE TABLE radiographie (
  id_radio    INT          PRIMARY KEY AUTO_INCREMENT,
  id_dossier  INT          NOT NULL,
  id_medecin  INT          NOT NULL,
  type_radio  ENUM('panoramique','rétro-alvéolaire','bitewing','cone_beam') NOT NULL,
  fichier_url VARCHAR(500) NOT NULL,
  format      VARCHAR(10)  DEFAULT 'JPEG',
  date_prise  DATE         NOT NULL,
  description TEXT,
  FOREIGN KEY (id_dossier) REFERENCES dossier_medical(id_dossier),
  FOREIGN KEY (id_medecin) REFERENCES medecin(id_medecin)
);

-- ============================================================
-- 19. ANALYSE
-- ============================================================
CREATE TABLE analyse (
  id_analyse     INT          PRIMARY KEY AUTO_INCREMENT,
  id_dossier     INT          NOT NULL,
  id_medecin     INT          NOT NULL,
  type_analyse   VARCHAR(100) NOT NULL,
  laboratoire    VARCHAR(100),
  date_demande   DATE         NOT NULL,
  date_resultat  DATE,
  resultat       TEXT,
  interpretation TEXT,
  fichier_url    VARCHAR(500),
  FOREIGN KEY (id_dossier) REFERENCES dossier_medical(id_dossier),
  FOREIGN KEY (id_medecin) REFERENCES medecin(id_medecin)
);

-- ============================================================
-- 20. FACTURE
-- ============================================================
CREATE TABLE facture (
  id_facture     INT           PRIMARY KEY AUTO_INCREMENT,
  id_patient     INT           NOT NULL,
  id_rdv         INT,
  numero_facture VARCHAR(50)   UNIQUE NOT NULL,         -- FAC-YYYY-NNN
  date_emission  DATE          NOT NULL,
  montant_total  DECIMAL(10,2) NOT NULL,
  montant_paye   DECIMAL(10,2) DEFAULT 0.00,
  statut         ENUM('en attente','partiellement payée','payée','annulée') DEFAULT 'en attente',
  created_at     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_patient) REFERENCES patient(id_patient),
  FOREIGN KEY (id_rdv)     REFERENCES rendez_vous(id_rdv)
);

-- ============================================================
-- 21. PAIEMENT
-- ============================================================
CREATE TABLE paiement (
  id_paiement   INT           PRIMARY KEY AUTO_INCREMENT,
  id_facture    INT           NOT NULL,
  date_paiement DATE          NOT NULL,
  montant       DECIMAL(10,2) NOT NULL,
  mode_paiement ENUM('espèces','carte','virement','chèque') NOT NULL,
  reference     VARCHAR(100),
  FOREIGN KEY (id_facture) REFERENCES facture(id_facture)
);

-- ============================================================
--  DONNÉES DE TEST
-- ============================================================

-- Régions du Maroc
INSERT INTO region (nom_region) VALUES
  ('Tanger-Tétouan-Al Hoceïma'),   -- 1
  ('Oriental'),                     -- 2
  ('Fès-Meknès'),                   -- 3
  ('Rabat-Salé-Kénitra'),           -- 4
  ('Béni Mellal-Khénifra'),         -- 5
  ('Casablanca-Settat'),            -- 6
  ('Marrakech-Safi'),               -- 7
  ('Drâa-Tafilalet'),               -- 8
  ('Souss-Massa'),                  -- 9
  ('Guelmim-Oued Noun'),            -- 10
  ('Laâyoune-Sakia El Hamra'),      -- 11
  ('Dakhla-Oued Ed-Dahab');         -- 12

INSERT INTO ville (nom_ville, id_region) VALUES
  ('Rabat',       4),   -- 1
  ('Casablanca',  6),   -- 2
  ('Fès',         3),   -- 3
  ('Marrakech',   7);   -- 4

INSERT INTO secteur (nom_secteur, id_ville) VALUES
  ('Agdal',        1),   -- 1
  ('Souissi',      1),   -- 2
  ('Centre-ville', 1),   -- 3
  ('Maarif',       2),   -- 4
  ('Hay Riad',     1);   -- 5

INSERT INTO type_etablissement (nom_type) VALUES
  ('cabinet'),           -- 1
  ('clinique'),          -- 2
  ('hôpital'),           -- 3
  ('laboratoire'),       -- 4
  ('centre de santé');   -- 5

INSERT INTO etablissement (nom_etablissement, id_type, id_secteur, adresse, telephone) VALUES
  ('Cabinet Dr. Alaoui',   1, 1, '12 Rue Ibn Sina, Agdal',   '0537001122'),   -- 1
  ('Cabinet Sourire Plus', 1, 4, '45 Bd Zerktouni, Maarif',  '0522334455'),   -- 2
  ('Cabinet Hay Riad',     1, 5, '8 Rue Al Majd, Hay Riad',  '0537556677');   -- 3

INSERT INTO domaine_medical (nom_domaine, icone, description) VALUES
  ('Dentaire',      '🦷', 'Soins dentaires, chirurgie buccale, orthodontie'),   -- 1
  ('ORL',           '👂', 'Oto-rhino-laryngologie'),                            -- 2
  ('Cardiologie',   '❤️', 'Cœur et vaisseaux sanguins'),                       -- 3
  ('Pédiatrie',     '👶', 'Médecine des enfants'),                              -- 4
  ('Gynécologie',   '👩', 'Santé de la femme'),                                 -- 5
  ('Ophtalmologie', '👁️', 'Yeux et vision'),                                   -- 6
  ('Dermatologie',  '🧴', 'Peau et cheveux'),                                   -- 7
  ('Généraliste',   '🩺', 'Médecine générale'),                                 -- 8
  ('Neurologie',    '🧠', 'Système nerveux'),                                   -- 9
  ('Psychiatrie',   '🗣️', 'Santé mentale');                                    -- 10

INSERT INTO utilisateur (email, mot_de_passe, role) VALUES
  ('admin@dentaire.ma',        '$2a$10$HASH', 'admin'),          -- 1
  ('dr.alaoui@dentaire.ma',    '$2a$10$HASH', 'medecin'),        -- 2
  ('reception@dentaire.ma',    '$2a$10$HASH', 'receptionniste'), -- 3
  ('patient.test@gmail.com',   '$2a$10$HASH', 'patient');        -- 4

INSERT INTO medecin (id_utilisateur, id_etablissement, id_domaine, nom, prenom, numero_ordre) VALUES
  (2, 1, 1, 'Alaoui', 'Karim', 'ORD-2020-001');   -- id_medecin = 1

INSERT INTO receptionniste (id_utilisateur, nom, prenom, telephone) VALUES
  (3, 'Benali', 'Sara', '0661001122');

INSERT INTO patient (id_utilisateur, nom, prenom, email, telephone, date_naissance) VALUES
  (4, 'El Mansouri', 'Youssef', 'patient.test@gmail.com', '0612345678', '1990-05-15');

-- Horaires du médecin : lundi, mercredi, vendredi 09h-17h
INSERT INTO disponibilite (id_medecin, jour_semaine, heure_debut, heure_fin) VALUES
  (1, 'lundi',    '09:00', '17:00'),
  (1, 'mercredi', '09:00', '17:00'),
  (1, 'vendredi', '09:00', '17:00');
