package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig;
import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.service.*;

import java.util.Date;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BlockpuzzlePitonakCommentsController {
    @Autowired private CommentService commentService;
    @Autowired private UserService userService;
    @Autowired private GameConfig gameConfig;

    @RequestMapping("/blockpuzzle-pitonak/comments")
    public String blockpuzzlePitonakComments(@RequestParam(name = "c", required = false) String comment,
                                             @RequestParam(name = "p", required = false) String username, Model model) throws CommentException {
        try {
            if (!comment.equals("") && !username.equals("") && userService.checkRegistration(username) != null)
                commentService.addComment(new Comment(username, gameConfig.getGameName(), comment, new Date()));
        }
        catch (NullPointerException e) {

        }
        model.addAttribute("comments", commentService.getComments(gameConfig.getGameName()));
        return "/blockpuzzle-pitonak_comments";
    }
}