package com.example.bil.repository;
import com.example.bil.model.Bil;
import com.example.bil.model.Kunde;
import com.example.bil.model.Lejekontrakt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.time.LocalDate;
import java.util.List;

@Repository
public class LejekontraktRepo {
    @Autowired
    JdbcTemplate template;
//test

    // Retunere en liste af lejekontrakter fra databasen
    public List<Lejekontrakt> fetchAll()
    {
        String sql = "SELECT " +
                "l.kontraktId, " +
                "l.kundeId, " +
                "l.bilId, " +
                "l.startDato, " +
                "l.slutDato, " +
                "l.abonnementType, " +
                "l.pris " +
                "FROM lejekontrakt l " +
                "JOIN kunde k ON l.kundeId = k.kundeId " +
                "JOIN bil b ON l.bilId = b.bilId";

        return template.query(sql, new BeanPropertyRowMapper<>(Lejekontrakt.class));
    }


    // Oprette en ny lejekontrakt / gemme lejekontrakt
    public void addLejekontrakt(Lejekontrakt l)
    {
        String sql = "INSERT INTO Lejekontrakt(kontraktId, kundeId, bilId, startDato, slutDato, abonnementType, pris) VALUES(?,?,?,?,?,?,?)";
        template.update(sql, l.getKontraktId(), l.getKundeId(), l.getBilId(), l.getStartDato(), l.getSlutDato(), l.getAbonnementType(), l.getPris());
    }


    //  opdatere en eksisterende lejekontrakt
    public void updateLejekontrakt(Lejekontrakt l)
    {
        String sql = "UPDATE Lejekontrakt SET kundeId = ?, bilId = ?, startDato = ?, slutDato = ?, abonnementType = ?, pris = ? WHERE kontraktId = ?";
        template.update(sql,
                l.getKundeId(),
                l.getBilId(),
                l.getStartDato(),
                l.getSlutDato(),
                l.getAbonnementType(),
                l.getPris(),
                l.getKontraktId()); // Her bruger du kontraktId til at finde rækken, men du ændrer det ikke.
    }

    // Find lejekontrakt på kontraktID
    public Lejekontrakt findLejekontraktById(int kontraktId) {
        String sql = "SELECT kontraktId, kundeId, bilId, startDato, slutDato, abonnementType, pris " +
                "FROM lejekontrakt WHERE kontraktId = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<>(Lejekontrakt.class), kontraktId);
    }
    // Slet lejekontrakt på ID
    public void deleteLejekontrakt(int kontraktId) {
        String sql = "DELETE FROM Lejekontrakt WHERE kontraktId = ?";
        template.update(sql, kontraktId);
    }

    //// Søger efter lejekontrakter hvor søgeordet matcher kontrakt ID, kundens navn eller abonnementstype
    public List<Lejekontrakt> searchLejekontrakter(String soegeord) {
        String sql = "SELECT l.kontraktId, l.kundeId, l.bilId, l.startDato, l.slutDato, l.abonnementType, l.pris " +
                "FROM Lejekontrakt l " +
                "JOIN Kunde k ON l.kundeId = k.kundeId " +
                "WHERE CAST(l.kontraktId AS CHAR) LIKE ? " + // Ved at CASTe det til CHAR, kan du søge i ID’er som tekst.
                "OR k.navn LIKE ? " +
                "OR l.abonnementType LIKE ?";

        return template.query(sql,
                new BeanPropertyRowMapper<>(Lejekontrakt.class),
                "%" + soegeord + "%",
                "%" + soegeord + "%",
                "%" + soegeord + "%"
        );
    }

}