import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OeuvreRecommandee } from '../models/models';

const API_URL = 'http://localhost:8080/api/recommandations';

@Injectable({ providedIn: 'root' })
export class RecommandationService {
  constructor(private http: HttpClient) {}

  getRecommandations(utilisateurId: number, limite = 10): Observable<OeuvreRecommandee[]> {
    return this.http.get<OeuvreRecommandee[]>(`${API_URL}/${utilisateurId}?limite=${limite}`);
  }
}
