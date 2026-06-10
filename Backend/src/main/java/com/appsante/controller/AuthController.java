package com.appsante.controller;

import com.appsante.dto.request.RegisterRequest;
import com.appsante.dto.response.RegisterResponse;
import com.appsante.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ② Reçoit la requête
    // ③ Valide les données (@Valid)
    // ④ Appelle le Service
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest req) {

        RegisterResponse response = authService.inscrirePatient(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
