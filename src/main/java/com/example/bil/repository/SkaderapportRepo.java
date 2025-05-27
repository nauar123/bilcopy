package com.example.bil.repository;

import com.example.bil.model.Skaderapport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SkaderapportRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Custom RowMapper for at håndtere field mapping
    private static class SkaderapportRowMapper implements RowMapper<Skaderapport> {
        @Override
        public Skaderapport mapRow(ResultSet rs, int rowNum) throws SQLException {
            Skaderapport skaderapport = new Skaderapport();
            skaderapport.setSkadeId(rs.getInt("skaderapport_id"));
            skaderapport.setTilstandsrapportId(rs.getInt("tilstandsrapport_id"));
            skaderapport.setMedarbejderId(rs.getInt("medarbejder_id"));
            skaderapport.setAntalSkader(rs.getInt("antal_skader"));
            skaderapport.setPrisPrSkade(rs.getInt("pris_pr_skade"));
            skaderapport.setPrisTotal(rs.getInt("pris_total"));
            skaderapport.setBeskrivelse(rs.getString("beskrivelse"));
            return skaderapport;
        }
    }

    // 1. Hent alle skaderapporter
    public List<Skaderapport> fetchAll() {
        String sql = "SELECT skaderapport_id, tilstandsrapport_id, medarbejder_id, antal_skader, pris_pr_skade, pris_total, beskrivelse FROM skaderapport";
        return jdbcTemplate.query(sql, new SkaderapportRowMapper());
    }

    // 2. Opretter ny skaderapport
    public int opretSkaderapport(Skaderapport skaderapport) {
        String sql = "INSERT INTO skaderapport (tilstandsrapport_id, medarbejder_id, antal_skader, pris_pr_skade, pris_total, beskrivelse) VALUES (?, ?, ?, ?, ?, ?)";
        System.out.println("=== OPRETTER SKADERAPPORT ===");
        System.out.println("TilstandsrapportId: " + skaderapport.getTilstandsrapportId());
        System.out.println("MedarbejderId: " + skaderapport.getMedarbejderId());
        System.out.println("AntalSkader: " + skaderapport.getAntalSkader());
        System.out.println("PrisPrSkade: " + skaderapport.getPrisPrSkade());
        System.out.println("PrisTotal: " + skaderapport.getPrisTotal());
        System.out.println("Beskrivelse: " + skaderapport.getBeskrivelse());

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
        return jdbcTemplate.queryForObject(sql, new SkaderapportRowMapper(), skaderapportId);
    }

    // 5. Slet skaderapport efter id
    public int sletEfterId(int skaderapportId) {
        String sql = "DELETE FROM skaderapport WHERE skaderapport_id = ?";
        System.out.println("Sletter skaderapport med ID: " + skaderapportId);
        return jdbcTemplate.update(sql, skaderapportId);
    }
}