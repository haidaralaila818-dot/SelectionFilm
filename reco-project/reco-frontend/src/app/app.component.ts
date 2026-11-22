import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Utilisateur } from './models/models';
import { UtilisateurService } from './services/utilisateur.service';
import { OeuvreListComponent } from './components/oeuvre-list/oeuvre-list.component';
import { RecommandationsComponent } from './components/recommandations/recommandations.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, OeuvreListComponent, RecommandationsComponent],
  template: `
    <div class="container">
      <h1>🎬 📚 Reco Films &amp; Livres</h1>

      <div class="card">
        <label>
          Utilisateur :
          <select [(ngModel)]="utilisateurIdCourant" (ngModelChange)="onUtilisateurChange()">
            <option *ngFor="let u of utilisateurs" [ngValue]="u.id">{{ u.nom }}</option>
          </select>
        </label>
        <button (click)="afficherFormulaire = !afficherFormulaire">
          {{ afficherFormulaire ? 'Annuler' : 'Nouvel utilisateur' }}
        </button>

        <form *ngIf="afficherFormulaire" (ngSubmit)="creerUtilisateur()" style="margin-top:12px">
          <input name="nom" placeholder="Nom" [(ngModel)]="nouveau.nom" required>
          <input name="email" type="email" placeholder="Email" [(ngModel)]="nouveau.email" required>
          <input name="mdp" type="password" placeholder="Mot de passe" [(ngModel)]="nouveau.motDePasse" required>
          <button type="submit">Créer</button>
          <p *ngIf="erreur" style="color:#b91c1c">{{ erreur }}</p>
        </form>
      </div>

      <ng-container *ngIf="utilisateurIdCourant !== null">
        <app-recommandations
          [utilisateurId]="utilisateurIdCourant"
          [rafraichir]="compteurRafraichissement"></app-recommandations>
        <h2>Catalogue</h2>
        <app-oeuvre-list
          [utilisateurId]="utilisateurIdCourant"
          (noteEnregistree)="compteurRafraichissement = compteurRafraichissement + 1"></app-oeuvre-list>
      </ng-container>

      <p *ngIf="utilisateurIdCourant === null">Crée un premier utilisateur pour commencer.</p>
    </div>
  `
})
export class AppComponent implements OnInit {
  utilisateurs: Utilisateur[] = [];
  utilisateurIdCourant: number | null = null;
  compteurRafraichissement = 0;

  afficherFormulaire = false;
  nouveau = { nom: '', email: '', motDePasse: '' };
  erreur = '';

  constructor(private utilisateurService: UtilisateurService) {}

  ngOnInit(): void {
    this.chargerUtilisateurs();
  }

  chargerUtilisateurs(selectionnerId?: number): void {
    this.utilisateurService.getAll().subscribe(data => {
      this.utilisateurs = data;
      if (selectionnerId !== undefined) {
        this.utilisateurIdCourant = selectionnerId;
      } else if (this.utilisateurIdCourant === null && data.length > 0) {
        this.utilisateurIdCourant = data[0].id;
      }
    });
  }

  onUtilisateurChange(): void {
    this.compteurRafraichissement++;
  }

  creerUtilisateur(): void {
    this.erreur = '';
    this.utilisateurService.create(this.nouveau).subscribe({
      next: cree => {
        this.nouveau = { nom: '', email: '', motDePasse: '' };
        this.afficherFormulaire = false;
        this.chargerUtilisateurs(cree.id);
      },
      error: () => (this.erreur = "Impossible de créer l'utilisateur (email déjà utilisé ou champ invalide).")
    });
  }
}
