package com.example.bil.model;
import com.example.bil.model.Medarbejder;

public class Skaderapport
{
    private int skadeId;
    private int tilstandsrapportId;
    private int medarbejderId;
    private int antalSkader;
    private int prisPrSkade;
    private int prisTotal;
    private String beskrivelse;
public Skaderapport (){}
    public Skaderapport(int skadeId, int tilstandsrapportId, int medarbejderId, int antalSkader, int prisPrSkade, int prisTotal, String beskrivelse){
        this.skadeId = skadeId;
        this.tilstandsrapportId = tilstandsrapportId;
        this.medarbejderId = medarbejderId;
        this.antalSkader = antalSkader;
        this.prisPrSkade = prisPrSkade;
        this.prisTotal = prisTotal;
        this.beskrivelse = beskrivelse;
    }

    public int getSkadeId(){
        return skadeId;
    }
    public int getTilstandsrapportId(){
        return tilstandsrapportId;
    }
    public int getMedarbejderId(){
        return medarbejderId;
    }
    public int getAntalSkader(){
        return antalSkader;
    }
    public int getPrisPrSkade(){
        return prisPrSkade;
    }
    public int getPrisTotal(){
        return prisTotal;
    }
    public String getBeskrivelse(){
        return beskrivelse;
    }
    public void setSkadeId(int skadeId){
        this.skadeId = skadeId;
    }
    public void setTilstandsrapport(int tilstandsrapport){
        this.tilstandsrapportId = tilstandsrapport;
    }
    public void setMedarbejder(int medarbejder){
        this.medarbejderId = medarbejder;
    }
    public void setAntalSkader(int antalSkader){
        this.antalSkader = antalSkader;
    }
    public void setPrisPrSkade(int prisPrSkade){
        this.prisPrSkade = prisPrSkade;
    }
    public void setPrisTotal(int prisTotal){
        this.prisTotal = prisTotal;
    }
    public void setBeskrivelse(String beskrivelse){
        this.beskrivelse = beskrivelse;
    }
}

