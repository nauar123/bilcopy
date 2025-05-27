package com.example.bil.model;

public class Kunde {

    private int kundeId;
    private String navn;
    private String adresse;
    private String telefonnr;
    private String email;

//F

    public Kunde(){}
    public Kunde (int kundeId, String navn, String adresse, String telefonnr,String email)
    {
        this.kundeId = kundeId;
        this.navn = navn;
        this.adresse = adresse;
        this.telefonnr = telefonnr;
        this.email = email;
    }


    public int getKundeId()
    {
        return kundeId;
    }
    public String getNavn()
    {
        return navn;
    }
    public String getAdresse()
    {
        return adresse;
    }
    public String getTelefonnr()
    {
        return telefonnr;
    }
    public String getEmail ()
    {
        return email;
    }


    public void setKundeId()
    {
        this.kundeId = kundeId;
    }


    public void setNavn()
    {
        this.navn = navn;
    }


    public void setAdresse()
    {
        this.adresse = adresse;
    }


    public void setTelefonnr() {
        this.telefonnr = telefonnr;
    }


    public void setEmail()
    {
        this.email = email;
    }
}

