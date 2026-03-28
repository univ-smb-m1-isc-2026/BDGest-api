package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import com.univ_smb_m1_isc_2026.BDGest_api.model.Bd;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.BdRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class BdController {

    private final BdRepository bdRepository;

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
                code { background: #f4f4f4; margin: 5px; display: block; }
                .endpoint { margin-bottom: 15px; }
            </style>
        </head>
        <body>
            <h1>BDGest API</h1>
            <p>L'API fonctionne correctement ✅</p>

            <h2>BD - Requêtes disponibles</h2>

            <div class="endpoint">
                <b>BD aléatoires :</b>
                <code>/random-bd/{nb}</code>
                Exemple : <code>/random-bd/5</code>
            </div>

            <div class="endpoint">
                <b>Recherche BD (tous filtres disponibles) :</b>
                <code>/search?titre=&lt;Titre&gt;&auteur=&lt;Auteur&gt;&serie=&lt;Serie&gt;&isbn=&lt;ISBN&gt;&limit=&lt;Nombre&gt;</code>
                <p>Exemples :</p>
                <code>/search?titre=Tintin</code>
                <code>/search?auteur=Hergé</code>
                <code>/search?serie=Astérix&limit=5</code>
            </div>

            <h2>Auteurs - Requêtes disponibles</h2>

            <div class="endpoint">
                <b>Liste complète :</b>
                <code>/list-auteur</code>
            </div>

            <div class="endpoint">
                <b>Recherche par nom :</b>
                <code>/list-auteur/search?nom=Nom</code>
            </div>

            <div class="endpoint">
                <b>Ajouter un auteur :</b>
                <code>POST /list-auteur</code>
            </div>

            <h2>Séries - Requêtes disponibles</h2>

            <div class="endpoint">
                <b>Liste complète :</b>
                <code>/list-serie</code>
            </div>

            <div class="endpoint">
                <b>Recherche par nom :</b>
                <code>/list-serie/search?nom=Nom</code>
            </div>

            <div class="endpoint">
                <b>Ajouter une série :</b>
                <code>POST /list-serie</code>
            </div>
        </body>
        </html>
        """;
    }

    @GetMapping("/search")
    public List<Bd> searchBd(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String auteur,
            @RequestParam(required = false) String serie,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false, defaultValue = "12") int limit) {

        // Appeler le repository avec une méthode personnalisée
        return bdRepository.findBdByFilters(titre, auteur, serie, isbn, limit);
    }

    @GetMapping("/random-bd/{nb}")
    public List<Bd> randBd(@PathVariable int nb) {
        List<Bd> allBds = new ArrayList<>(bdRepository.findAll());
        Collections.shuffle(allBds); // Mélange aléatoirement
        return allBds.stream().limit(nb).toList();
    }
}