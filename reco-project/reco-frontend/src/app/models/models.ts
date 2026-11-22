export interface Oeuvre {
  id: number;
  titre: string;
  type: 'FILM' | 'LIVRE';
  genre: string;
  annee: number;
  description: string;
}

export interface Utilisateur {
  id: number;
  nom: string;
  email: string;
}

export interface NoteRequest {
  utilisateurId: number;
  oeuvreId: number;
  valeur: number;
}

export interface OeuvreRecommandee {
  oeuvre: Oeuvre;
  scorePredit: number;
}
