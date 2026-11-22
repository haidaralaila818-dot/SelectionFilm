import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Oeuvre } from '../../models/models';
import { OeuvreService } from '../../services/oeuvre.service';
import { NoteService } from '../../services/note.service';

@Component({
  selector: 'app-oeuvre-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card" *ngFor="let oeuvre of oeuvres">
      <h3>{{ oeuvre.titre }} <small>({{ oeuvre.annee }})</small></h3>
      <p>{{ oeuvre.type }} — {{ oeuvre.genre }}</p>
      <p>{{ oeuvre.description }}</p>
      <select [(ngModel)]="notesChoisies[oeuvre.id]">
        <option [ngValue]="null" disabled>Noter...</option>
        <option *ngFor="let v of [1,2,3,4,5]" [ngValue]="v">{{ v }} ★</option>
      </select>
      <button (click)="noter(oeuvre.id)">Valider la note</button>
      <span *ngIf="confirmation === oeuvre.id" style="color:#15803d"> ✓ Note enregistrée</span>
    </div>
    <p *ngIf="oeuvres.length === 0">Le catalogue est vide.</p>
  `
})
export class OeuvreListComponent implements OnInit, OnChanges {
  @Input() utilisateurId!: number;
  @Output() noteEnregistree = new EventEmitter<void>();

  oeuvres: Oeuvre[] = [];
  notesChoisies: Record<number, number | null> = {};
  confirmation: number | null = null;

  constructor(private oeuvreService: OeuvreService, private noteService: NoteService) {}

  ngOnInit(): void {
    this.oeuvreService.getAll().subscribe(data => (this.oeuvres = data));
  }

  ngOnChanges(changes: SimpleChanges): void {
    // On repart de zéro quand on change d'utilisateur : les sélections de l'ancien ne le concernent pas
    if (changes['utilisateurId'] && !changes['utilisateurId'].firstChange) {
      this.notesChoisies = {};
      this.confirmation = null;
    }
  }

  noter(oeuvreId: number): void {
    const valeur = this.notesChoisies[oeuvreId];
    if (!valeur) return;

    this.noteService
      .noter({ utilisateurId: this.utilisateurId, oeuvreId, valeur })
      .subscribe(() => {
        this.confirmation = oeuvreId;
        this.noteEnregistree.emit();
      });
  }
}
