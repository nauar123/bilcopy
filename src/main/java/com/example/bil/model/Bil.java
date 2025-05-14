package com.example.bil.model;

public class Bil {


    private int bilId;
    private String stelNr;
    private String maerke;
    private String model;
    private String udstyrsniveau;
    private Status status;
    private double regAfgift;
    private double co2Udledning;


    public enum Status
    {
        Ledig,
        udlejet,
        skadet,
        tilbageleveret
    }

    public Bil(int bilId,String stelNr, String maerke,String model, String udstyrsniveau, Status status, double regAfgift, double co2Udledning)
    {
        this.bilId = bilId;
        this.stelNr = stelNr;
        this.maerke = maerke;
        this.model = model;
        this.udstyrsniveau = udstyrsniveau;
        this.status = status;
        this.regAfgift = regAfgift;
        this.co2Udledning = co2Udledning;
    }

    public int getBilId()
    {
        return bilId;
    }

    public String getStelNr()
    {
        return stelNr;
    }

    public String getMaerke()
    {
        return maerke;
    }

    public String getModel()
    {
        return model;
    }

    public String getUdstyrsniveau()
    {
        return  udstyrsniveau;
    }

    public Status status()
    {
        return status;
    }

    public Double getRegAfgift()
    {
        return regAfgift;
    }

    public Double getCo2Udledning()
    {
        return co2Udledning;
    }

    public void setBilId()
    {
        this.bilId = bilId;
    }

    public void setStelNr()
    {
        this.stelNr = stelNr;
    }

    public void setMaerke()
    {
        this.maerke = maerke;
    }

    public void setModel()
    {
        this.model= model;
    }
    
    public void setUdstyrsniveau()
    {
        this.udstyrsniveau = udstyrsniveau;
    }

    public void setStatus()
    {
        this.status = status;
    }

    public void setCo2Udledning()
    {
        this.co2Udledning = co2Udledning;
    }

    public void setRegAfgift()
    {
        this.regAfgift = regAfgift;
    }
}
