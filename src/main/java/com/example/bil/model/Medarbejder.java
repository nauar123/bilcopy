package com.example.bil.model;

import jakarta.persistence.*;

@Entity
@Table(name = "medarbejder")
public class Medarbejder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medarbejder_id")
    private Integer medarbejderId;

    @Column(name = "navn")
    private String navn;

    @Column(name = "email")
    private String email;

    @Column(name = "adgangskode")
    private String adgangskode;

    @Column(name = "rolle")
    private String rolle;

    // Standard constructors, getters, and setters
    public Medarbejder() {}

    public Medarbejder(Integer medarbejderId, String navn, String email, String adgangskode, String rolle) {
        this.medarbejderId = medarbejderId;
        this.navn = navn;
        this.email = email;
        this.adgangskode = adgangskode;
        this.rolle = rolle;
    }

    public Integer getMedarbejderId() {
        return medarbejderId;
    }

    public void setMedarbejderId(Integer medarbejderId) {
        this.medarbejderId = medarbejderId;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdgangskode() {
        return adgangskode;
    }

    public void setAdgangskode(String adgangskode) {
        this.adgangskode = adgangskode;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }
}