package sk.tuke.gamestudio.game.blockpuzzle.pitonak.webui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Level;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers.WebManager;

// GAMEPLAY IMPROVEMENTS
// TODO variable field size in Level Builder
// TODO reloadless gameplay
// TODO block animations while moving with them

// SERVICES IMPROVEMENTS
// TODO submit rating and update immediately
// TODO securely save score through URL
// TODO securely set rating through URL
// TODO securely add comment through URL

public class WebUI {
    @Autowired private WebManager webManager;
    @Autowired private Level level;
    @Autowired private GameConfig gameConfig;

    private final Logger LOGGER = LoggerFactory.getLogger(WebUI.class);
    private WebAction action;
    private int puzzleID;
    private int row;
    private int column;
    private String username;
    private int selectedPuzzleRow;
    private int selectedPuzzleColumn;
    private int selectedPuzzleID;

    /**
     * Method takes actions based on user's browser requests
     *
     * @param action - string object containing requested action user wants to take
     * @param puzzleIDString - string object containing puzzle's unique ID
     * @param rowString - string object containing either field's row number or puzzles box's one
     * @param columnString - string object containing either field's column number or puzzles box's one
     * @param username - string object containing user's
     */
    public void processGameAction(String action, String puzzleIDString, String rowString, String columnString, String username) {
        try {
            setValues(action, puzzleIDString, rowString, columnString, username);
            checkActionPermission();
            performAction();
        }
        catch(Exception e) {
            LOGGER.warn("Action " + this.action.name() + " cannot be performed.");
            LOGGER.warn(e.getLocalizedMessage());
        }
    }

    /**
     * Method sets instance variables.
     *
     * @param action - string object containing requested action user wants to take
     * @param puzzleIDString - string object containing puzzle's unique ID
     * @param rowString - string object containing either field's row number or puzzles box's one
     * @param columnString - string object containing either field's column number or puzzles box's one
     * @param username - string object containing user's name
     *
     * @throws UnsupportedOperationException when requested action does not exist.
     */
    private void setValues(String action, String puzzleIDString, String rowString, String columnString, String username) throws UnsupportedOperationException {
        setWebAction(action);
        setUsername(username);
        try {
            parseIntegers(puzzleIDString, rowString, columnString);
        }
        catch(NumberFormatException e) {
            // ignore empty or incorrect parameters
        }
    }

    /**
     * Method converts {@code action} string object to {@code WebAction} object.
     *
     * @param action - string object containing requested action user wants to take
     *
     * @throws UnsupportedOperationException when requested action does not exist.
     */
    private void setWebAction(String action) throws UnsupportedOperationException {
        if (action == null) {
            this.action = WebAction.START_NEW_GAME;
            return;
        }
        switch(action) {
            case "n": this.action = WebAction.PLAY_NEXT_LEVEL; break;
            case "r": this.action = WebAction.CANCEL_GAME; break;
            case "b": this.action = WebAction.START_BUILDER; break;
            case "l": this.action = WebAction.LAUNCH_BUILDER_LEVEL; break;
            case "a": this.action = WebAction.REPLAY_BUILDER_LEVEL; break;
            case "s": this.action = WebAction.SELECT_PUZZLE; break;
            case "p": this.action = WebAction.PLACE_PUZZLE; break;
            case "t": this.action = WebAction.TAKE_PUZZLE; break;
            default: throw new UnsupportedOperationException();
        }
    }

    /**
     * Method sets user's name if any.
     *
     * @param username - string object containing user's name which needs to be verified
     */
    private void setUsername(String username) {
        if (username == null)
            this.username = "";
        else
            this.username = username;
    }

    /**
     * Method parses string object to integer values.
     *
     * @param puzzleIDString - string object containing puzzle's unique ID
     * @param rowString - string object containing either field's row number or puzzles box's one
     * @param columnString - string object containing either field's column number or puzzles box's one
     *
     * @throws NumberFormatException if there was an error parsing any of the strings.
     */
    private void parseIntegers(String puzzleIDString, String rowString, String columnString) throws NumberFormatException {
        if (puzzleIDString == null)
            puzzleID = 0;
        else
            puzzleID = Integer.parseInt(puzzleIDString);
        this.row = Integer.parseInt(rowString);
        this.column = Integer.parseInt(columnString);
    }

    /**
     * Method checks whether it is possible to make requested {@code WebAction} or not.
     *
     * @throws WebActionNotPermittedException - thrown when it is not possible to make {@code action}.
     */
    private void checkActionPermission() throws WebActionNotPermittedException {
        if ((level.isSolved() && (action.equals(WebAction.PLACE_PUZZLE) || action.equals(WebAction.TAKE_PUZZLE) || action.equals(WebAction.SELECT_PUZZLE))) || !level.isSolved() && (action.equals(WebAction.CANCEL_GAME) || action.equals(WebAction.PLAY_NEXT_LEVEL) || action.equals(WebAction.LAUNCH_BUILDER_LEVEL)))
            throw new WebActionNotPermittedException();
    }

