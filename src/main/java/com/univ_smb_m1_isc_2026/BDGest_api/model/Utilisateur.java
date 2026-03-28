package com.univ_smb_m1_isc_2026.BDGest_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mail;
    private String mdp;
    @ManyToMany
    @JoinTable(
            name = "utilisateur_bd_collection",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "bd_id")
    )
    @JsonIgnore
    private Set<Bd> collection = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "utilisateur_serie_suivie",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "serie_id")
    )
    @JsonIgnore
    private Set<Serie> seriesSuivies = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "utilisateur_auteur_suivi",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "auteur_id")
    )
    @JsonIgnore
    private Set<Auteur> auteursSuivis = new HashSet<>();

    // getters / setters
    public Long getId() { return id; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getMdp() { return mdp; }
    public void setMdp(String mdp) { this.mdp = mdp; }

    public Set<Bd> getCollection() { return collection; }
    public void addCollection(Bd bd) { this.collection.add(bd); }

    public Set<Serie> getSeriesSuivies() { return seriesSuivies; }
    public void addSerie(Serie serie) { this.seriesSuivies.add(serie); }

    public Set<Auteur> getAuteursSuivis() { return auteursSuivis; }
    public void addAuteur(Auteur auteur) { this.auteursSuivis.add(auteur); }
}