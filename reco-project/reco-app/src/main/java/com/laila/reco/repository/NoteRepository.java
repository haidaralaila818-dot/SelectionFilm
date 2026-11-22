package com.laila.reco.repository;

import com.laila.reco.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUtilisateurId(Long utilisateurId);
    List<Note> findByOeuvreId(Long oeuvreId);
    Optional<Note> findByUtilisateurIdAndOeuvreId(Long utilisateurId, Long oeuvreId);
}
