package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Level;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.webui.WebUI;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BlockpuzzlePitonakController {
    @Autowired private WebUI webUI;
    @Autowired private Level level;

    @RequestMapping("/blockpuzzle-pitonak/play")
    public String blockpuzzlePitonak(@RequestParam(value = "a", required = false) String action,
                                     @RequestParam(value = "i", required = false) String puzzleIDString,
                                     @RequestParam(value = "r", required = false) String rowString,
                                     @RequestParam(value = "c", required = false) String columnString,
                                     @RequestParam(value = "p", required = false) String username, Model model) {
        webUI.processGameAction(action, puzzleIDString, rowString, columnString, username);
        model.addAttribute("webUI", webUI);
        return "/blockpuzzle-pitonak";
    }

    @RequestMapping("/blockpuzzle-pitonak")
    public String blockpuzzlePitonakIndex(Model model) {
        model.addAttribute("webUI", webUI);
        return "/blockpuzzle-pitonak";
    }

    @RequestMapping("/blockpuzzle-pitonak/score")
    @ResponseBody
    public int blockpuzzlePitonakGetScore() {
        if (level.isSolved())
            return level.getTotalScore();
        return level.getCurrentScore();
    }

    @RequestMapping("/blockpuzzle-pitonak/seconds")
    @ResponseBody
    public int blockpuzzlePitonakGetSeconds() {
        if (level.isSolved())
            return level.getTotalTime();
        return level.getPlayingTime();
    }

    @RequestMapping("/blockpuzzle-pitonak/state")
    @ResponseBody
    public int blockpuzzlePitonakGetState() {
        if (level.isSolved())
            return 1;
        return 0;
    }
}