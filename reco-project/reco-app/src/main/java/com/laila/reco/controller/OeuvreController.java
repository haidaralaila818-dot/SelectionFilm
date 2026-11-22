package com.laila.reco.controller;

import com.laila.reco.model.Oeuvre;
import com.laila.reco.repository.OeuvreRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oeuvres")
@CrossOrigin(origins = "*")
public class OeuvreController {

    private final OeuvreRepository oeuvreRepository;

    public OeuvreController(OeuvreRepository oeuvreRepository) {
        this.oeuvreRepository = oeuvreRepository;
    }

    @GetMapping
    public List<Oeuvre> getAll() {
        return oeuvreRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Oeuvre> getById(@PathVariable Long id) {
        return oeuvreRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Oeuvre create(@Valid @RequestBody Oeuvre oeuvre) {
        return oeuvreRepository.save(oeuvre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Oeuvre> update(@PathVariable Long id, @Valid @RequestBody Oeuvre oeuvreDetails) {
        return oeuvreRepository.findById(id)
                .map(oeuvre -> {
                    oeuvre.setTitre(oeuvreDetails.getTitre());
                    oeuvre.setType(oeuvreDetails.getType());
                    oeuvre.setGenre(oeuvreDetails.getGenre());
                    oeuvre.setAnnee(oeuvreDetails.getAnnee());
                    oeuvre.setDescription(oeuvreDetails.getDescription());
                    return ResponseEntity.ok(oeuvreRepository.save(oeuvre));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!oeuvreRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        oeuvreRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
