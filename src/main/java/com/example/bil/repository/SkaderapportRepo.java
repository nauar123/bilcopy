package com.example.bil.repository;

import com.example.bil.model.Skaderapport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SkaderapportRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 1. Hent alle skaderapporter
    public List<Skaderapport> fetchAll() {
        String sql = "SELECT skaderapport_id, tilstandsrapport_id, medarbejder_id, antal_skader, pris_pr_skade, pris_total, beskrivelse FROM skaderapport";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Skaderapport.class));
    }

    // 2. Opretter ny skaderapport
    public int opretSkaderapport(Skaderapport skaderapport) {
        String sql = "INSERT INTO skaderapport (tilstandsrapport_id, medarbejder_id, antal_skader, pris_pr_skade, pris_total, beskrivelse) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                skaderapport.getTilstandsrapportId(),
                skaderapport.getMedarbejderId(),
                skaderapport.getAntalSkader(),
                skaderapport.getPrisPrSkade(),
                skaderapport.getPrisTotal(),
                skaderapport.getBeskrivelse());
    }

    // 3. Opdater eksisterende skaderapport
    public int updateSkaderapport(Skaderapport skaderapport) {
        String sql = "UPDATE skaderapport SET tilstandsrapport_id=?, medarbejder_id=?, antal_skader=?, pris_pr_skade=?, pris_total=?, beskrivelse=? WHERE skaderapport_id=?";
        return jdbcTemplate.update(sql,
                skaderapport.getTilstandsrapportId(),
                skaderapport.getMedarbejderId(),
                skaderapport.getAntalSkader(),
                skaderapport.getPrisPrSkade(),
                skaderapport.getPrisTotal(),
                skaderapport.getBeskrivelse(),
                skaderapport.getSkadeId());
    }

    // 4. Find skaderapport efter id
    public Skaderapport findById(int skaderapportId) {
        String sql = "SELECT skaderapport_id, tilstandsrapport_id, medarbejder_id, antal_skader, pris_pr_skade, pris_total, beskrivelse FROM skaderapport WHERE skaderapport_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Skaderapport.class), skaderapportId);
    }

    // 5. Slet skaderapport efter id
    public int sletEfterId(int skaderapportId) {
        String sql = "DELETE FROM skaderapport WHERE skaderapport_id = ?";
        return jdbcTemplate.update(sql, skaderapportId);
    }
}
