package com.example.bil.repository;
import com.example.bil.model.Lejekontrakt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

@Repository
public class LejekontraktRepo {
    @Autowired
    JdbcTemplate template;

    // userstory Som medarbejder i dataregistrering vil jeg kunne se et overblik over alle lejekontrakter
    public List<Lejekontrakt> fetchAll() // retunere en liste af lejekontrakter fra databasen
    {
        String sql = "SELECT * FROM lejekontrakt";
        RowMapper<Lejekontrakt> rowMapper = new BeanPropertyRowMapper<>(Lejekontrakt.class);// den vil tage alle værdierne fra databasen og fylde listen op.
        // den retunere dette til homecontrolleren
        return template.query(sql,rowMapper);
    }

    // User story Som medarbejder i dataregistreringen vil jeg kunne oprette en ny bil lejekontrakt, hvor Jeg vil kunne registrere,
    // hvilken bil der skal udleveres til hvilken kunde (lejekontrakt id) og hvornår (start/slut dato) abonnementstype (limited/unlimited).
    public void addLejekontrakt(Lejekontrakt l)
    {
        String sql = "INSERT INTO Lejekontrakt(kontraktId, kundeId, bilId, startDato, slutDato, abonnementType, pris) VALUES(?,?,?,?,?,?,?)";
        template.update(sql, l.getKontraktId(), l.getKundeId(), l.getBilId(), l.getStartDato(), l.getSlutDato(), l.getAbonnementType(), l.getPris());
    }
}