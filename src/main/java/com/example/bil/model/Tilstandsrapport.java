package com.example.bil.model;


import java.util.Date;
import com.example.bil.model.Medarbejder;
import org.springframework.format.annotation.DateTimeFormat;

public class Tilstandsrapport
{
    private int tilstandsrapportId;
    private int bilId;
    private int kontraktId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tilstandsrapportDato;
    private int medarbejderId;
    private boolean erSkadet;


    public Tilstandsrapport(){}

    public Tilstandsrapport (int tilstandsrapportId, int bilId, int kontraktId, Date tilstandsrapportDato, int medarbejderId, Boolean erSkadet )
    {
        this.tilstandsrapportId = tilstandsrapportId;
        this.bilId = bilId;
        this.kontraktId = kontraktId;
        this.tilstandsrapportDato = tilstandsrapportDato;
        this.medarbejderId = medarbejderId;
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

    public int getMedarbejderId() {
        return medarbejderId;
    }

    public void setMedarbejderId(int medarbejderId) {
        this.medarbejderId = medarbejderId;
    }

}
