package com.example.bil.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Forretningsudvikler
{
    @Id

    protected int bilId;
    protected String stelNr;
    protected String maerke;
    protected String model;
    protected String status; // fx "ledig", "ikke ledig"
    protected double samletIndtaegt;

    protected int getBilId()
    {
        return bilId;
    }

    public void setBilId(int bilId)
    {
        this.bilId = bilId;
    }

    public String getStelNr()
    {
        return stelNr;
    }

    public void setStelNr(String stelNr)
    {
        this.stelNr = stelNr;
    }

    public String getMaerke()
    {
        return maerke;
    }
    public void setMaerke(String maerke)
    {
        this.maerke = maerke;
    }
    public String getModel()
    {
        return model;
    }
    public void setModel(String model)
    {
        this.model = model;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
    public double getSamletIndtaegt()
    {
        return samletIndtaegt;
    }
    public void setSamletIndtaegt(double samletIndtaegt)
    {
        this.samletIndtaegt = samletIndtaegt;
    }

}
