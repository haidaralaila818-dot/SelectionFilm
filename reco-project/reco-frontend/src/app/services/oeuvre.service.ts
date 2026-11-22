import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Oeuvre } from '../models/models';

const API_URL = 'http://localhost:8080/api/oeuvres';

@Injectable({ providedIn: 'root' })
export class OeuvreService {
  constructor(private http: HttpClient) {}

  getAll(): Observable<Oeuvre[]> {
    return this.http.get<Oeuvre[]>(API_URL);
  }

  create(oeuvre: Partial<Oeuvre>): Observable<Oeuvre> {
    return this.http.post<Oeuvre>(API_URL, oeuvre);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API_URL}/${id}`);
  }
}
