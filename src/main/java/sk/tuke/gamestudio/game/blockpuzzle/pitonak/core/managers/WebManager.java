package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.Cleaner;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.GameSessionRecord;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Level;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.Puzzle;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.PuzzlesBox;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.filedecoder.FileDecoder;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.webui.WebAction;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.UserService;
import sk.tuke.gamestudio.server.service.ScoreService;

import java.util.ArrayList;
import java.util.Date;

public class WebManager implements WebGameManagement, Cleaner {
    @Autowired private GameConfig gameConfig;
    @Autowired private Level level;
    @Autowired private FileDecoder fileDecoder;
    @Autowired private PuzzleSpecialist puzzleSpecialist;
    @Autowired private PuzzlesBox puzzlesBox;
    @Autowired private GameSessionRecord gameSessionRecord;
    @Autowired private ScoreService scoreService;
    @Autowired private UserService userService;

    private final Logger LOGGER = LoggerFactory.getLogger(WebManager.class);
    private ArrayList<Puzzle> puzzlesList;
    private WebAction action;
    private boolean playableBuilderLevel;

    /**
     * Method loads level based on {@code WebAction} variable.
     *
     * @param action - {@code WebAction} to be taken
     *
     * @throws Exception when there was a problem while loading upcoming level
     */
    @Override
    public void loadLevel(WebAction action) throws Exception {
        executeStageOne(action);
        executeStageTwo();
        executeStageThree();
        level.setStartMillis(System.currentTimeMillis());
        LOGGER.info("Level " + level.getCurrentLevelNumber() + " loaded");
    }

    /**
     * This method executes preparation stage so that all instances are reset and ready for the upcoming level.
     *
     * @param action - {@code WebAction} to be taken
     *
     * @throws LevelNotAvailableException when there is not another level left to play in the game, yet.
     */
    private void executeStageOne(WebAction action) throws LevelNotAvailableException {
        LOGGER.info("Preparation stage has just started");
        this.action = action;
        checkLevelAvailability();
        clean();
    }

    /**
     * Method checks whether it is possible to load another level or not.
     *
     * @throws LevelNotAvailableException when there is not another level left to play in the game, yet.
     */
    private void checkLevelAvailability() throws LevelNotAvailableException {
        if (action.equals(WebAction.START_NEW_GAME))
            level.setCurrentLevelNumber(gameConfig.getStartingLevelNumber());
        else if (action.equals(WebAction.PLAY_NEXT_LEVEL)) {
            gameSessionRecord.addScore(level.getTotalScore());
            if (level.getCurrentLevelNumber() + 1 > gameConfig.getAvailableLevelsNumber()) {
                LOGGER.warn("Level is not available");
                throw new LevelNotAvailableException();
            }
            level.setCurrentLevelNumber(level.getCurrentLevelNumber() + 1);
        }
        else if (action.equals(WebAction.START_BUILDER)) {
            if (!gameConfig.isLevelBuilderEnabled())
                throw new LevelNotAvailableException();
            else
                level.setCurrentLevelNumber(0);
        }
        LOGGER.info("Level available");
    }

    /**
     * This method prepares {@code WebManager} instance for the upcoming level.
     */
    @Override
    public void clean() {
        LOGGER.info("Cleaning up before upcoming level");
        if (playableBuilderLevel)
            playableBuilderLevel = false;
        puzzleSpecialist.clean();
        level.clean();
        if (this.action.equals(WebAction.START_NEW_GAME))
            gameSessionRecord.clean();
        puzzlesList = new ArrayList<>();
        Puzzle.resetPuzzleID();
        LOGGER.info("Everything prepared");
    }

