package com.example.bil.repository;

import com.example.bil.model.Bil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BilRepo {

    @Autowired
    JdbcTemplate template;

    // Find alle biler
    public List<Bil> findAll() {
        String sql = "SELECT * FROM bil";
        RowMapper<Bil> rowMapper = new BeanPropertyRowMapper<>(Bil.class);
        return template.query(sql, rowMapper);
    }

    // Find bil efter id
    public Bil findById(int bilId) {
        String sql = "SELECT * FROM bil WHERE bilId = ?";
        RowMapper<Bil> rowMapper = new BeanPropertyRowMapper<>(Bil.class);
        return template.queryForObject(sql, rowMapper, bilId);
    }

    // Tilføj en ny bil
    public void addBil(Bil bil) {
        String sql = "INSERT INTO bil (bilId, stelNr, maerke, model, udstyrsniveau, status, regAfgift, co2Udledning) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        template.update(sql, bil.getBilId(), bil.getStelNr(), bil.getMaerke(), bil.getModel(), bil.getUdstyrsniveau(),
                bil.getStatus().name(), bil.getRegAfgift(), bil.getCo2Udledning());
    }

    // Opdater en eksisterende bil
    public void updateBil(Bil bil) {
        String sql = "UPDATE bil SET stelNr = ?, maerke = ?, model = ?, udstyrsniveau = ?, status = ?, regAfgift = ?, co2Udledning = ? " +
                "WHERE bilId = ?";
        template.update(sql, bil.getStelNr(), bil.getMaerke(), bil.getModel(), bil.getUdstyrsniveau(),
                bil.getStatus().name(), bil.getRegAfgift(), bil.getCo2Udledning(), bil.getBilId());
    }

    // Slet en bil efter id
    public void deleteById(int bilId) {
        String sql = "DELETE FROM bil WHERE bilId = ?";
        template.update(sql, bilId);
    }
}

