package com.laila.reco.service;

import com.laila.reco.dto.OeuvreRecommandee;
import com.laila.reco.model.Note;
import com.laila.reco.model.Oeuvre;
import com.laila.reco.repository.NoteRepository;
import com.laila.reco.repository.OeuvreRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommandationService {

    private final NoteRepository noteRepository;
    private final OeuvreRepository oeuvreRepository;

    public RecommandationService(NoteRepository noteRepository, OeuvreRepository oeuvreRepository) {
        this.noteRepository = noteRepository;
        this.oeuvreRepository = oeuvreRepository;
    }

    /**
     * Filtrage collaboratif user-based :
     * 1. Construit la matrice utilisateur -> (oeuvre -> note)
     * 2. Calcule la similarité cosinus entre l'utilisateur cible et tous les autres
     * 3. Prédit un score pour chaque oeuvre non notée par l'utilisateur cible,
     *    pondéré par la similarité des autres utilisateurs qui l'ont notée
     */
    public List<OeuvreRecommandee> recommander(Long utilisateurId, int limite) {
        List<Note> toutesLesNotes = noteRepository.findAll();

        // Construction de la matrice utilisateur -> (oeuvreId -> valeur)
        Map<Long, Map<Long, Double>> notesParUtilisateur = new HashMap<>();
        for (Note note : toutesLesNotes) {
            notesParUtilisateur
                    .computeIfAbsent(note.getUtilisateur().getId(), k -> new HashMap<>())
                    .put(note.getOeuvre().getId(), note.getValeur().doubleValue());
        }

        Map<Long, Double> notesCible = notesParUtilisateur.getOrDefault(utilisateurId, Collections.emptyMap());
        Set<Long> oeuvresDejaNotees = notesCible.keySet();

        if (notesCible.isEmpty()) {
            // Pas assez de données pour cet utilisateur : on renvoie les mieux notées globalement
            return recommanderParPopularite(oeuvresDejaNotees, limite);
        }

        // Calcul de la similarité cosinus avec chaque autre utilisateur
        Map<Long, Double> similarites = new HashMap<>();
        for (Map.Entry<Long, Map<Long, Double>> entry : notesParUtilisateur.entrySet()) {
            Long autreUtilisateurId = entry.getKey();
            if (autreUtilisateurId.equals(utilisateurId)) continue;

            double similarite = similariteCosinus(notesCible, entry.getValue());
            if (similarite > 0) {
                similarites.put(autreUtilisateurId, similarite);
            }
        }

        // Prédiction pondérée des scores pour les oeuvres non notées
        Map<Long, Double> scoresPredits = new HashMap<>();
        Map<Long, Double> sommesPoids = new HashMap<>();

        for (Map.Entry<Long, Double> simEntry : similarites.entrySet()) {
            Long autreUtilisateurId = simEntry.getKey();
            double similarite = simEntry.getValue();
            Map<Long, Double> notesAutre = notesParUtilisateur.get(autreUtilisateurId);

            for (Map.Entry<Long, Double> noteEntry : notesAutre.entrySet()) {
                Long oeuvreId = noteEntry.getKey();
                if (oeuvresDejaNotees.contains(oeuvreId)) continue;

                double note = noteEntry.getValue();
                scoresPredits.merge(oeuvreId, similarite * note, Double::sum);
                sommesPoids.merge(oeuvreId, similarite, Double::sum);
            }
        }

        List<Map.Entry<Long, Double>> classement = scoresPredits.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), e.getValue() / sommesPoids.get(e.getKey())))
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(limite)
                .collect(Collectors.toList());

        if (classement.isEmpty()) {
            return recommanderParPopularite(oeuvresDejaNotees, limite);
        }

        List<OeuvreRecommandee> resultat = new ArrayList<>();
        for (Map.Entry<Long, Double> entry : classement) {
            oeuvreRepository.findById(entry.getKey())
                    .ifPresent(oeuvre -> resultat.add(new OeuvreRecommandee(oeuvre, entry.getValue())));
        }
        return resultat;
    }

    /** Similarité cosinus entre deux vecteurs de notes (seulement sur les oeuvres communes) */
    private double similariteCosinus(Map<Long, Double> notesA, Map<Long, Double> notesB) {
        Set<Long> communes = new HashSet<>(notesA.keySet());
        communes.retainAll(notesB.keySet());

        if (communes.isEmpty()) return 0.0;

        double produitScalaire = 0.0, normeA = 0.0, normeB = 0.0;
        for (Long oeuvreId : communes) {
            double a = notesA.get(oeuvreId);
            double b = notesB.get(oeuvreId);
            produitScalaire += a * b;
            normeA += a * a;
            normeB += b * b;
        }

        if (normeA == 0 || normeB == 0) return 0.0;
        return produitScalaire / (Math.sqrt(normeA) * Math.sqrt(normeB));
    }

    /** Solution de repli : les oeuvres les mieux notées en moyenne, hors celles déjà notées */
    private List<OeuvreRecommandee> recommanderParPopularite(Set<Long> exclues, int limite) {
        List<Note> toutesLesNotes = noteRepository.findAll();
        Map<Long, List<Double>> notesParOeuvre = new HashMap<>();
        for (Note note : toutesLesNotes) {
            if (exclues.contains(note.getOeuvre().getId())) continue;
            notesParOeuvre
                    .computeIfAbsent(note.getOeuvre().getId(), k -> new ArrayList<>())
                    .add(note.getValeur().doubleValue());
        }

        return notesParOeuvre.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), e.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0)))
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(limite)
                .map(e -> oeuvreRepository.findById(e.getKey())
                        .map(oeuvre -> new OeuvreRecommandee(oeuvre, e.getValue()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
