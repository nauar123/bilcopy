package com.example.bil.model;

public class Kunde {

    private int kundeId;
    private String navn;
    private String adresse;
    private String telefonnr;
    private String email;

    public Kunde() {}

    public Kunde(int kundeId, String navn, String adresse, String telefonnr, String email) {
        this.kundeId = kundeId;
        this.navn = navn;
        this.adresse = adresse;
        this.telefonnr = telefonnr;
        this.email = email;
    }

    // Getters
    public int getKundeId() {
        return kundeId;
    }

    public String getNavn() {
        return navn;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTelefonnr() {
        return telefonnr;
    }

    public String getEmail() {
        return email;
    }

    // FIXED: Setters with parameters
    public void setKundeId(int kundeId) {
        this.kundeId = kundeId;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setTelefonnr(String telefonnr) {
        this.telefonnr = telefonnr;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}