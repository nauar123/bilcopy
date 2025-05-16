package com.example.bil.model;

public class Medarbejder {
    private int medarbejderId;
    private String navn;
    private String email;
    private String kodeord;
    private String rolle;

    // Default constructor
    public Medarbejder() {
    }

    // Constructor med alle felter
    public Medarbejder(int medarbejderId, String navn, String email, String kodeord, String rolle) {
        this.medarbejderId = medarbejderId;
        this.navn = navn;
        this.email = email;
        this.kodeord = kodeord;
        this.rolle = rolle;
    }

    // Getters og setters
    public int getMedarbejderId() {
        return medarbejderId;
    }

    public void setMedarbejderId(int medarbejderId) {
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

    public String getKodeord() {
        return kodeord;
    }

    public void setKodeord(String kodeord) {
        this.kodeord = kodeord;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }
}