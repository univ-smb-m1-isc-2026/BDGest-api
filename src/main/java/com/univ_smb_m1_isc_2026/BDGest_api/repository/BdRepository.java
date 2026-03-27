package com.univ_smb_m1_isc_2026.BDGest_api.repository;

import com.univ_smb_m1_isc_2026.BDGest_api.model.Bd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BdRepository extends JpaRepository<Bd, Long> {

    // Recherche par nom d'auteur
    List<Bd> findByAuteurNomContainingIgnoreCase(String nom);

    // Recherche par nom de série
    List<Bd> findBySerieNomContainingIgnoreCase(String nom);

    // Recherche par titre
    List<Bd> findByTitreContainingIgnoreCase(String titre);

    // Recherche par ISBN
    List<Bd> findByIsbnContainingIgnoreCase(String isbn);
}