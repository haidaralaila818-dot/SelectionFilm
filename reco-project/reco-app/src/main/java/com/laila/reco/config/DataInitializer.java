package com.laila.reco.config;

import com.laila.reco.model.Note;
import com.laila.reco.model.Oeuvre;
import com.laila.reco.model.TypeOeuvre;
import com.laila.reco.model.Utilisateur;
import com.laila.reco.repository.NoteRepository;
import com.laila.reco.repository.OeuvreRepository;
import com.laila.reco.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Remplit la base avec des données de démonstration au premier démarrage.
 * Ne fait rien si la base contient déjà des œuvres ou des utilisateurs.
 *
 * Pour repartir de zéro avec Docker : docker compose down -v
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final OeuvreRepository oeuvreRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final NoteRepository noteRepository;

    public DataInitializer(OeuvreRepository oeuvreRepository,
                           UtilisateurRepository utilisateurRepository,
                           NoteRepository noteRepository) {
        this.oeuvreRepository = oeuvreRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.noteRepository = noteRepository;
    }

    @Override
    public void run(String... args) {
        if (oeuvreRepository.count() > 0 || utilisateurRepository.count() > 0) {
            log.info("Base déjà remplie : seed ignoré.");
            return;
        }

        List<Oeuvre> oeuvres = oeuvreRepository.saveAll(List.of(
                new Oeuvre("Inception", TypeOeuvre.FILM, "Science-fiction", 2010,
                        "Un voleur spécialisé dans l'infiltration des rêves doit implanter une idée dans l'esprit d'une cible."),
                new Oeuvre("Interstellar", TypeOeuvre.FILM, "Science-fiction", 2014,
                        "Des astronautes traversent un trou de ver pour chercher une nouvelle planète habitable pour l'humanité."),
                new Oeuvre("The Dark Knight", TypeOeuvre.FILM, "Action", 2008,
                        "Un justicier affronte un criminel imprévisible qui plonge Gotham dans le chaos."),
                new Oeuvre("Parasite", TypeOeuvre.FILM, "Thriller", 2019,
                        "Une famille modeste s'infiltre peu à peu dans la vie d'une famille riche, avec des conséquences inattendues."),
                new Oeuvre("Intouchables", TypeOeuvre.FILM, "Comédie dramatique", 2011,
                        "L'amitié improbable entre un riche tétraplégique et son aide à domicile issu de banlieue."),
                new Oeuvre("Titanic", TypeOeuvre.FILM, "Romance", 1997,
                        "Une histoire d'amour entre deux passagers de classes sociales opposées à bord d'un paquebot condamné."),
                new Oeuvre("Le Fabuleux Destin d'Amélie Poulain", TypeOeuvre.FILM, "Comédie romantique", 2001,
                        "Une jeune serveuse parisienne décide de rendre la vie des autres plus belle, en secret."),
                new Oeuvre("La La Land", TypeOeuvre.FILM, "Comédie musicale", 2016,
                        "Un pianiste de jazz et une actrice en devenir poursuivent leurs rêves à Los Angeles."),
                new Oeuvre("Le Petit Prince", TypeOeuvre.LIVRE, "Conte", 1943,
                        "Un aviateur échoué dans le désert rencontre un petit garçon venu d'une autre planète."),
                new Oeuvre("1984", TypeOeuvre.LIVRE, "Dystopie", 1949,
                        "Dans un régime totalitaire où tout est surveillé, un homme tente de penser librement."),
                new Oeuvre("L'Alchimiste", TypeOeuvre.LIVRE, "Roman initiatique", 1988,
                        "Un jeune berger part à la recherche d'un trésor et découvre sa propre destinée."),
                new Oeuvre("Harry Potter à l'école des sorciers", TypeOeuvre.LIVRE, "Fantasy", 1997,
                        "Un orphelin découvre qu'il est un sorcier et entre à Poudlard."),
                new Oeuvre("Une si longue lettre", TypeOeuvre.LIVRE, "Roman", 1979,
                        "Une veuve sénégalaise écrit à son amie d'enfance pour raconter sa vie et la condition des femmes."),
                new Oeuvre("Les Soleils des indépendances", TypeOeuvre.LIVRE, "Roman", 1968,
                        "Un prince malinké déchu peine à trouver sa place dans une Afrique nouvellement indépendante."),
                new Oeuvre("Le Devoir de violence", TypeOeuvre.LIVRE, "Roman historique", 1968,
                        "Une fresque sombre de l'histoire d'un empire africain imaginaire, du Moyen Âge à la colonisation.")
        ));

        List<Utilisateur> utilisateurs = utilisateurRepository.saveAll(List.of(
                new Utilisateur("Aminata", "aminata@example.com", "demo1234"),
                new Utilisateur("Moussa", "moussa@example.com", "demo1234"),
                new Utilisateur("Fatoumata", "fatoumata@example.com", "demo1234"),
                new Utilisateur("Ibrahim", "ibrahim@example.com", "demo1234"),
                new Utilisateur("Awa", "awa@example.com", "demo1234")
        ));

        // {indexUtilisateur, indexOeuvre, valeur}
        // Deux "familles de goûts" : science-fiction/thriller (Aminata, Moussa) et romance/romans (Fatoumata, Awa),
        // Ibrahim fait le pont entre les deux.
        int[][] notes = {
                // Aminata : peu de notes, pour que l'algorithme ait des choses à lui recommander
                {0, 0, 5}, {0, 1, 5}, {0, 2, 4}, {0, 5, 2}, {0, 9, 5},
                // Moussa
                {1, 0, 5}, {1, 1, 4}, {1, 2, 5}, {1, 3, 5}, {1, 9, 4}, {1, 8, 3}, {1, 11, 4},
                // Fatoumata
                {2, 5, 5}, {2, 6, 5}, {2, 7, 4}, {2, 4, 5}, {2, 12, 5}, {2, 8, 4}, {2, 0, 2},
                // Ibrahim
                {3, 0, 4}, {3, 2, 4}, {3, 3, 4}, {3, 4, 4}, {3, 10, 5}, {3, 13, 4}, {3, 14, 4},
                // Awa
                {4, 6, 4}, {4, 7, 5}, {4, 5, 4}, {4, 12, 5}, {4, 13, 5}, {4, 11, 5}, {4, 10, 4}
        };

        List<Note> toutesLesNotes = new java.util.ArrayList<>();
        for (int[] n : notes) {
            toutesLesNotes.add(new Note(utilisateurs.get(n[0]), oeuvres.get(n[1]), n[2]));
        }
        noteRepository.saveAll(toutesLesNotes);

        log.info("Seed terminé : {} œuvres, {} utilisateurs, {} notes.",
                oeuvres.size(), utilisateurs.size(), toutesLesNotes.size());
    }
}
