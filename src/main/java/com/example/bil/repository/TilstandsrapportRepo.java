package com.example.bil.repository;

import com.example.bil.model.Tilstandsrapport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TilstandsrapportRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Tilstandsrapport> fetchAll() {
        String sql = "SELECT tilstandsrapport_id AS tilstandsrapportId, bil_id AS bilId, kontrakt_id AS kontraktId, tilstandsrapport_dato AS tilstandsrapportDato, medarbejder_id AS medarbejderId, er_skadet AS erSkadet FROM tilstandsrapport";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tilstandsrapport.class));
    }

    public int saveTilstandsrapport(Tilstandsrapport tilstandsrapport) {
        String sql = "INSERT INTO tilstandsrapport (bil_id, kontrakt_id, tilstandsrapport_dato, medarbejder_id, er_skadet) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                tilstandsrapport.getBilId(),
                tilstandsrapport.getKontraktId(),
                new java.sql.Date(tilstandsrapport.getTilstandsrapportDato().getTime()),
                tilstandsrapport.getMedarbejderId(),
                tilstandsrapport.isErSkadet());
    }

    public int updateTilstandsrapport(Tilstandsrapport tilstandsrapport) {
        String sql = "UPDATE tilstandsrapport SET bil_id=?, kontrakt_id=?, tilstandsrapport_dato=?, medarbejder_id=?, er_skadet=? WHERE tilstandsrapport_id=?";
        return jdbcTemplate.update(sql,
                tilstandsrapport.getBilId(),
                tilstandsrapport.getKontraktId(),
                new java.sql.Date(tilstandsrapport.getTilstandsrapportDato().getTime()),
                tilstandsrapport.getMedarbejderId(),
                tilstandsrapport.isErSkadet(),
                tilstandsrapport.getTilstandsrapportId());
    }

    public Tilstandsrapport findById(int tilstandsrapportId) {
        String sql = "SELECT tilstandsrapport_id AS tilstandsrapportId, bil_id AS bilId, kontrakt_id AS kontraktId, tilstandsrapport_dato AS tilstandsrapportDato, medarbejder_id AS medarbejderId, er_skadet AS erSkadet FROM tilstandsrapport WHERE tilstandsrapport_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Tilstandsrapport.class), tilstandsrapportId);
    }

    public int deleteById(int tilstandsrapportId) {
        String sql = "DELETE FROM tilstandsrapport WHERE tilstandsrapport_id = ?";
        return jdbcTemplate.update(sql, tilstandsrapportId);
    }

}