    /**
     * This method executes pre-production stage where level data are loaded and decoded.
     *
     * @throws Exception when there was a problem while {@code fileDecoder} was trying to parse data or {@code puzzleSpecialist} was not able to create certain factory.
     */
    @SuppressWarnings("Duplicates")
    private void executeStageTwo() throws Exception {
        LOGGER.info("Pre-production stage has just started");
        LOGGER.info("Loading level data");
        String[] decodedLevelData = fileDecoder.parseLevelData(level.getCurrentLevelNumber());
        LOGGER.info("Production lines in factories are being filled at the moment");
        puzzleSpecialist.manageFactories(decodedLevelData);
    }

    /**
     * This method executes production stage where decoded level data are turned into real {@code Puzzle} pieces.
     *
     * @throws Exception when there was a problem to build certain puzzle piece.
     */
    @SuppressWarnings("Duplicates")
    private void executeStageThree() throws Exception {
        LOGGER.info("Production stage has just started");
        LOGGER.info("Puzzles are being built in the factories at the moment");
        puzzleSpecialist.beginProduction();
        puzzlesList = puzzleSpecialist.getPuzzles();
        puzzlesBox.setPuzzlesList(puzzlesList);
        LOGGER.info("All puzzles were successfully built");
    }


    /**
     * This method cancels the whole game session and reset level to the first one.
     *
     * @param username - string object containing user's name
     *
     * @throws Exception if user is not registered in system or {@code WebManager} was not able to load first level.
     */
    @Override
    public void cancelGameSession(String username) throws Exception {
        LOGGER.info("Cancelling game");
        checkIdentity(username);
        collectGameSessionData(username);
        executeStageFour();
        LOGGER.info("Starting new game");
        loadLevel(WebAction.START_NEW_GAME);
    }

    /**
     * Method verifies the user.
     *
     * @param username - string object containing user's name
     *
     * @throws UserNotFoundException when {@code username} was not found in system.
     */
    private void checkIdentity(String username) throws UserNotFoundException {
        if (userService.checkRegistration(username) == null)
            throw new UserNotFoundException();
        LOGGER.info("User registered");
    }

    /**
     * Method adds recorded game data to {@code GameSessionRecord} instance.
     *
     * @param username - string object containing user's name
     */
    private void collectGameSessionData(String username) {
        gameSessionRecord.setPlayer(username);
        gameSessionRecord.addScore(level.getTotalScore());
        gameSessionRecord.setPlayedOn(new Date());
        LOGGER.info("Game session data collected");
    }

    /**
     * This method executes data update stage where player's score is inserted into the database.
     *
     * @throws ScoreException when score could not be added to database.
     */
    private void executeStageFour() throws ScoreException {
        LOGGER.info("Data update stage has just started");
        scoreService.addScore(new Score(gameConfig.getGameName(), gameSessionRecord.getPlayer(), gameSessionRecord.getTotalScore(), gameSessionRecord.getPlayedOn()));
        LOGGER.info("Score was successfully added to database");
    }

    /**
     * Method initializes level builder which means that it loads level containing all puzzle pieces.
     *
     * @throws Exception if there was a problem in initialization process of Level Builder.
     */
    public void initializeBuilder() throws Exception {
        executeStageOne(WebAction.START_BUILDER);
        executeStageTwo();
        executeStageThree();
        LOGGER.info("Builder initialized");
    }

    /**
     * Method is called when the player presses PLAY button after creating custom level.
     *
     * @param action - action to be taken in Builder mode
     */
    public void playBuilderLevel(WebAction action) {
        if (action.equals(WebAction.LAUNCH_BUILDER_LEVEL)) {
            LOGGER.info("Preparing instances for custom level");
            puzzlesBox.savePuzzlesUsed();
            playableBuilderLevel = !playableBuilderLevel;
        }
        else if (action.equals(WebAction.REPLAY_BUILDER_LEVEL)) {
            LOGGER.info("Preparing instances for re-launch");
            puzzlesBox.makeAllAvailable();
        }
        level.clean();
        level.setStartMillis(System.currentTimeMillis());
        LOGGER.info("Everything prepared");
    }

    public boolean playableBuilderLevel() {
        return playableBuilderLevel;
    }
}