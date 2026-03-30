package com.univ_smb_m1_isc_2026.BDGest_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "utilisateur_bd_pret")
public class Pret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Utilisateur qui prête
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    @JsonIgnore
    private Utilisateur utilisateur;

    // BD prêtée
    @ManyToOne
    @JoinColumn(name = "bd_id", nullable = false)
    private Bd bd;

    // À qui on prête
    @Column(nullable = false)
    private String emprunteur;

    // Date de prêt
    private LocalDate datePret = LocalDate.now();

    // Date de retour (null = pas encore rendu)
    private LocalDate dateRetour;

    // ===== GETTERS / SETTERS =====

    public Long getId() {
        return id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Bd getBd() {
        return bd;
    }

    public void setBd(Bd bd) {
        this.bd = bd;
    }

    public String getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(String emprunteur) {
        this.emprunteur = emprunteur;
    }

    public LocalDate getDatePret() {
        return datePret;
    }

    public void setDatePret(LocalDate datePret) {
        this.datePret = datePret;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(LocalDate dateRetour) {
        this.dateRetour = dateRetour;
    }
}