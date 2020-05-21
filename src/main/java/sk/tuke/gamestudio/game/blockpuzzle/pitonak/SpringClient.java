package sk.tuke.gamestudio.game.blockpuzzle.pitonak;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import sk.tuke.gamestudio.server.service.*;

@Configuration
@SpringBootApplication
@EnableConfigurationProperties(GameConfig.class)
public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(false).run(args);
    }

    @Bean
    public CommandLineRunner runner() {
        return (args) -> blockpuzzlePitonakManager().manageGame();
    }

    @Bean
    @Primary
    @Scope("singleton")
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig blockpuzzlePitonakGameConfig() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig();
    }

    @Bean
    @Scope("singleton")
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers.ConsoleManager blockpuzzlePitonakManager() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers.ConsoleManager();
    }

    @Bean
    @Scope("singleton")
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.consoleui.ConsoleUI blockpuzzlePitonakConsoleUI() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.consoleui.ConsoleUI();
    }

    @Bean
    @Scope("singleton")
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.filedecoder.FileDecoder blockpuzzlePitonakFileDecoder() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.filedecoder.FileDecoder();
    }

    @Bean
    @Scope("singleton")
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers.PuzzleSpecialist blockpuzzlePitonakPuzzleSpecialist() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers.PuzzleSpecialist();
    }

    @Bean
    @Scope("singleton")
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory.PentominoFactory blockpuzzlePitonakPentominoFactory() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory.PentominoFactory();
    }

    @Bean
    @Scope("singleton")
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory.PentominoEngineer blockpuzzlePitonakPentominoEngineer() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory.PentominoEngineer();
    }

    @Bean
    @Scope("singleton")
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Level blockpuzzlePitonakLevel() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Level();
    }

    @Bean
    @Scope("singleton")
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Field blockpuzzlePitonakField() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Field(blockpuzzlePitonakGameConfig().getFieldRows(), blockpuzzlePitonakGameConfig().getFieldColumns());
    }

    @Bean
    @Scope("singleton")
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.PuzzlesBox blockpuzzlePitonakPuzzlesBox() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.PuzzlesBox();
    }

    @Bean
    @Scope("singleton")
    public sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.GameSessionRecord blockpuzzlePitonakGameSessionRecord() {
        return new sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.GameSessionRecord();
    }

    @Bean
    @Scope("singleton")
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }

    @Bean
    @Scope("singleton")
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }

    @Bean
    @Scope("singleton")
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }
}
