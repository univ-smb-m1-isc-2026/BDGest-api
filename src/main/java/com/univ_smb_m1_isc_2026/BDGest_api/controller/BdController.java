package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import com.univ_smb_m1_isc_2026.BDGest_api.model.Bd;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.BdRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class BdController {

    private final BdRepository bdRepository;

    // Injection du repository via le constructeur
    public BdController(BdRepository bdRepository) {
        this.bdRepository = bdRepository;
    }

    @GetMapping("/")
    public String home() {
        return """
    <html>
    <head>
        <title>BDGest API</title>
        <style>
            body { font-family: Arial; margin: 40px; }
            h1 { color: green; }
            code { background: #f4f4f4; margin: 5px; }
            .endpoint { margin-bottom: 10px; }
        </style>
    </head>
    <body>
        <h1>BDGest API</h1>
        <p>L'API fonctionne correctement ✅</p>

        <h2>Requêtes disponibles</h2>

        <div class="endpoint">
            <b>Liste complète :</b><br>
            <code>/list-bd</code>
        </div>

        <div class="endpoint">
            <b>BD aléatoires :</b><br>
            <code>/random-bd/{nb}</code><br>
            Exemple : <code>/random-bd/5</code>
        </div>

        <h2>Recherche</h2>

        <div class="endpoint">
            <b>Par auteur :</b><br>
            <code>/search/auteur?auteur=Nom</code>
        </div>

        <div class="endpoint">
            <b>Par série :</b><br>
            <code>/search/serie?serie=Nom</code>
        </div>

        <div class="endpoint">
            <b>Par titre :</b><br>
            <code>/search/titre?titre=Nom</code>
        </div>

        <div class="endpoint">
            <b>Par ISBN :</b><br>
            <code>/search/isbn?isbn=Code</code>
        </div>

        <h2>Exemples</h2>
        <code>/search/auteur?auteur=Isayama</code><br>
        <code>/search/serie?serie=Kid Paddle</code><br>
        <code>/search/titre?titre=Vol 714</code><br>
        <code>/search/isbn?isbn=2205049348</code>
    </body>
    </html>
    """;
    }

    // Lister tt les bandes dessinées
    @GetMapping("/list-bd")
    public List<Bd> testBd() {
        return bdRepository.findAll();
    }

    // Recherche par auteur
    @GetMapping("/search/auteur")
    public List<Bd> searchByAuteur(@RequestParam String auteur) {
        return bdRepository.findByAuteurContainingIgnoreCase(auteur);
    }

    // Recherche par série
    @GetMapping("/search/serie")
    public List<Bd> searchBySerie(@RequestParam String serie) {
        return bdRepository.findBySerieContainingIgnoreCase(serie);
    }

    // Recherche par titre
    @GetMapping("/search/titre")
    public List<Bd> searchByTitre(@RequestParam String titre) {
        return bdRepository.findByTitreContainingIgnoreCase(titre);
    }

    // Recherche par ISBN
    @GetMapping("/search/isbn")
    public List<Bd> searchByIsbn(@RequestParam String isbn) {
        return bdRepository.findByIsbnContainingIgnoreCase(isbn);
    }


    // BD aléatoires
    @GetMapping("/random-bd/{nb}")
    public List<Bd> randBd(@PathVariable int nb) {
        return bdRepository.findAll()
                .stream()
                .sorted((a, b) -> Math.random() > 0.5 ? 1 : -1)
                .limit(nb)
                .toList();
    }
}