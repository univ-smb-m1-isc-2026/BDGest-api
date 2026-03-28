package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import com.univ_smb_m1_isc_2026.BDGest_api.dto.LoginRequest;
import com.univ_smb_m1_isc_2026.BDGest_api.dto.RegisterRequest;
import com.univ_smb_m1_isc_2026.BDGest_api.model.Utilisateur;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.UtilisateurRepository;
import com.univ_smb_m1_isc_2026.BDGest_api.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UtilisateurController(UtilisateurRepository utilisateurRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtUtils jwtUtils) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    // INSCRIPTION
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody @Valid RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();

        if (utilisateurRepository.findByMail(request.getMail()).isPresent()) {
            response.put("success", false);
            response.put("message", "Email déjà utilisé");
            return response;
        }

        Utilisateur user = new Utilisateur();
        user.setMail(request.getMail());
        user.setMdp(passwordEncoder.encode(request.getMdp()));

        utilisateurRepository.save(user);

        response.put("success", true);
        response.put("message", "Utilisateur créé");
        response.put("userId", user.getId());
        return response;
    }

    // LOGIN avec JWT
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody @Valid LoginRequest request) {
        Map<String, Object> response = new HashMap<>();

        utilisateurRepository.findByMail(request.getMail())
                .ifPresentOrElse(user -> {
                    if (passwordEncoder.matches(request.getMdp(), user.getMdp())) {
                        // Génère un token JWT
                        String token = jwtUtils.generateJwtToken(user.getMail());

                        response.put("success", true);
                        response.put("message", "Login réussi");
                        response.put("userId", user.getId());
                        response.put("token", token); // <-- nouveau
                    } else {
                        response.put("success", false);
                        response.put("message", "Mot de passe incorrect");
                    }
                }, () -> {
                    response.put("success", false);
                    response.put("message", "Utilisateur non trouvé");
                });

        return response;
    }

    // Endpoint test pour récupérer l'utilisateur connecté via JWT
    @GetMapping("/me")
    public Map<String, Object> me(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtils.getUsernameFromJwt(token);
            response.put("email", email);
            response.put("valid", jwtUtils.validateJwtToken(token));
        } catch (Exception e) {
            response.put("valid", false);
            response.put("error", e.getMessage());
        }
        return response;
    }
}