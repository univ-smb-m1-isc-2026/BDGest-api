package com.univ_smb_m1_isc_2026.BDGest_api.repository;

import com.univ_smb_m1_isc_2026.BDGest_api.model.Bd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BdRepository extends JpaRepository<Bd, Long> {
    @Query(value = """
        SELECT b.* 
        FROM bd b
        LEFT JOIN auteur a ON b.auteur_id = a.id
        LEFT JOIN serie s ON b.serie_id = s.id
        WHERE (:titre IS NULL OR b.titre ILIKE %:titre%)
          AND (:auteur IS NULL OR a.nom ILIKE %:auteur%)
          AND (:serie IS NULL OR s.nom ILIKE %:serie%)
          AND (:isbn IS NULL OR b.isbn = :isbn)
            ORDER BY
                              -- 1. Priorité aux numéros purement numériques (ex: "1", "2", "11")
                              CASE WHEN b.numero ~ '^[0-9]+$' THEN 0 ELSE 1 END,
                              -- 2. Tri numérique pour les numéros purement numériques
                              CASE WHEN b.numero ~ '^[0-9]+$' THEN CAST(b.numero AS INTEGER) ELSE NULL END,
                              -- 3. Tri alphabétique pour les autres (ex: "HS", "HS01", "A1")
                              b.numero
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<Bd> findBdByFilters(
            @Param("titre") String titre,
            @Param("auteur") String auteur,
            @Param("serie") String serie,
            @Param("isbn") String isbn,
            @Param("limit") int limit,
            @Param("offset") int offset);
}