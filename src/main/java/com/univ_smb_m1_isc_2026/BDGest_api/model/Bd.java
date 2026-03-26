package com.univ_smb_m1_isc_2026.BDGest_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bd")  // Correspond à ta table "bd" dans PostgreSQL
public class Bd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serie;
    private String numero;
    private String titre;
    private String auteur;
    private String isbn;
    private String date_parution;
    private int pages;
    private String url_image;
    @Column(columnDefinition = "TEXT")
    private String synopsis;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getDate_parution() { return date_parution; }
    public void setDate_parution(String date_parution) { this.date_parution = date_parution; }

    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }

    public String getUrl_image() { return url_image; }
    public void setUrl_image(String url_image) { this.url_image = url_image; }

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }
}