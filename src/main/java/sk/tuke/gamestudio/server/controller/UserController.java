package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig;
import sk.tuke.gamestudio.server.entity.User;
import sk.tuke.gamestudio.server.service.RatingService;
import sk.tuke.gamestudio.server.service.UserService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    @Autowired private UserService userService;
    @Autowired private RatingService ratingService;
    @Autowired private GameConfig gameConfig;

    private User loggedUser;

    @RequestMapping("/login")
    public String login(User user, Model model) {
        try {
            if (isInputCorrect(user)) {
                user = userService.login(user.getUsername(), user.getPasswd());
                if (user == null)
                    throw new NullPointerException();
                loggedUser = user;
            }
        }
        catch (NullPointerException e) {
        }
        return "login";
    }

    @RequestMapping("/register")
    public String register(User user, Model model) {
        if (isInputCorrect(user)) {
            if (userService.login(user.getUsername(), user.getPasswd()) == null)
                userService.register(user.getUsername(), user.getPasswd());
        }
        return "login";
    }

    @RequestMapping("/logout")
    public String logout() {
        loggedUser = null;
        return "login";
    }

    public int getRating() {
        try {
            return ratingService.getRating(gameConfig.getGameName(), loggedUser.getUsername());
        }
        catch(Exception e) {
            return 0;
        }
    }

    private boolean isInputCorrect(User user) {
        return !user.getUsername().equals("") && !user.getPasswd().equals("");
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }
}
