package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.webui.WebUI;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BlockpuzzlePitonakLevelBuilderController {
    @Autowired WebUI webUI;

    @RequestMapping("/blockpuzzle-pitonak/level-builder")
    public String blockpuzzlePitonakLevelBuilder(@RequestParam(value = "a", required = false) String action,
                                                 @RequestParam(value = "i", required = false) String puzzleIDString,
                                                 @RequestParam(value = "r", required = false) String rowString,
                                                 @RequestParam(value = "c", required = false) String columnString, Model model) {
        webUI.processBuilderAction(action, puzzleIDString, rowString, columnString);
        model.addAttribute("webUI", webUI);
        return "/blockpuzzle-pitonak_levelBuilder";
    }
}