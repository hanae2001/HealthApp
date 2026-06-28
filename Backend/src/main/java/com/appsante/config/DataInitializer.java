package com.appsante.config;

import com.appsante.entity.DomaineMedical;
import com.appsante.entity.Secteur;
import com.appsante.entity.Utilisateur;
import com.appsante.entity.Ville;
import com.appsante.repository.DomaineMedicalRepository;
import com.appsante.repository.SecteurRepository;
import com.appsante.repository.UtilisateurRepository;
import com.appsante.repository.VilleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final DomaineMedicalRepository domaineMedicalRepository;
    private final VilleRepository villeRepository;
    private final SecteurRepository secteurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedDomaines();
        seedVilles();
        seedSecteurs();
    }

    private void seedAdmin() {
        if (!utilisateurRepository.existsByEmail("admin@appsante.ma")) {
            Utilisateur admin = new Utilisateur();
            admin.setEmail("admin@appsante.ma");
            admin.setMotDePasse(passwordEncoder.encode("Admin1234"));
            admin.setRole(Utilisateur.Role.admin);
            utilisateurRepository.save(admin);
            System.out.println("[DataInitializer] Admin created");
        }
    }

    private void seedDomaines() {
        Set<String> existing = new HashSet<>();
        domaineMedicalRepository.findAll().forEach(d -> existing.add(d.getNomDomaine()));

        String[] noms = {
            "Généraliste", "Médecine d'urgence", "Médecine interne",
            "Médecine du travail", "Gériatrie",
            "Cardiologie", "Chirurgie vasculaire", "Angiologie",
            "Neurologie", "Neurochirurgie", "Psychiatrie", "Psychologie",
            "Pédiatrie", "Néonatologie", "Gynécologie", "Obstétrique",
            "Gastroentérologie", "Hépatologie", "Néphrologie", "Urologie",
            "Orthopédie", "Rhumatologie", "Traumatologie",
            "ORL", "Ophtalmologie", "Dermatologie",
            "Dentaire", "Orthodontie", "Parodontologie",
            "Endocrinologie", "Diabétologie", "Nutrition",
            "Pneumologie", "Allergologie",
            "Oncologie", "Hématologie",
            "Radiologie", "Biologie médicale"
        };

        List<DomaineMedical> toInsert = new ArrayList<>();
        for (String nom : noms) {
            if (!existing.contains(nom)) {
                DomaineMedical d = new DomaineMedical();
                d.setNomDomaine(nom);
                toInsert.add(d);
            }
        }
        if (!toInsert.isEmpty()) {
            domaineMedicalRepository.saveAll(toInsert);
            System.out.println("[DataInitializer] " + toInsert.size() + " domaines inseres");
        } else {
            System.out.println("[DataInitializer] Domaines OK: " + noms.length);
        }
    }

    private void seedVilles() {
        Object[][] cities = {
            {"Casablanca", 6}, {"Mohammedia", 6}, {"Settat", 6},
            {"Berrechid", 6}, {"Benslimane", 6}, {"El Jadida", 6},
            {"Rabat", 4}, {"Salé", 4}, {"Témara", 4},
            {"Kénitra", 4}, {"Khémisset", 4}, {"Sidi Kacem", 4},
            {"Sidi Slimane", 4}, {"Souk El Arbaa", 4},
            {"Fès", 3}, {"Meknès", 3}, {"Taza", 3},
            {"Sefrou", 3}, {"Ifrane", 3}, {"Azrou", 3}, {"Khénifra", 3},
            {"Tanger", 1}, {"Tétouan", 1}, {"Al Hoceïma", 1},
            {"Chefchaouen", 1}, {"Larache", 1}, {"M'Diq", 1},
            {"Fnideq", 1}, {"Ksar el-Kébir", 1},
            {"Oujda", 2}, {"Nador", 2}, {"Berkane", 2},
            {"Taourirt", 2}, {"Figuig", 2}, {"Driouch", 2},
            {"Marrakech", 7}, {"Safi", 7}, {"Essaouira", 7},
            {"Ben Guerir", 7}, {"Youssoufia", 7}, {"Kelaa des Sraghna", 7},
            {"Agadir", 9}, {"Tiznit", 9}, {"Taroudant", 9},
            {"Inezgane", 9}, {"Oulad Teima", 9},
            {"Béni Mellal", 5}, {"Khouribga", 5},
            {"Azilal", 5}, {"Fquih Ben Salah", 5},
            {"Errachidia", 8}, {"Ouarzazate", 8}, {"Zagora", 8},
            {"Midelt", 8}, {"Tinghir", 8},
            {"Guelmim", 10}, {"Tan-Tan", 10}, {"Sidi Ifni", 10}, {"Tata", 10},
            {"Laâyoune", 11}, {"Tarfaya", 11}, {"Boujdour", 11},
            {"Dakhla", 12}
        };

        Set<String> existingVilles = new HashSet<>();
        villeRepository.findAll().forEach(v -> existingVilles.add(v.getNomVille()));

        List<Ville> toInsert = new ArrayList<>();
        for (Object[] entry : cities) {
            String nom = (String) entry[0];
            Integer idRegion = (Integer) entry[1];
            if (!existingVilles.contains(nom)) {
                Ville v = new Ville();
                v.setNomVille(nom);
                v.setIdRegion(idRegion);
                toInsert.add(v);
            }
        }
        if (!toInsert.isEmpty()) {
            villeRepository.saveAll(toInsert);
            System.out.println("[DataInitializer] " + toInsert.size() + " villes inserees");
        } else {
            System.out.println("[DataInitializer] Villes OK: " + cities.length);
        }
    }

    private void seedSecteurs() {
        // Build nomVille -> idVille map
        Map<String, Integer> villeIds = new HashMap<>();
        villeRepository.findAll().forEach(v -> villeIds.put(v.getNomVille(), v.getIdVille()));

        // Sectors per city (nomVille -> list of secteur names)
        Map<String, String[]> secteurMap = new LinkedHashMap<>();
        secteurMap.put("Casablanca",       new String[]{"Maarif","Ain Sebaa","Anfa","Bourgogne","Hay Hassani","Sidi Maarouf","Derb Sultan","Ain Chock","Lissasfa","Roches Noires","Sbata","Belvédère"});
        secteurMap.put("Mohammedia",       new String[]{"Centre-ville","Hay Oulfa","Quartier du Port","Hay Nord"});
        secteurMap.put("Settat",           new String[]{"Centre-ville","Hay Salam","Hay Al Amal"});
        secteurMap.put("Berrechid",        new String[]{"Centre-ville","Hay Jadid"});
        secteurMap.put("Benslimane",       new String[]{"Centre-ville","Hay Riad"});
        secteurMap.put("El Jadida",        new String[]{"Centre-ville","Médina","Hay Jadid","Azemmour"});
        secteurMap.put("Rabat",            new String[]{"Agdal","Hassan","Souissi","Hay Riad","Océan","Akkari","Youssoufia","Takaddoum","Aviation"});
        secteurMap.put("Salé",             new String[]{"Bab Lamrissa","Hay Salam","Médina","Tabriquet","Hay Karima"});
        secteurMap.put("Témara",           new String[]{"Centre-ville","Hay Riad","Menzeh","Bir Anzarane"});
        secteurMap.put("Kénitra",          new String[]{"Centre-ville","Hay Salam","Bir Rami","Quartier du Port"});
        secteurMap.put("Khémisset",        new String[]{"Centre-ville","Hay Al Amal"});
        secteurMap.put("Sidi Kacem",       new String[]{"Centre-ville","Hay Jadid"});
        secteurMap.put("Sidi Slimane",     new String[]{"Centre-ville","Hay Salam"});
        secteurMap.put("Souk El Arbaa",    new String[]{"Centre-ville","Hay Jadid"});
        secteurMap.put("Fès",              new String[]{"Narjiss","Ville Nouvelle","Médina","Les Orangers","Agdal","Dhar Mehraz","Sahrij"});
        secteurMap.put("Meknès",           new String[]{"Hamria","Ville Nouvelle","Médina","Bni M'hamed","Hay Ismaïlia"});
        secteurMap.put("Taza",             new String[]{"Centre-ville","Hay Jadid","Taza Haute"});
        secteurMap.put("Sefrou",           new String[]{"Centre-ville","Médina"});
        secteurMap.put("Ifrane",           new String[]{"Centre-ville","Hay Al Amir"});
        secteurMap.put("Azrou",            new String[]{"Centre-ville","Hay Al Amal"});
        secteurMap.put("Khénifra",         new String[]{"Centre-ville","Hay Salam","Moulay Ismail"});
        secteurMap.put("Tanger",           new String[]{"Malabata","Ville Nouvelle","Médina","Beni Makada","Marchane","Iberia","M'Snana"});
        secteurMap.put("Tétouan",          new String[]{"Ensanche","Médina","Hay Salam","Centre-ville","Beni Makada"});
        secteurMap.put("Al Hoceïma",       new String[]{"Centre-ville","Ajdir","Hay Chabab"});
        secteurMap.put("Chefchaouen",      new String[]{"Médina","Centre-ville","Hay Al Amal"});
        secteurMap.put("Larache",          new String[]{"Centre-ville","Médina","Hay Jadid"});
        secteurMap.put("M'Diq",            new String[]{"Quartier Plage","Centre-ville"});
        secteurMap.put("Fnideq",           new String[]{"Centre-ville","Quartier du Port"});
        secteurMap.put("Ksar el-Kébir",    new String[]{"Centre-ville","Médina","Hay Al Amal"});
        secteurMap.put("Oujda",            new String[]{"Hay Qods","Ville Nouvelle","Médina","Sidi Yahia","Lazaret"});
        secteurMap.put("Nador",            new String[]{"Centre-ville","Hay Jadid","Quartier du Port"});
        secteurMap.put("Berkane",          new String[]{"Centre-ville","Hay Al Amal"});
        secteurMap.put("Taourirt",         new String[]{"Centre-ville","Hay Salam"});
        secteurMap.put("Figuig",           new String[]{"Zenaga","Centre-ville"});
        secteurMap.put("Driouch",          new String[]{"Centre-ville","Hay Jadid"});
        secteurMap.put("Marrakech",        new String[]{"Guéliz","Médina","Hivernage","Palmeraie","M'Hamid","Targa","Massira","Daoudiate"});
        secteurMap.put("Safi",             new String[]{"Centre-ville","Médina","Hay Jadid"});
        secteurMap.put("Essaouira",        new String[]{"Médina","Centre-ville","Hay Salam"});
        secteurMap.put("Ben Guerir",       new String[]{"Centre-ville","Hay Al Amal"});
        secteurMap.put("Youssoufia",       new String[]{"Centre-ville","Hay Jadid"});
        secteurMap.put("Kelaa des Sraghna",new String[]{"Centre-ville","Hay Salam"});
        secteurMap.put("Agadir",           new String[]{"Talborjt","Hay Mohammadi","Charaf","Dakhla","Anza","Hay Salam","Tikiouine"});
        secteurMap.put("Tiznit",           new String[]{"Centre-ville","Médina","Hay Al Amal"});
        secteurMap.put("Taroudant",        new String[]{"Centre-ville","Médina","Hay Jadid"});
        secteurMap.put("Inezgane",         new String[]{"Centre-ville","Hay Salam","Bir Anzarane"});
        secteurMap.put("Oulad Teima",      new String[]{"Centre-ville","Hay Jadid"});
        secteurMap.put("Béni Mellal",      new String[]{"Centre-ville","Hay Ismaïlia","Aïn Asserdoune","Hay Al Amal"});
        secteurMap.put("Khouribga",        new String[]{"Centre-ville","Quartier OCP","Hay Jadid"});
        secteurMap.put("Azilal",           new String[]{"Centre-ville","Hay Al Amal"});
        secteurMap.put("Fquih Ben Salah",  new String[]{"Centre-ville","Hay Salam"});
        secteurMap.put("Errachidia",       new String[]{"Centre-ville","Hay Al Amal","Rissani"});
        secteurMap.put("Ouarzazate",       new String[]{"Centre-ville","Hay Jadid","Tabount"});
        secteurMap.put("Zagora",           new String[]{"Centre-ville","Hay Al Amal"});
        secteurMap.put("Midelt",           new String[]{"Centre-ville","Hay Salam"});
        secteurMap.put("Tinghir",          new String[]{"Centre-ville","Hay Jadid"});
        secteurMap.put("Guelmim",          new String[]{"Centre-ville","Hay Al Amal","Hay Salam"});
        secteurMap.put("Tan-Tan",          new String[]{"Centre-ville","Hay Jadid"});
        secteurMap.put("Sidi Ifni",        new String[]{"Centre-ville","Quartier Plage"});
        secteurMap.put("Tata",             new String[]{"Centre-ville","Hay Al Amal"});
        secteurMap.put("Laâyoune",         new String[]{"Centre-ville","Hay Al Amal","Colomina","Hay Jadid"});
        secteurMap.put("Tarfaya",          new String[]{"Centre-ville"});
        secteurMap.put("Boujdour",         new String[]{"Centre-ville","Hay Al Amal"});
        secteurMap.put("Dakhla",           new String[]{"Centre-ville","Hay Jadid","Quartier Lagune"});

        Set<String> existingSecteurs = new HashSet<>();
        secteurRepository.findAll().forEach(s -> existingSecteurs.add(s.getNomSecteur() + "|" + s.getIdVille()));

        List<Secteur> toInsert = new ArrayList<>();
        int skipped = 0;

        for (Map.Entry<String, String[]> entry : secteurMap.entrySet()) {
            String nomVille = entry.getKey();
            Integer idVille = villeIds.get(nomVille);
            if (idVille == null) {
                System.out.println("[DataInitializer] WARN: ville not found: " + nomVille);
                continue;
            }
            for (String nomSecteur : entry.getValue()) {
                if (!existingSecteurs.contains(nomSecteur + "|" + idVille)) {
                    Secteur s = new Secteur();
                    s.setNomSecteur(nomSecteur);
                    s.setIdVille(idVille);
                    toInsert.add(s);
                } else {
                    skipped++;
                }
            }
        }

        if (!toInsert.isEmpty()) {
            secteurRepository.saveAll(toInsert);
            System.out.println("[DataInitializer] " + toInsert.size() + " secteurs inseres (" + skipped + " existants)");
        } else {
            System.out.println("[DataInitializer] Secteurs OK (" + skipped + " existants)");
        }
    }
}
