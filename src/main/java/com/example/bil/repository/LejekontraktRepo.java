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

    // RETTET: SQL fejl fixet
    public List<Lejekontrakt> fetchAll() {
        String sql = "SELECT " +
                "l.kontraktId, " +
                "l.kundeId, " +
                "l.bilId, " +
                "l.startDato, " +
                "l.slutDato, " +
                "l.abonnementType, " +
                "l.pris, " +
                "l.medarbejderId " +
                "FROM lejekontrakt l";

        return template.query(sql, new BeanPropertyRowMapper<>(Lejekontrakt.class));
    }

    // RETTET: Korrekt antal parametre i SQL
    public void addLejekontrakt(Lejekontrakt l) {
        String sql = "INSERT INTO lejekontrakt(kontraktId, kundeId, bilId, startDato, slutDato, abonnementType, pris, medarbejderId) VALUES(?,?,?,?,?,?,?,?)";
        template.update(sql,
                l.getKontraktId(),
                l.getKundeId(),
                l.getBilId(),
                l.getStartDato(),
                l.getSlutDato(),
                l.getAbonnementType().name(),
                l.getPris(),
                l.getMedarbejderId());
    }

    public void updateLejekontrakt(Lejekontrakt l) {
        String sql = "UPDATE lejekontrakt SET kundeId = ?, bilId = ?, startDato = ?, slutDato = ?, abonnementType = ?, pris = ?, medarbejderId = ? WHERE kontraktId = ?";
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
        String sql = "SELECT kontraktId, kundeId, bilId, startDato, slutDato, abonnementType, pris, medarbejderId " +
                "FROM lejekontrakt WHERE kontraktId = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<>(Lejekontrakt.class), kontraktId);
    }

    public void deleteLejekontrakt(int kontraktId) {
        String sql = "DELETE FROM lejekontrakt WHERE kontraktId = ?";
        template.update(sql, kontraktId);
    }

    public List<Lejekontrakt> searchLejekontrakter(String soegeord) {
        String sql = "SELECT l.kontraktId, l.kundeId, l.bilId, l.startDato, l.slutDato, l.abonnementType, l.pris, l.medarbejderId " +
                "FROM lejekontrakt l " +
                "WHERE CAST(l.kontraktId AS CHAR) LIKE ? " +
                "OR l.abonnementType LIKE ?";

        return template.query(sql,
                new BeanPropertyRowMapper<>(Lejekontrakt.class),
                "%" + soegeord + "%",
                "%" + soegeord + "%");
    }
}