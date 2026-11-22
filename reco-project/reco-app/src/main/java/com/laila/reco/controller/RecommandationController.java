package com.laila.reco.controller;

import com.laila.reco.dto.OeuvreRecommandee;
import com.laila.reco.service.RecommandationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommandations")
@CrossOrigin(origins = "*")
public class RecommandationController {

    private final RecommandationService recommandationService;

    public RecommandationController(RecommandationService recommandationService) {
        this.recommandationService = recommandationService;
    }

    @GetMapping("/{utilisateurId}")
    public List<OeuvreRecommandee> recommander(
            @PathVariable Long utilisateurId,
            @RequestParam(defaultValue = "10") int limite) {
        return recommandationService.recommander(utilisateurId, limite);
    }
}
