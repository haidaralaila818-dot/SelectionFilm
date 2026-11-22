import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Utilisateur } from '../models/models';

const API_URL = 'http://localhost:8080/api/utilisateurs';

@Injectable({ providedIn: 'root' })
export class UtilisateurService {
  constructor(private http: HttpClient) {}

  getAll(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(API_URL);
  }

  create(utilisateur: { nom: string; email: string; motDePasse: string }): Observable<Utilisateur> {
    return this.http.post<Utilisateur>(API_URL, utilisateur);
  }
}
