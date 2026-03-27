package com.univ_smb_m1_isc_2026.BDGest_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Email obligatoire")
    @Email(message = "Email invalide")
    private String mail;

    @NotBlank(message = "Mot de passe obligatoire")
    @Size(min = 4, message = "Mot de passe trop court (min 4 caractères)")
    private String mdp;

    // getters / setters
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getMdp() { return mdp; }
    public void setMdp(String mdp) { this.mdp = mdp; }
}