    /**
     * Main {@code WebUI} method which performs requested {@code WebAction}.
     *
     * @throws Exception when it was not possible to perform requested {@code WebAction}.
     */
    private void performAction() throws Exception {
        switch (action) {
            case START_NEW_GAME: webManager.loadLevel(action); break;
            case PLAY_NEXT_LEVEL: webManager.loadLevel(action); break;
            case START_BUILDER: webManager.initializeBuilder(); break;
            case LAUNCH_BUILDER_LEVEL: webManager.playBuilderLevel(action); break;
            case REPLAY_BUILDER_LEVEL: webManager.playBuilderLevel(action); break;
            case CANCEL_GAME: webManager.cancelGameSession(username); break;
            case SELECT_PUZZLE: selectPuzzle(); break;
            case PLACE_PUZZLE: placePuzzle(); break;
            case TAKE_PUZZLE: takePuzzle(); break;
            default: /* previous switch prevents this case from happening */ break;
        }
    }

    /**
     * Method processes {@code WebAction.SELECT_PUZZLE} action.
     */
    private void selectPuzzle() {
        if (selectedPuzzleID == puzzleID)
            LOGGER.info("Puzzle re-selected");
        else
            LOGGER.info("Puzzle selected");

        selectedPuzzleID = puzzleID;
        selectedPuzzleRow = row;
        selectedPuzzleColumn = column;
    }

    /**
     * Method processes {@code WebAction.PLACE_PUZZLE} action.
     *
     * @throws Exception when there was an error with puzzle insertion.
     */
    private void placePuzzle() throws Exception {
        if (isPuzzleSelected()) {
            level.insertPuzzle(selectedPuzzleID, selectedPuzzleRow, selectedPuzzleColumn, row, column);
            unselectPuzzle();
            LOGGER.info("Puzzle inserted");
            if (level.isSolved())
                LOGGER.info("Level solved with score " + level.getTotalScore());
        }
        else
            LOGGER.info("Select puzzle first");
    }

    /**
     * Method processes {@code WebAction.TAKE_PUZZLE} action.
     *
     * @throws Exception when it was not possible to take puzzle with {@code puzzleID} back to puzzles box.
     */
    private void takePuzzle() throws Exception {
        if (isPuzzleSelected())
            unselectPuzzle();
        level.takePuzzleBack(puzzleID);
        LOGGER.info("Puzzle taken back");
    }

    /**
     * Method unselects puzzle after it was successfully inserted into the field.
     */
    private void unselectPuzzle() {
        selectedPuzzleID = 0;
        LOGGER.info("Puzzle unselected");
    }

    /**
     * Method takes actions based on user's browser requests in Builder component.
     *
     * @param action - string object containing requested action user wants to take
     * @param puzzleIDString - string object containing puzzle's unique ID
     * @param rowString - string object containing either field's row number or puzzles box's one
     * @param columnString - string object containing either field's column number or puzzles box's one
     */
    public void processBuilderAction(String action, String puzzleIDString, String rowString, String columnString) {
        try {
            setValues(action, puzzleIDString, rowString, columnString);
            if (this.action.equals(WebAction.LAUNCH_BUILDER_LEVEL))
                checkActionPermission();
            performAction();
        }
        catch(Exception e) {
            LOGGER.warn("Action " + this.action.name() + " cannot be performed.");
            LOGGER.warn(e.getLocalizedMessage());
        }
    }

    /**
     * Method sets instance variables.
     *
     * @param action - string object containing requested action user wants to take
     * @param puzzleIDString - string object containing puzzle's unique ID
     * @param rowString - string object containing either field's row number or puzzles box's one
     * @param columnString - string object containing either field's column number or puzzles box's one
     *
     * @throws UnsupportedOperationException when requested action does not exist.
     */
    private void setValues(String action, String puzzleIDString, String rowString, String columnString) throws UnsupportedOperationException {
        if (action == null)
            setWebAction("b");
        else
            setWebAction(action);
        try {
            parseIntegers(puzzleIDString, rowString, columnString);
        }
        catch(NumberFormatException e) {
            // ignore empty or incorrect parameters
        }
    }

    public boolean isLevelBuilderEnabled() {
        return gameConfig.isLevelBuilderEnabled();
    }

    public boolean isBuilderLevelReady() {
        return webManager.playableBuilderLevel();
    }

    public Level getLevel() {
        return level;
    }

    public WebManager getWebManager() {
        return webManager;
    }

    public boolean isPuzzleSelected() {
        return !(selectedPuzzleID == 0);
    }

    public int getMaxLevelNumber() {
        return gameConfig.getAvailableLevelsNumber();
    }
}
