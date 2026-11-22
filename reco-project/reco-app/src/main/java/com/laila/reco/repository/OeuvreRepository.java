package com.laila.reco.repository;

import com.laila.reco.model.Oeuvre;
import com.laila.reco.model.TypeOeuvre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OeuvreRepository extends JpaRepository<Oeuvre, Long> {
    List<Oeuvre> findByType(TypeOeuvre type);
    List<Oeuvre> findByGenreIgnoreCase(String genre);
}
