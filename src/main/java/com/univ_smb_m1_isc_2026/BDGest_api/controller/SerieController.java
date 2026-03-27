package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import com.univ_smb_m1_isc_2026.BDGest_api.model.Serie;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.SerieRepository;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/list-serie")
public class SerieController {

    private final SerieRepository serieRepository;

    public SerieController(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    // Lister toutes les séries
    @GetMapping
    public Map<String, Object> listAll() {
        Map<String, Object> response = new HashMap<>();
        List<Serie> series = serieRepository.findAll();
        response.put("count", series.size());
        response.put("series", series);
        return response;
    }

    // Recherche par nom de série
    @GetMapping("/search")
    public Map<String, Object> searchByName(@RequestParam String nom) {
        Map<String, Object> response = new HashMap<>();
        List<Serie> result = serieRepository.findByNomContainingIgnoreCase(nom);
        response.put("count", result.size());
        response.put("series", result);
        return response;
    }

    // Ajouter une série
    @PostMapping
    public Map<String, Object> addSerie(@RequestBody Serie serie) {
        Map<String, Object> response = new HashMap<>();
        Serie saved = serieRepository.save(serie);
        response.put("success", true);
        response.put("serieId", saved.getId());
        response.put("serie", saved);
        return response;
    }
}