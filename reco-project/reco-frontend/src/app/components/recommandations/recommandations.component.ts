import { Component, Input, OnChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OeuvreRecommandee } from '../../models/models';
import { RecommandationService } from '../../services/recommandation.service';

@Component({
  selector: 'app-recommandations',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Recommandé pour toi</h2>
    <div class="card" *ngFor="let item of recommandations">
      <h3>{{ item.oeuvre.titre }}</h3>
      <p>{{ item.oeuvre.type }} — {{ item.oeuvre.genre }}</p>
      <p>Score prédit : {{ item.scorePredit | number: '1.1-2' }} / 5</p>
    </div>
    <p *ngIf="recommandations.length === 0">Pas encore assez de données pour te recommander quelque chose.</p>
  `
})
export class RecommandationsComponent implements OnChanges {
  @Input() utilisateurId!: number;
  // Incrémenté par le parent à chaque nouvelle note ou changement d'utilisateur : force le rechargement
  @Input() rafraichir = 0;
  recommandations: OeuvreRecommandee[] = [];

  constructor(private recommandationService: RecommandationService) {}

  ngOnChanges(): void {
    this.recommandationService
      .getRecommandations(this.utilisateurId)
      .subscribe(data => (this.recommandations = data));
  }
}
