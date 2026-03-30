package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import com.univ_smb_m1_isc_2026.BDGest_api.dto.LoginRequest;
import com.univ_smb_m1_isc_2026.BDGest_api.dto.RegisterRequest;
import com.univ_smb_m1_isc_2026.BDGest_api.model.*;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.*;
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
    private final PretRepository pretRepository;

    private Utilisateur getUserFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtils.getUsernameFromJwt(token);
        return utilisateurRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    public UtilisateurController(UtilisateurRepository utilisateurRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtUtils jwtUtils, BdRepository bdRepository, SerieRepository serieRepository, AuteurRepository auteurRepository, PretRepository pretRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.bdRepository = bdRepository;
        this.serieRepository = serieRepository;
        this.auteurRepository = auteurRepository;
        this.pretRepository = pretRepository;
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

    // Supprimer le compte courant
    @DeleteMapping("/me")
    public Map<String, Object> deleteAccount(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        try {
            Utilisateur user = getUserFromToken(authHeader);

            utilisateurRepository.delete(user);

            response.put("success", true);
            response.put("message", "Compte supprimé");
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
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

    @GetMapping("/{userId}/collection")
    public Object getCollectionByUser(@PathVariable Long userId) {
        try {
            Utilisateur user = utilisateurRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

            return user.getCollection();

        } catch (Exception e) {
            return Map.of(
                    "success", false,
                    "error", e.getMessage()
            );
        }
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

    @GetMapping("/prets")
    public Object getPrets(@RequestHeader("Authorization") String authHeader) {
        try {
            Utilisateur user = getUserFromToken(authHeader);
            return user.getPrets();
        } catch (Exception e) {
            return Map.of(
                    "success", false,
                    "error", e.getMessage()
            );
        }
    }

    @PostMapping("/prets/{bdId}")
    public Map<String, Object> addPret(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long bdId,
            @RequestParam String emprunteur) {

        Map<String, Object> response = new HashMap<>();

        try {
            Utilisateur user = getUserFromToken(authHeader);

            Bd bd = bdRepository.findById(bdId)
                    .orElseThrow(() -> new RuntimeException("BD introuvable"));

            Pret pret = new Pret();
            pret.setBd(bd);
            pret.setUtilisateur(user);
            pret.setEmprunteur(emprunteur);
            pret.setDatePret(java.time.LocalDate.now());

            pretRepository.save(pret);

            response.put("success", true);
            response.put("message", "BD prêtée");

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    @PutMapping("/prets/{pretId}/retour")
    public Map<String, Object> rendrePret(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long pretId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Utilisateur user = getUserFromToken(authHeader);

            Pret pret = pretRepository.findById(pretId)
                    .orElseThrow(() -> new RuntimeException("Prêt introuvable"));

            if (!pret.getUtilisateur().getId().equals(user.getId())) {
                throw new RuntimeException("Non autorisé");
            }

            pret.setDateRetour(java.time.LocalDate.now());
            pretRepository.save(pret);

            response.put("success", true);
            response.put("message", "BD rendue");

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    @DeleteMapping("/prets/{pretId}")
    public Map<String, Object> deletePret(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long pretId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Utilisateur user = getUserFromToken(authHeader);

            Pret pret = pretRepository.findById(pretId)
                    .orElseThrow(() -> new RuntimeException("Prêt introuvable"));

            if (!pret.getUtilisateur().getId().equals(user.getId())) {
                throw new RuntimeException("Non autorisé");
            }

            pretRepository.delete(pret);

            response.put("success", true);
            response.put("message", "Prêt supprimé");

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

}