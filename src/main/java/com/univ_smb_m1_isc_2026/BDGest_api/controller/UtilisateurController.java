package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import com.univ_smb_m1_isc_2026.BDGest_api.dto.LoginRequest;
import com.univ_smb_m1_isc_2026.BDGest_api.dto.RegisterRequest;
import com.univ_smb_m1_isc_2026.BDGest_api.model.Auteur;
import com.univ_smb_m1_isc_2026.BDGest_api.model.Bd;
import com.univ_smb_m1_isc_2026.BDGest_api.model.Serie;
import com.univ_smb_m1_isc_2026.BDGest_api.model.Utilisateur;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.AuteurRepository;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.BdRepository;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.SerieRepository;
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
    private final BdRepository bdRepository;
    private final SerieRepository serieRepository;
    private final AuteurRepository auteurRepository;

    private Utilisateur getUserFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtils.getUsernameFromJwt(token);
        return utilisateurRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    public UtilisateurController(UtilisateurRepository utilisateurRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtUtils jwtUtils, BdRepository bdRepository, SerieRepository serieRepository, AuteurRepository auteurRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.bdRepository = bdRepository;
        this.serieRepository = serieRepository;
        this.auteurRepository = auteurRepository;
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

    @GetMapping("/collection")
    public Object getCollection(@RequestHeader("Authorization") String authHeader) {
        try {
            Utilisateur user = getUserFromToken(authHeader);
            return user.getCollection();
        } catch (Exception e) {
            return Map.of(
                    "success", false,
                    "error", e.getMessage()
            );
        }
    }

    @PostMapping("/collection/{bdId}")
    public Map<String, Object> addBdToCollection(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long bdId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Utilisateur user = getUserFromToken(authHeader);

            Bd bd = bdRepository.findById(bdId)
                    .orElseThrow(() -> new RuntimeException("BD introuvable"));

            user.addCollection(bd);
            utilisateurRepository.save(user);

            response.put("success", true);
            response.put("message", "BD ajoutée à la collection");

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    @DeleteMapping("/collection/{bdId}")
    public Map<String, Object> removeBdFromCollection(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long bdId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Utilisateur user = getUserFromToken(authHeader);

            Bd bd = bdRepository.findById(bdId)
                    .orElseThrow(() -> new RuntimeException("BD introuvable"));

            user.getCollection().remove(bd);
            utilisateurRepository.save(user);

            response.put("success", true);
            response.put("message", "BD retirée de la collection");

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    @GetMapping("/series")
    public Object getSeries(@RequestHeader("Authorization") String authHeader) {
        try {
            Utilisateur user = getUserFromToken(authHeader);
            return user.getSeriesSuivies();
        } catch (Exception e) {
            return Map.of(
                    "success", false,
                    "error", e.getMessage()
            );
        }
    }

    @PostMapping("/series/{serieId}")
    public Map<String, Object> followSerie(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long serieId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Utilisateur user = getUserFromToken(authHeader);

            Serie serie = serieRepository.findById(serieId)
                    .orElseThrow(() -> new RuntimeException("Série introuvable"));

            user.addSerie(serie);
            utilisateurRepository.save(user);

            response.put("success", true);
            response.put("message", "Série suivie");

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    @DeleteMapping("/series/{serieId}")
    public Map<String, Object> unfollowSerie(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long serieId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Utilisateur user = getUserFromToken(authHeader);

            Serie serie = serieRepository.findById(serieId)
                    .orElseThrow(() -> new RuntimeException("Série introuvable"));

            user.getSeriesSuivies().remove(serie);
            utilisateurRepository.save(user);

            response.put("success", true);
            response.put("message", "Série retirée des suivis");

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    @GetMapping("/auteurs")
    public Object getAuteurs(@RequestHeader("Authorization") String authHeader) {
        try {
            Utilisateur user = getUserFromToken(authHeader);
            return user.getAuteursSuivis();
        } catch (Exception e) {
            return Map.of(
                    "success", false,
                    "error", e.getMessage()
            );
        }
    }

    @PostMapping("/auteurs/{auteurId}")
    public Map<String, Object> followAuteur(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long auteurId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Utilisateur user = getUserFromToken(authHeader);

            Auteur auteur = auteurRepository.findById(auteurId)
                    .orElseThrow(() -> new RuntimeException("Auteur introuvable"));

            user.addAuteur(auteur);
            utilisateurRepository.save(user);

            response.put("success", true);
            response.put("message", "Auteur suivi");

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    @DeleteMapping("/auteurs/{auteurId}")
    public Map<String, Object> unfollowAuteur(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long auteurId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Utilisateur user = getUserFromToken(authHeader);

            Auteur auteur = auteurRepository.findById(auteurId)
                    .orElseThrow(() -> new RuntimeException("Auteur introuvable"));

            user.getAuteursSuivis().remove(auteur);
            utilisateurRepository.save(user);

            response.put("success", true);
            response.put("message", "Auteur retiré des suivis");

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }
}