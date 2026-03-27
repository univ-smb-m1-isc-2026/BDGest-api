package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import com.univ_smb_m1_isc_2026.BDGest_api.dto.LoginRequest;
import com.univ_smb_m1_isc_2026.BDGest_api.dto.RegisterRequest;
import com.univ_smb_m1_isc_2026.BDGest_api.model.Utilisateur;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurController(UtilisateurRepository utilisateurRepository,
                                 PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // INSCRIPTION

    @PostMapping("/register")
    public String register(@RequestBody @Valid RegisterRequest request) {

        if (utilisateurRepository.findByMail(request.getMail()).isPresent()) {
            return "Email déjà utilisé";
        }

        Utilisateur user = new Utilisateur();
        user.setMail(request.getMail());

        String hashedPassword = passwordEncoder.encode(request.getMdp());
        user.setMdp(hashedPassword);

        utilisateurRepository.save(user);

        return "Utilisateur créé";
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest request) {

        return utilisateurRepository.findByMail(request.getMail())
                .map(user -> {
                    // 🔐 comparaison BCrypt
                    if (passwordEncoder.matches(request.getMdp(), user.getMdp())) {
                        return "Login réussi";
                    } else {
                        return "Mot de passe incorrect";
                    }
                })
                .orElse("Utilisateur non trouvé");
    }
}