//🎯 RÔLE : Démarrer l'application Spring Boot    
package com.appsante;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppSanteApplication {

    public static void main(String[] args) {
        //• SpringApplication.run() démarre le serveur Tomcat intégré 
        SpringApplication.run(AppSanteApplication.class, args);
    }
}
