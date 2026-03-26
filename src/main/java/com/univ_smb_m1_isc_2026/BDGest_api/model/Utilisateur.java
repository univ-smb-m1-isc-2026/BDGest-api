package com.univ_smb_m1_isc_2026.BDGest_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mail;
    private String mdp;

    // getters / setters
    public Long getId() { return id; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getMdp() { return mdp; }
    public void setMdp(String mdp) { this.mdp = mdp; }
}