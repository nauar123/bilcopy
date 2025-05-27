package com.example.bil.model;

import java.time.LocalDate;

public class Lejekontrakt {
    private int kontraktId; // FIXED: Consistent casing
    private int kundeId;
    private int bilId;
    private LocalDate startDato;
    private LocalDate slutDato;
    private AbonnementType abonnementType;
    private double pris;
    private int medarbejderId;



    public enum AbonnementType {
        limited, unlimited
    }

    public Lejekontrakt() {}

    public Lejekontrakt(int kontraktId, int kundeId, int bilId, LocalDate startDato,
                        LocalDate slutDato, AbonnementType abonnementType, double pris, int medarbejderId) {
        this.kontraktId = kontraktId;
        this.kundeId = kundeId;
        this.bilId = bilId;
        this.startDato = startDato;
        this.slutDato = slutDato;
        this.abonnementType = abonnementType;
        this.pris = pris;
        this.medarbejderId = medarbejderId;
    }

    // Getters
    public int getKontraktId() {
        return kontraktId;
    }

    public int getKundeId() {
        return kundeId;
    }

    public int getBilId() {
        return bilId;
    }

    public LocalDate getStartDato() {
        return startDato;
    }

    public LocalDate getSlutDato() {
        return slutDato;
    }

    public AbonnementType getAbonnementType() {
        return abonnementType;
    }

    public double getPris() {
        return pris;
    }

   public int getMedarbejderId()
   {
       return medarbejderId;
   }

    public void setKontraktId(int kontraktId) {
        this.kontraktId = kontraktId;
    }

    public void setKundeId(int kundeId) {
        this.kundeId = kundeId;
    }

    public void setBilId(int bilId) {
        this.bilId = bilId;
    }


    public void setStartDato(LocalDate startDato) {
        this.startDato = startDato;
    }

    public void setSlutDato(LocalDate slutDato) {
        this.slutDato = slutDato;
    }

    public void setPris(double pris) {
        this.pris = pris;
    }

    public void setAbonnementType(AbonnementType abonnementType) {
        this.abonnementType = abonnementType;
    }

    public void setMedarbejderId(int medarbejderId)
    {
        this.medarbejderId=medarbejderId;
    }


}