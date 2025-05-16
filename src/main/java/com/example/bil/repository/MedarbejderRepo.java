package com.example.bil.repository;

import com.example.bil.model.Medarbejder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MedarbejderRepo {

    @Autowired
    JdbcTemplate template;

    // Find alle medarbejdere
    public List<Medarbejder> findAll() {
        String sql = "SELECT * FROM medarbejder";
        RowMapper<Medarbejder> rowMapper = new BeanPropertyRowMapper<>(Medarbejder.class);
        return template.query(sql, rowMapper);
    }

    // Find en medarbejder baseret på email
    public Medarbejder findByEmail(String email) {
        String sql = "SELECT * FROM medarbejder WHERE email = ?";

        try {
            RowMapper<Medarbejder> rowMapper = new BeanPropertyRowMapper<>(Medarbejder.class);
            return template.queryForObject(sql, rowMapper, email);
        } catch (Exception e) {
            // Returnerer null hvis ingen medarbejder blev fundet
            return null;
        }
    }

    // Tjek om login er gyldigt med avanceret debugging
    public boolean validateLogin(String email, String adgangskode) {
        try {
            System.out.println("Validerer login for: " + email + ", Adgangskode: " + adgangskode);

            // Direkte SQL forespørgsel først for at se rå data
            try {
                String directSql = "SELECT kodeord FROM medarbejder WHERE email = ?";
                String directPassword = template.queryForObject(directSql, String.class, email);
                System.out.println("Direkte SQL kodeord: '" + directPassword + "', længde: " + directPassword.length());
            } catch (Exception e) {
                System.out.println("Fejl ved direkte SQL-kald: " + e.getMessage());
            }

            // Hent medarbejderen for at se de faktiske værdier
            String selectSql = "SELECT * FROM medarbejder WHERE email = ?";
            Medarbejder medarbejder = null;
            try {
                RowMapper<Medarbejder> rowMapper = new BeanPropertyRowMapper<>(Medarbejder.class);
                medarbejder = template.queryForObject(selectSql, rowMapper, email);
                System.out.println("Fundet medarbejder: " + medarbejder.getNavn());
                System.out.println("Database kodeord: '" + medarbejder.getKodeord() + "', længde: " + medarbejder.getKodeord().length());
                System.out.println("Indtastet adgangskode: '" + adgangskode + "', længde: " + adgangskode.length());
            } catch (Exception e) {
                System.out.println("Kunne ikke finde medarbejder med email: " + email);
                e.printStackTrace();
                return false;
            }

            // Trim værdier for at eliminere whitespace-problemer
            String trimmedDbPassword = medarbejder.getKodeord().trim();
            String trimmedInputPassword = adgangskode.trim();

            System.out.println("Trimmet DB kodeord: '" + trimmedDbPassword + "', længde: " + trimmedDbPassword.length());
            System.out.println("Trimmet input kodeord: '" + trimmedInputPassword + "', længde: " + trimmedInputPassword.length());

            // Sammenlign trimmede værdier
            boolean passwordMatches = trimmedDbPassword.equals(trimmedInputPassword);
            System.out.println("Kodeord matcher efter trim: " + passwordMatches);

            // Hvis de ikke matcher, lad os sammenligne dem tegn for tegn
            if (!passwordMatches) {
                System.out.println("Tegn-for-tegn sammenligning:");
                for (int i = 0; i < Math.min(trimmedDbPassword.length(), trimmedInputPassword.length()); i++) {
                    char dbChar = trimmedDbPassword.charAt(i);
                    char inputChar = trimmedInputPassword.charAt(i);
                    System.out.println("Position " + i + ": DB='" + dbChar + "' (ASCII: " + (int)dbChar +
                            "), Input='" + inputChar + "' (ASCII: " + (int)inputChar + ")");
                }

                // Hvis længderne er forskellige, rapporter det også
                if (trimmedDbPassword.length() != trimmedInputPassword.length()) {
                    System.out.println("Advarsel: Forskellige længder efter trim!");
                }
            }

            // Prøv også at sammenligne med case-insensitive
            boolean caseInsensitiveMatch = trimmedDbPassword.equalsIgnoreCase(trimmedInputPassword);
            if (caseInsensitiveMatch && !passwordMatches) {
                System.out.println("Kodeord matcher ved case-insensitive sammenligning!");
            }

            // Normal validering med SQL
            String sql = "SELECT COUNT(*) FROM medarbejder WHERE email = ? AND kodeord = ?";
            Integer count = template.queryForObject(sql, Integer.class, email, adgangskode);
            System.out.println("SQL resultat count med original input: " + count);

            // Prøv også med trimmede værdier
            Integer trimmedCount = template.queryForObject(sql, Integer.class, email, trimmedInputPassword);
            System.out.println("SQL resultat count med trimmet input: " + trimmedCount);

            // Beslut om login er gyldigt baseret på trimmede værdier
            boolean isValid = (trimmedCount != null && trimmedCount > 0);
            System.out.println("Login gyldigt: " + isValid);

            return isValid;
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}