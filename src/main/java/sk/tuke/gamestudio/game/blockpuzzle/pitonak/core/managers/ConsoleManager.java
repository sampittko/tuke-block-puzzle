package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.Cleaner;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Field;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.GameSessionRecord;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Level;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.LevelState;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.Puzzle;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.PuzzlesBox;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.filedecoder.FileDecoder;
import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.*;

import java.util.ArrayList;

public class ConsoleManager implements ConsoleGameManagement, Cleaner {
    @Autowired private GameConfig gameConfig;
    @Autowired private FileDecoder fileDecoder;
    @Autowired private PuzzleSpecialist puzzleSpecialist;
    @Autowired private ConsoleUI consoleUI;
    @Autowired private Level level;
    @Autowired private Field field;
    @Autowired private PuzzlesBox puzzlesBox;
    @Autowired private GameSessionRecord gameSessionRecord;
    @Autowired private ScoreService scoreService;
    @Autowired private CommentService commentService;
    @Autowired private RatingService ratingService;

    private final Logger LOGGER = LoggerFactory.getLogger(ConsoleManager.class);
    private ArrayList<Puzzle> puzzlesList;
    private int currentLevelNumber = -1;

    /**
     * This is the main {@code ConsoleManager} method called by {@code ConsoleUI}. This method takes control of the whole game including:
     * 1. PREPARATION STAGE
     * 2. PRE-PRODUCTION STAGE
     * 3. PRODUCTION STAGE
     * 4. PLAYING STAGE
     * 5. DATA UPDATE STAGE
     */
    public void manageGame() {
        do {
            try {
                executeStageOne();
                executeStageTwo();
                executeStageThree();
                executeStageFour();
            }
            catch (Exception e) {
                if (e instanceof LevelNotAvailableException) {
                    LOGGER.error(e.getMessage());
                    unavailableLevelHandler();
                }
                else {
                    LOGGER.error(e.getMessage());
                    return;
                }
            }
        }
        while (continueGame());
        if (!gameSessionRecord.hasNoScores()) {
            try {
                executeStageFive();
            }
            catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        LOGGER.info("Quitting game");
    }

    /**
     * This method executes preparation stage so that all instances are reset and ready for the upcoming level.
     *
     * @throws LevelNotAvailableException when there is not another level left to play in the game, yet.
     */
    private void executeStageOne() throws LevelNotAvailableException {
        LOGGER.info("Preparation stage has just started");
        if (currentLevelNumber == -1)
            currentLevelNumber = gameConfig.getStartingLevelNumber();
        checkLevelAvailability();
        clean();
    }

    /**
     * Method checks whether it is possible to load another level or not.
     *
     * @throws LevelNotAvailableException when there is not another level left to play in the game, yet.
     */
    private void checkLevelAvailability() throws LevelNotAvailableException {
        if (currentLevelNumber > gameConfig.getAvailableLevelsNumber())
            throw new LevelNotAvailableException();
        level.setCurrentLevelNumber(currentLevelNumber);
        LOGGER.info("Level available");
    }

    /**
     * This method prepares {@code ConsoleManager} instance for the upcoming level.
     */
    @Override
    public void clean() {
        LOGGER.info("Cleaning up before upcoming level");
        puzzleSpecialist.clean();
        field.clean();
        puzzlesList = new ArrayList<>();
        Puzzle.resetPuzzleID();
        LOGGER.info("Everything prepared");
    }

    /**
     * This method executes pre-production stage where level data are loaded and decoded.
     *
     * @throws Exception when there was a problem while {@code fileDecoder} was trying to parse data or {@code puzzleSpecialist} was not able to create certain factory.
     */
    private void executeStageTwo() throws Exception {
        LOGGER.info("Pre-production stage has just started");
        LOGGER.info("Loading level data");
        String[] decodedLevelData = fileDecoder.parseLevelData(currentLevelNumber);
        LOGGER.info("Production lines in factories are being filled at the moment");
        puzzleSpecialist.manageFactories(decodedLevelData);
    }

    /**
     * This method executes production stage where decoded level data are turned into real {@code Puzzle} pieces.
     *
     * @throws Exception when there was a problem to build certain puzzle piece.
     */
    private void executeStageThree() throws Exception {
        LOGGER.info("Production stage has just started");
        LOGGER.info("Puzzles are being built in the factories at the moment");
        puzzleSpecialist.beginProduction();
        puzzlesList = puzzleSpecialist.getPuzzles();
        puzzlesBox.setPuzzlesList(puzzlesList);
        LOGGER.info("All puzzles were successfully built");
    }

    /**
     * This method starts the game through ({@code ui}) which returns reference to the {@code GameSessionRecord} object containing needed information for {@code ConsoleManager}. It also increments {@code currentLevelNumber} number so that player can play the next level.
     */
    private void executeStageFour() {
        LOGGER.info("Playing stage has just started");
        LOGGER.info("Starting console user interface");
        consoleUI.run();
    }

    /**
     * Method analyzes how gameRecord ended the last level and according to that information, value is returned of which {@code ConsoleManager} makes further steps.
     *
     * @return - true or false, depending on the {@code GameSessionRecord} object data
     */
    private boolean continueGame() {
        // player canceled the game during the first level
        if (gameSessionRecord.hasNoScores()) {
            LOGGER.info("No score to insert into database");
            return false;
        }
        // player solved the game and did not want to continue playing at the end or he canceled the game in the higher level than the first one
        else if (gameSessionRecord.getEndGameState().equals(LevelState.SOLVED) && !gameSessionRecord.getPlayer().equals("") || gameSessionRecord.getEndGameState().equals(LevelState.CANCELED)) {
            LOGGER.info("Data will be inserted into database");
            return false;
        }
        // player continues playing the game after successful end of the level while his nickname remains null
        else if (gameSessionRecord.getEndGameState().equals(LevelState.SOLVED) && gameSessionRecord.getPlayer().equals("")) {
            LOGGER.info("Current's level score saved");
            level.setCurrentLevelNumber(++currentLevelNumber);
            return true;
        }
        // something wrong happened
        LOGGER.error("Cannot continue game for unknown reason");
        return false;
    }

    /**
     * This method executes data update stage where player's score, possible comment and rating are inserted into the database.
     *
     * @throws Exception when data could not be added to database.
     */
    private void executeStageFive() throws Exception {
        LOGGER.info("Data update stage has just started");
        // score insertion
        scoreService.addScore(new Score(gameConfig.getGameName(), gameSessionRecord.getPlayer(), gameSessionRecord.getTotalScore(), gameSessionRecord.getPlayedOn()));
        LOGGER.info("Score added to database");
        // comment insertion
        if (!gameSessionRecord.getComment().equals("")) {
            commentService.addComment(new Comment(gameSessionRecord.getPlayer(), gameConfig.getGameName(), gameSessionRecord.getComment(), gameSessionRecord.getCommentedOn()));
            LOGGER.info("Comment added to database");
        }
        // rating insertion
        if (gameSessionRecord.getRating() != 0) {
            ratingService.setRating(new Rating(gameSessionRecord.getPlayer(), gameConfig.getGameName(), gameSessionRecord.getRating(), gameSessionRecord.getRatedOn()));
            LOGGER.info("Rating added to database");
        }
        LOGGER.info("Data successfully inserted");
    }

    /**
     * Method is called in the case when the player wants to play next level but it is not available.
     */
    private void unavailableLevelHandler() {
        level.setState(LevelState.CANCELED);
        consoleUI.updateGameSessionRecord();
    }
}