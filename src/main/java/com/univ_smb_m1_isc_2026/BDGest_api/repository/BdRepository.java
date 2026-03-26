package com.univ_smb_m1_isc_2026.BDGest_api.repository;

import com.univ_smb_m1_isc_2026.BDGest_api.model.Bd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BdRepository extends JpaRepository<Bd, Long> {
    List<Bd> findByAuteurContainingIgnoreCase(String auteur);

    List<Bd> findBySerieContainingIgnoreCase(String serie);

    List<Bd> findByTitreContainingIgnoreCase(String titre);

    List<Bd> findByIsbnContainingIgnoreCase(String isbn);
}