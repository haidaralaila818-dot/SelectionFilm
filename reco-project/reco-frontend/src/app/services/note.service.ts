import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NoteRequest } from '../models/models';

const API_URL = 'http://localhost:8080/api/notes';

@Injectable({ providedIn: 'root' })
export class NoteService {
  constructor(private http: HttpClient) {}

  noter(note: NoteRequest): Observable<any> {
    return this.http.post(API_URL, note);
  }
}
