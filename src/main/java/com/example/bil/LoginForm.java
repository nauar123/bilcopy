package com.example.bil.model;

public class LoginForm {
    private String email;
    private String adgangskode;

    public LoginForm() {
    }

    public LoginForm(String email, String adgangskode) {
        this.email = email;
        this.adgangskode = adgangskode;
    }
//test
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
}