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

    // RETTET: Tilføj ny kunde - to versioner alt efter om I bruger auto-increment

    // Brug denne hvis kundeId er AUTO_INCREMENT i databasen
    public void addKundeAutoIncrement(Kunde kunde) {
        String sql = "INSERT INTO kunde (navn, adresse, telefonnr, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, kunde.getNavn(), kunde.getAdresse(), kunde.getTelefonnr(), kunde.getEmail());
    }

    // Brug denne hvis I selv angiver kundeId
    public void addKundeManualId(Kunde kunde) {
        String sql = "INSERT INTO kunde (kundeId, navn, adresse, telefonnr, email) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, kunde.getKundeId(), kunde.getNavn(), kunde.getAdresse(), kunde.getTelefonnr(), kunde.getEmail());
    }

    // Hovedmetode - vælg den rigtige baseret på jeres database-setup
    public void addKunde(Kunde kunde) {
        // VÆLG EN AF DISSE TO LINJER:

        // Hvis kundeId er AUTO_INCREMENT i database:
        addKundeAutoIncrement(kunde);

        // Hvis I selv angiver kundeId:
        // addKundeManualId(kunde);
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