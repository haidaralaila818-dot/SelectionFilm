package com.laila.reco.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "oeuvres")
public class Oeuvre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titre;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TypeOeuvre type;

    private String genre;

    private Integer annee;

    @Column(length = 1000)
    private String description;

    // Relation inverse ignorée en JSON pour éviter les boucles infinies
    @JsonIgnore
    @OneToMany(mappedBy = "oeuvre", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Note> notes = new HashSet<>();

    public Oeuvre() {
    }

    public Oeuvre(String titre, TypeOeuvre type, String genre, Integer annee, String description) {
        this.titre = titre;
        this.type = type;
        this.genre = genre;
        this.annee = annee;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public TypeOeuvre getType() {
        return type;
    }

    public void setType(TypeOeuvre type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }
}
