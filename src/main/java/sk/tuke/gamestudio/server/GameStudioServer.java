package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.server.service.*;
import sk.tuke.gamestudio.server.webservice.CommentServiceRest;
import sk.tuke.gamestudio.server.webservice.RatingServiceRest;
import sk.tuke.gamestudio.server.webservice.ScoreServiceRest;

@SpringBootApplication
@EntityScan({"sk.tuke.gamestudio.server.entity"})
public class GameStudioServer {
	public static void main(String[] args) {
		SpringApplication.run(GameStudioServer.class, args);
	}

    @Bean
    @Primary
    @Scope("singleton")
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig blockpuzzlePitonakGameConfig() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig();
    }

    @Bean
    @Scope(WebApplicationContext.SCOPE_SESSION)
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Level blockpuzzlePitonakLevel() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Level();
    }

    @Bean
    @Scope(WebApplicationContext.SCOPE_SESSION)
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.webui.WebUI blockpuzzlePitonakWebUI() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.webui.WebUI();
    }

    @Bean
    @Scope(WebApplicationContext.SCOPE_SESSION)
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers.WebManager blockpuzzlePitonakWebManager() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers.WebManager();
    }

    @Bean
    @Scope(WebApplicationContext.SCOPE_SESSION)
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.filedecoder.FileDecoder blockpuzzlePitonakFileDecoder() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.filedecoder.FileDecoder();
    }

    @Bean
    @Scope(WebApplicationContext.SCOPE_SESSION)
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers.PuzzleSpecialist blockpuzzlePitonakPuzzleSpecialist() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers.PuzzleSpecialist();
    }

    @Bean
    @Scope(WebApplicationContext.SCOPE_SESSION)
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory.PentominoFactory blockpuzzlePitonakPentominoFactory() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory.PentominoFactory();
    }

    @Bean
    @Scope(WebApplicationContext.SCOPE_SESSION)
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory.PentominoEngineer blockpuzzlePitonakPentominoEngineer() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory.PentominoEngineer();
    }

    @Bean
    @Scope(WebApplicationContext.SCOPE_SESSION)
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Field blockpuzzlePitonakField() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Field(blockpuzzlePitonakGameConfig().getFieldRows(), blockpuzzlePitonakGameConfig().getFieldColumns());
    }

    @Bean
    @Scope(WebApplicationContext.SCOPE_SESSION)
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.PuzzlesBox blockpuzzlePitonakPuzzlesBox() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.PuzzlesBox();
    }

    @Bean
    @Scope(WebApplicationContext.SCOPE_SESSION)
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.GameSessionRecord blockpuzzlePitonakGameSessionRecord() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.GameSessionRecord();
    }

    @Bean
    @Scope("singleton")
    public UserService userService() {
	    return new UserServiceJPA();
    }

	@Bean
    @Scope("singleton")
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    @Scope("singleton")
    public CommentService commentService() {
        return new CommentServiceJPA();
    }

    @Bean
    @Scope("singleton")
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }

    @Bean
    @Scope("singleton")
    public ScoreServiceRest scoreServiceRest() {
        return new ScoreServiceRest();
    }

    @Bean
    @Scope("singleton")
    public CommentServiceRest commentServiceRest() {
        return new CommentServiceRest();
    }

    @Bean
    @Scope("singleton")
    public RatingServiceRest ratingServiceRest() {
        return new RatingServiceRest();
    }
}
