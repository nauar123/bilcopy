package com.example.bil.repository;
import com.example.bil.model.Forretningsudvikler;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ForretningsudviklerRepo extends JpaRepository<Forretningsudvikler, Integer>
{
    // finder biler med status "ledig"
    List<Forretningsudvikler> findByStatus(String status);
}