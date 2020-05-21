package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig;
import sk.tuke.gamestudio.server.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BlockpuzzlePitonakTop10Controller {
    @Autowired private ScoreService scoreService;
    @Autowired private GameConfig gameConfig;

    @RequestMapping("/blockpuzzle-pitonak/top-10")
    public String blockpuzzlePitonakTop10(Model model) {
        model.addAttribute("scores", scoreService.getBestScores(gameConfig.getGameName()));
        return "/blockpuzzle-pitonak_top10";
    }
}