package com.appsante.controller;

import com.appsante.dto.request.CreateEtablissementRequest;
import com.appsante.dto.request.CreateMedecinRequest;
import com.appsante.dto.request.CreateReceptionnisteRequest;
import com.appsante.dto.response.*;
import com.appsante.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/medecins")
    public ResponseEntity<MedecinResponse> creerMedecin(@Valid @RequestBody CreateMedecinRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.creerMedecin(req));
    }

    @PostMapping("/receptionnistes")
    public ResponseEntity<ReceptionnisteResponse> creerReceptionniste(@Valid @RequestBody CreateReceptionnisteRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.creerReceptionniste(req));
    }

    @GetMapping("/medecins")
    public ResponseEntity<List<MedecinResponse>> listerMedecins() {
        return ResponseEntity.ok(adminService.listerMedecins());
    }

    @GetMapping("/receptionnistes")
    public ResponseEntity<List<ReceptionnisteResponse>> listerReceptionnistes() {
        return ResponseEntity.ok(adminService.listerReceptionnistes());
    }

    @GetMapping("/villes")
    public ResponseEntity<List<VilleResponse>> listerVilles() {
        return ResponseEntity.ok(adminService.listerVilles());
    }

    @GetMapping("/secteurs")
    public ResponseEntity<List<SecteurResponse>> listerSecteurs(@RequestParam Integer idVille) {
        return ResponseEntity.ok(adminService.listerSecteurs(idVille));
    }

    @DeleteMapping("/medecins/{id}")
    public ResponseEntity<Void> supprimerMedecin(@PathVariable Integer id) {
        adminService.supprimerMedecin(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/receptionnistes/{id}")
    public ResponseEntity<Void> supprimerReceptionniste(@PathVariable Integer id) {
        adminService.supprimerReceptionniste(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/etablissements")
    public ResponseEntity<EtablissementResponse> creerEtablissement(@Valid @RequestBody CreateEtablissementRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.creerEtablissement(req));
    }

    @GetMapping("/etablissements")
    public ResponseEntity<List<EtablissementResponse>> listerEtablissements(
            @RequestParam(required = false) Integer idSecteur) {
        if (idSecteur != null) {
            return ResponseEntity.ok(adminService.listerEtablissementsParSecteur(idSecteur));
        }
        return ResponseEntity.ok(adminService.listerEtablissements());
    }

    @GetMapping("/domaines")
    public ResponseEntity<List<DomaineResponse>> listerDomaines() {
        return ResponseEntity.ok(adminService.listerDomaines());
    }
}
