package com.example.bil.repository;

import com.example.bil.model.Kunde;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KundeRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Henter alle kunder
    public List<Kunde> fetchAll() {
        String sql = "SELECT * FROM kunde";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Kunde.class));
    }

    // Finder kunder efter ID
    public Kunde findById(int kundeId) {
        String sql = "SELECT * FROM kunde WHERE kundeId = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Kunde.class), kundeId);
    }

    // Tilføj ny kunde
    public void addKunde(Kunde kunde) {
        System.out.println("KundeRepo: Forsøger at indsætte kunde i database");
        System.out.println("Repo - SQL bliver udført...");

        try {
            // Prøver UDEN kundeId (auto increment)
            String sql = "INSERT INTO kunde (navn, adresse, telefonnr, email) VALUES (?, ?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(sql, kunde.getNavn(), kunde.getAdresse(), kunde.getTelefonnr(), kunde.getEmail());
            System.out.println("KundeRepo: " + rowsAffected + " række(r) påvirket");
        } catch (Exception e) {
            System.out.println("KundeRepo FEJL: " + e.getMessage());
            System.out.println("SQL: INSERT INTO kunde (navn, adresse, telefonnr, email) VALUES (?, ?, ?, ?)");
            throw e;
        }
    }

    // Opdater eksisterende kunde
    public void updateKunde(Kunde kunde) {
        String sql = "UPDATE kunde SET navn = ?, adresse = ?, telefonnr = ?, email = ? WHERE kundeId = ?";
        jdbcTemplate.update(sql, kunde.getNavn(), kunde.getAdresse(), kunde.getTelefonnr(), kunde.getEmail(), kunde.getKundeId());
    }

    // Slet kunde efter ID
    public void deleteById(int kundeId) {
        String sql = "DELETE FROM kunde WHERE kundeId = ?";
        jdbcTemplate.update(sql, kundeId);
    }
}