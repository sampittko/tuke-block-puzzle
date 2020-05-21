package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig;
import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.service.RatingException;
import sk.tuke.gamestudio.server.service.RatingService;
import sk.tuke.gamestudio.server.service.UserService;

import java.util.Date;


@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BlockpuzzlePitonakRatingController {
    @Autowired private RatingService ratingService;
    @Autowired private UserService userService;
    @Autowired private GameConfig gameConfig;

    @RequestMapping("/blockpuzzle-pitonak/rating")
    public String blockpuzzlePitonakRating(@RequestParam(name = "r", required = false) String rating,
                                           @RequestParam(name = "p", required = false) String username, Model model) throws RatingException {
        try {
            if (!rating.equals("") && !username.equals("") && userService.checkRegistration(username) != null) {
                int parsedRating = Integer.parseInt(rating);
                ratingService.setRating(new Rating(username, gameConfig.getGameName(), parsedRating, new Date()));
            }
        }
        catch (Exception e) {

        }
        model.addAttribute("rating", ratingService.getAverageRating(gameConfig.getGameName()));
        return "/blockpuzzle-pitonak_rating";
    }
}