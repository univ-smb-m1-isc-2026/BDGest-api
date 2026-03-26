package com.univ_smb_m1_isc_2026.BDGest_api.repository;

import com.univ_smb_m1_isc_2026.BDGest_api.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByMail(String mail);
}