//Entity est une classe Java qui représente une table dans la base de données.
//LES 3 RÔLES PRINCIPAUX D'UNE ENTITY 
    //1-Représenter une table   
    //2- Définir les relations entre tables      
    //3-Permettre à JPA/Hibernate de générer les requêtes SQL
    
package com.appsante.entity;
import jakarta.persistence.*;//Toutes les annotations JPA (@Entity, @Table, @Id, @Column...)
import lombok.Data;//Génère automatiquement : getters, setters, toString, equals, hashCod
import lombok.NoArgsConstructor;//Génère un constructeur sans arguments 
import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateur")
@Data
@NoArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Integer idUtilisateur;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "mot_de_passe", nullable = false, length = 255)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "actif")
    private Boolean actif = true;

    // Géré par MySQL DEFAULT CURRENT_TIMESTAMP — Hibernate ne l'insère pas
    @Column(name = "date_creation", insertable = false, updatable = false)
    private LocalDateTime dateCreation;

    public enum Role {
        patient, medecin, receptionniste, admin
    }
}
