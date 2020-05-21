package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.Cleaner;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.PuzzlesBox;

public class Level implements StateUpdater, Cleaner {
    @Autowired private GameConfig gameConfig;
    @Autowired private Field field;
    @Autowired private PuzzlesBox puzzlesBox;

    private final Logger LOGGER = LoggerFactory.getLogger(Level.class);
    private final String IDENTIFIER = ConsoleUI.PURPLE + "[LEVEL] " + ConsoleUI.RESET;
    private int currentLevelNumber;
    private LevelState state;
    private long startMillis;
    private int totalScore;
    private int totalTime;
    private int totalMoves;

    /**
     * Method which performs insertion of puzzle with {@code puzzleID} into the field.
     *
     * @param puzzleID - unique identification number of puzzle
     * @param puzzleRow - puzzle box y coordinate
     * @param puzzleColumn - puzzle box x coordinate
     * @param fieldRow - field y coordinate
     * @param fieldColumn - field x coordinate
     *
     * @throws Exception if puzzle could not be inserted into the field.
     */
    public void insertPuzzle(int puzzleID, int puzzleRow, int puzzleColumn, int fieldRow, int fieldColumn) throws Exception {
        field.insertPuzzle(puzzlesBox.takePuzzle(puzzleID), puzzleRow, puzzleColumn, fieldRow, fieldColumn);
        totalMoves++;
        updateState();
        LOGGER.info("Move correct");
    }

    /**
     * Method which removes puzzle from {@code field} and makes it available again in {@code puzzlesBox}.
     *
     * @param puzzleID - unique identification number of puzzle
     *
     * @throws Exception if selected puzzle is already inside {@code puzzlesBox}
     */
    public void takePuzzleBack(int puzzleID) throws Exception {
        puzzlesBox.setAvailable(puzzleID);
        field.takePuzzleBack(puzzleID);
        totalMoves++;
        LOGGER.info("Move correct");
    }

    /**
     * Method is called after puzzle insertion to ensure that state of current level stays up-to-date.
     */
    @Override
    public void updateState() {
        if (field.isFullyOccupied()) {
            state = LevelState.SOLVED;
            saveScoreWithPlaytime();
            return;
        }
        state = LevelState.PLAYING;
    }

    /**
     * Method calculates player's end-game totalScore.
     *
     * @return - integer value which contains player's end-game totalScore
     */
    public int getCurrentScore() {
        try {
            if (state.equals(LevelState.CANCELED))
                return 0;
        }
        catch (NullPointerException e) {
            return 0;
        }
        int score = gameConfig.getFieldRows() * gameConfig.getFieldColumns() * 5 - (getPlayingTime() / 2) - 26;
        if (score > 0)
            return score;
        return 0;
    }

    /**
     * Saves current score at the time player successfully ended level.
     */
    private void saveScoreWithPlaytime() {
        if (!state.equals(LevelState.SOLVED))
            return;
        totalScore = getCurrentScore();
        totalTime = getPlayingTime();
        LOGGER.info("Total score and playing time saved");
    }

    /**
     * Getter method which calculates the number of seconds from the point, where game was loaded and player was playing.
     *
     * @return - integer value which contains player's playtime
     */
    public int getPlayingTime() {
        return ((int) (System.currentTimeMillis() - startMillis)) / 1000;
    }

    /**
     * Overrided method returns formatted string object ready for output.
     *
     * @return - string object containing puzzle render
     */
    @Override
    public String toString() {
        return IDENTIFIER + ConsoleUI.PURPLE + "\n  [PUZZLES BOX]\n\n" + ConsoleUI.RESET + puzzlesBox.toString() + IDENTIFIER + ConsoleUI.PURPLE + "\n  [FIELD]\n\n" + ConsoleUI.RESET + field.toString();
    }

    /**
     * This method prepares {@code Level} instance for the upcoming level.
     */
    @Override
    public void clean() {
        field.clean();
        updateState();
        totalMoves = totalScore = totalTime = 0;
        LOGGER.info("Prepared");
    }

    public boolean isSolved() {
        try {
            return state.equals(LevelState.SOLVED);
        }
        catch(NullPointerException e) {
            return false;
        }
    }

    public void setStartMillis(long startMillis) {
        this.startMillis = startMillis;
    }

    public LevelState getState() {
        return state;
    }

    public void setState(LevelState state) {
        this.state = state;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getCurrentLevelNumber() {
        return currentLevelNumber;
    }

    public void setCurrentLevelNumber(int currentLevelNumber) {
        this.currentLevelNumber = currentLevelNumber;
    }

    public Field getField() {
        return field;
    }

    public PuzzlesBox getPuzzlesBox() {
        return puzzlesBox;
    }

    public int getTotalMoves() {
        return totalMoves;
    }
}
