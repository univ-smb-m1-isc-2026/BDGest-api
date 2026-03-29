package com.univ_smb_m1_isc_2026.BDGest_api.repository;

import com.univ_smb_m1_isc_2026.BDGest_api.model.Pret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PretRepository extends JpaRepository<Pret, Long> {

    List<Pret> findByUtilisateurId(Long utilisateurId);

    List<Pret> findByUtilisateurIdAndDateRetourIsNull(Long utilisateurId);
}