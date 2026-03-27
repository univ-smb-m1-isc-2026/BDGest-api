package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import com.univ_smb_m1_isc_2026.BDGest_api.model.Auteur;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.AuteurRepository;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/list-auteur")
public class AuteurController {

    private final AuteurRepository auteurRepository;

    public AuteurController(AuteurRepository auteurRepository) {
        this.auteurRepository = auteurRepository;
    }

    // Lister tous les auteurs
    @GetMapping
    public Map<String, Object> listAll() {
        Map<String, Object> response = new HashMap<>();
        List<Auteur> auteurs = auteurRepository.findAll();
        response.put("count", auteurs.size());
        response.put("auteurs", auteurs);
        return response;
    }

    // Recherche par nom d'auteur
    @GetMapping("/search")
    public Map<String, Object> searchByName(@RequestParam String nom) {
        Map<String, Object> response = new HashMap<>();
        List<Auteur> result = auteurRepository.findByNomContainingIgnoreCase(nom);
        response.put("count", result.size());
        response.put("auteurs", result);
        return response;
    }

    // Ajouter un auteur
    @PostMapping
    public Map<String, Object> addAuteur(@RequestBody Auteur auteur) {
        Map<String, Object> response = new HashMap<>();
        Auteur saved = auteurRepository.save(auteur);
        response.put("success", true);
        response.put("auteurId", saved.getId());
        response.put("auteur", saved);
        return response;
    }
}