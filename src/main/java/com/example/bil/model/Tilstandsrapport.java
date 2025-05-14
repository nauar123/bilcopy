package com.example.bil.model;


import java.util.Date;


public class Tilstandsrapport
{
    private int tilstandsrapportId;
    private int bilId;
    private int kontraktId;
    private Date tilstandsrapportDato;
    private boolean erSkadet;




    public Tilstandsrapport (int tilstandsrapportId, int bilId, int kontraktId, Date tilstandsrapportDato, boolean erSkadet )
    {
        this.tilstandsrapportId = tilstandsrapportId;
        this.bilId = bilId;
        this.kontraktId = kontraktId;
        this.tilstandsrapportDato = tilstandsrapportDato;
        this.erSkadet = erSkadet;
    }


    public int getTilstandsrapportId() {
        return tilstandsrapportId;
    }
    public void setTilstandsrapportId(int tilstandsrapportId) {
        this.tilstandsrapportId = tilstandsrapportId;
    }
    public int getBilId() {
        return bilId;
    }
    public void setBilId(int bilId) {
        this.bilId = bilId;
    }
    public int getKontraktId() {
        return kontraktId;
    }
    public void setKontraktId(int kontraktId) {
        this.kontraktId = kontraktId;
    }
    public Date getTilstandsrapportDato() {


        return tilstandsrapportDato;
    }
    public void setTilstandsrapportDato(Date tilstandsrapportDato) {
        this.tilstandsrapportDato = tilstandsrapportDato;
    }
    public boolean isErSkadet() {
        return erSkadet;
    }
    public void setErSkadet(boolean erSkadet) {
        this.erSkadet = erSkadet;
    }

}
