package com.example.bil.repository;
import com.example.bil.model.Lejekontrakt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class LejekontraktRepo {
    @Autowired
    JdbcTemplate template;

    // RETTET: Custom RowMapper til at håndtere enum conversion
    public List<Lejekontrakt> fetchAll() {
        String sql = "SELECT " +
                "kontrakt_id, " +
                "kunde_id, " +
                "bil_id, " +
                "start_dato, " +
                "slut_dato, " +
                "abonnement_type, " +
                "pris, " +
                "medarbejder_id " +
                "FROM lejekontrakt";

        return template.query(sql, new LejekontraktRowMapper());
    }

    // Custom RowMapper klasse
    private static class LejekontraktRowMapper implements RowMapper<Lejekontrakt> {
        @Override
        public Lejekontrakt mapRow(ResultSet rs, int rowNum) throws SQLException {
            Lejekontrakt lejekontrakt = new Lejekontrakt();

            lejekontrakt.setKontraktId(rs.getInt("kontrakt_id"));
            lejekontrakt.setKundeId(rs.getInt("kunde_id"));
            lejekontrakt.setBilId(rs.getInt("bil_id"));
            lejekontrakt.setStartDato(rs.getObject("start_dato", LocalDate.class));
            lejekontrakt.setSlutDato(rs.getObject("slut_dato", LocalDate.class));
            lejekontrakt.setPris(rs.getDouble("pris"));
            lejekontrakt.setMedarbejderId(rs.getInt("medarbejder_id"));

            // RETTET: Håndter enum conversion
            String abonnementTypeStr = rs.getString("abonnement_type");
            if (abonnementTypeStr != null) {
                // Konverter til lowercase for at matche enum
                lejekontrakt.setAbonnementType(
                        Lejekontrakt.AbonnementType.valueOf(abonnementTypeStr.toLowerCase())
                );
            }

            return lejekontrakt;
        }
    }

    public void addLejekontrakt(Lejekontrakt l) {
        System.out.println("=== REPO: Forsøger at indsætte lejekontrakt ===");

        try {
            String sql = "INSERT INTO lejekontrakt(kontrakt_id, kunde_id, bil_id, start_dato, slut_dato, abonnement_type, pris, medarbejder_id) VALUES(?,?,?,?,?,?,?,?)";
            System.out.println("Repo - SQL: " + sql);

            int rowsAffected = template.update(sql,
                    l.getKontraktId(),
                    l.getKundeId(),
                    l.getBilId(),
                    l.getStartDato(),
                    l.getSlutDato(),
                    l.getAbonnementType().name(),
                    l.getPris(),
                    l.getMedarbejderId());

            System.out.println("Repo - Rækker påvirket: " + rowsAffected);
        } catch (Exception e) {
            System.out.println("=== REPO FEJL ===");
            System.out.println("Fejl: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void updateLejekontrakt(Lejekontrakt l) {
        String sql = "UPDATE lejekontrakt SET kunde_id = ?, bil_id = ?, start_dato = ?, slut_dato = ?, abonnement_type = ?, pris = ?, medarbejder_id = ? WHERE kontrakt_id = ?";
        template.update(sql,
                l.getKundeId(),
                l.getBilId(),
                l.getStartDato(),
                l.getSlutDato(),
                l.getAbonnementType().name(),
                l.getPris(),
                l.getMedarbejderId(),
                l.getKontraktId());
    }

    public Lejekontrakt findLejekontraktById(int kontraktId) {
        String sql = "SELECT " +
                "kontrakt_id, " +
                "kunde_id, " +
                "bil_id, " +
                "start_dato, " +
                "slut_dato, " +
                "abonnement_type, " +
                "pris, " +
                "medarbejder_id " +
                "FROM lejekontrakt WHERE kontrakt_id = ?";
        return template.queryForObject(sql, new LejekontraktRowMapper(), kontraktId);
    }

    public void deleteLejekontrakt(int kontraktId) {
        String sql = "DELETE FROM lejekontrakt WHERE kontrakt_id = ?";
        template.update(sql, kontraktId);
    }

    public List<Lejekontrakt> searchLejekontrakter(String soegeord) {
        String sql = "SELECT " +
                "kontrakt_id, " +
                "kunde_id, " +
                "bil_id, " +
                "start_dato, " +
                "slut_dato, " +
                "abonnement_type, " +
                "pris, " +
                "medarbejder_id " +
                "FROM lejekontrakt " +
                "WHERE CAST(kontrakt_id AS CHAR) LIKE ? " +
                "OR abonnement_type LIKE ?";

        return template.query(sql, new LejekontraktRowMapper(),
                "%" + soegeord + "%",
                "%" + soegeord + "%");
    }
}