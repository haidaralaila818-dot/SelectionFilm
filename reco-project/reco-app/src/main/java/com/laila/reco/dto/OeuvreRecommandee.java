package com.laila.reco.dto;

import com.laila.reco.model.Oeuvre;

public class OeuvreRecommandee {
    private Oeuvre oeuvre;
    private double scorePredit;

    public OeuvreRecommandee(Oeuvre oeuvre, double scorePredit) {
        this.oeuvre = oeuvre;
        this.scorePredit = scorePredit;
    }

    public Oeuvre getOeuvre() {
        return oeuvre;
    }

    public void setOeuvre(Oeuvre oeuvre) {
        this.oeuvre = oeuvre;
    }

    public double getScorePredit() {
        return scorePredit;
    }

    public void setScorePredit(double scorePredit) {
        this.scorePredit = scorePredit;
    }
}
