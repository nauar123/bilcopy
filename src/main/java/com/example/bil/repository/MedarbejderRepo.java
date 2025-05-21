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

    // Tjek om login er gyldigt
    public boolean validateLogin(String email, String adgangskode) {
        try {
            System.out.println("Validerer login for: " + email);

            // Hent medarbejderen
            String selectSql = "SELECT * FROM medarbejder WHERE email = ?";
            Medarbejder medarbejder = null;
            try {
                RowMapper<Medarbejder> rowMapper = new BeanPropertyRowMapper<>(Medarbejder.class);
                medarbejder = template.queryForObject(selectSql, rowMapper, email);
                System.out.println("Fundet medarbejder: " + medarbejder.getNavn());
            } catch (Exception e) {
                System.out.println("Kunne ikke finde medarbejder med email: " + email);
                return false;
            }

            // Tjek adgangskode - ændret 'adgangskode' til 'kodeord'
            String sql = "SELECT COUNT(*) FROM medarbejder WHERE email = ? AND kodeord = ?";
            Integer count = template.queryForObject(sql, Integer.class, email, adgangskode);
            boolean isValid = (count != null && count > 0);

            System.out.println("Login gyldigt: " + isValid);
            return isValid;
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}