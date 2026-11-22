package com.laila.reco.controller;

import com.laila.reco.model.Note;
import com.laila.reco.model.Oeuvre;
import com.laila.reco.model.Utilisateur;
import com.laila.reco.repository.NoteRepository;
import com.laila.reco.repository.OeuvreRepository;
import com.laila.reco.repository.UtilisateurRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    private final NoteRepository noteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final OeuvreRepository oeuvreRepository;

    public NoteController(NoteRepository noteRepository,
                           UtilisateurRepository utilisateurRepository,
                           OeuvreRepository oeuvreRepository) {
        this.noteRepository = noteRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.oeuvreRepository = oeuvreRepository;
    }

    public static class NoteRequest {
        @NotNull
        public Long utilisateurId;
        @NotNull
        public Long oeuvreId;
        @NotNull
        @Min(1)
        @Max(5)
        public Integer valeur;
    }

    @GetMapping
    public List<Note> getAll() {
        return noteRepository.findAll();
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public List<Note> getByUtilisateur(@PathVariable Long utilisateurId) {
        return noteRepository.findByUtilisateurId(utilisateurId);
    }

    @GetMapping("/oeuvre/{oeuvreId}")
    public List<Note> getByOeuvre(@PathVariable Long oeuvreId) {
        return noteRepository.findByOeuvreId(oeuvreId);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody NoteRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findById(request.utilisateurId)
                .orElse(null);
        Oeuvre oeuvre = oeuvreRepository.findById(request.oeuvreId)
                .orElse(null);

        if (utilisateur == null || oeuvre == null) {
            return ResponseEntity.badRequest().body("Utilisateur ou œuvre introuvable");
        }

        Note note = noteRepository.findByUtilisateurIdAndOeuvreId(request.utilisateurId, request.oeuvreId)
                .orElse(new Note(utilisateur, oeuvre, request.valeur));
        note.setValeur(request.valeur);

        return ResponseEntity.ok(noteRepository.save(note));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!noteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        noteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
