package com.example.bil.repository;
import com.example.bil.model.Lejekontrakt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

@Repository
public class LejekontraktRepo {
    @Autowired
    JdbcTemplate template;

    // RETTET: Bruger korrekte kolonnenavne med underscores og aliases
    public List<Lejekontrakt> fetchAll() {
        String sql = "SELECT " +
                "kontrakt_id as kontraktId, " +
                "kunde_id as kundeId, " +
                "bil_id as bilId, " +
                "start_dato as startDato, " +
                "slut_dato as slutDato, " +
                "abonnement_type as abonnementType, " +
                "pris, " +
                "medarbejder_id as medarbejderId " +
                "FROM lejekontrakt";

        return template.query(sql, new BeanPropertyRowMapper<>(Lejekontrakt.class));
    }

    // RETTET: Bruger korrekte kolonnenavne
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
                "kontrakt_id as kontraktId, " +
                "kunde_id as kundeId, " +
                "bil_id as bilId, " +
                "start_dato as startDato, " +
                "slut_dato as slutDato, " +
                "abonnement_type as abonnementType, " +
                "pris, " +
                "medarbejder_id as medarbejderId " +
                "FROM lejekontrakt WHERE kontrakt_id = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<>(Lejekontrakt.class), kontraktId);
    }

    public void deleteLejekontrakt(int kontraktId) {
        String sql = "DELETE FROM lejekontrakt WHERE kontrakt_id = ?";
        template.update(sql, kontraktId);
    }

    public List<Lejekontrakt> searchLejekontrakter(String soegeord) {
        String sql = "SELECT " +
                "kontrakt_id as kontraktId, " +
                "kunde_id as kundeId, " +
                "bil_id as bilId, " +
                "start_dato as startDato, " +
                "slut_dato as slutDato, " +
                "abonnement_type as abonnementType, " +
                "pris, " +
                "medarbejder_id as medarbejderId " +
                "FROM lejekontrakt " +
                "WHERE CAST(kontrakt_id AS CHAR) LIKE ? " +
                "OR abonnement_type LIKE ?";

        return template.query(sql,
                new BeanPropertyRowMapper<>(Lejekontrakt.class),
                "%" + soegeord + "%",
                "%" + soegeord + "%");
    }
}