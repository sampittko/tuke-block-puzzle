package sk.tuke.gamestudio.game.blockpuzzle.pitonak;

import org.springframework.boot.context.properties.ConfigurationProperties;

// GENERAL GAMEPLAY IMPROVEMENTS
// TODO support for more puzzle pieces (2, 3, 4, 6, 7)
// TODO variable levels difficulty (changeable field size)

/**
 * POJO for game configuration data loading from application.properties file and retrieve during gameplay.
 */
@ConfigurationProperties("game-config")
public class GameConfig {
    private String gameName;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    private String levelsFolderPath;

    public String getLevelsFolderPath() {
        return levelsFolderPath;
    }

    public void setLevelsFolderPath(String levelsFolderPath) {
        this.levelsFolderPath = levelsFolderPath;
    }

    private String levelsSuffix;

    public String getLevelsSuffix() {
        return levelsSuffix;
    }

    public void setLevelsSuffix(String levelsSuffix) {
        this.levelsSuffix = levelsSuffix;
    }

    private String puzzlesFolderPath;

    public String getPuzzlesFolderPath() {
        return puzzlesFolderPath;
    }

    public void setPuzzlesFolderPath(String puzzlesFolderPath) {
        this.puzzlesFolderPath = puzzlesFolderPath;
    }

    private String puzzlesSuffix;

    public String getPuzzlesSuffix() {
        return puzzlesSuffix;
    }

    public void setPuzzlesSuffix(String puzzlesSuffix) {
        this.puzzlesSuffix = puzzlesSuffix;
    }

    private int availableLevelsNumber;

    public int getAvailableLevelsNumber() {
        return availableLevelsNumber;
    }

    public void setAvailableLevelsNumber(int availableLevelsNumber) {
        this.availableLevelsNumber = availableLevelsNumber;
    }

    private int startingLevelNumber;

    public int getStartingLevelNumber() {
        return startingLevelNumber;
    }

    public void setStartingLevelNumber(int startingLevelNumber) {
        this.startingLevelNumber = startingLevelNumber;
    }

    private int fieldRows;

    public int getFieldRows() {
        return fieldRows;
    }

    public void setFieldRows(int fieldRows) {
        this.fieldRows = fieldRows;
    }

    private int fieldColumns;

    public int getFieldColumns() {
        return fieldColumns;
    }

    public void setFieldColumns(int fieldColumns) {
        this.fieldColumns = fieldColumns;
    }

    private boolean levelBuilderEnabled;

    public boolean isLevelBuilderEnabled() {
        return levelBuilderEnabled;
    }

    public void setLevelBuilderEnabled(boolean levelBuilderEnabled) {
        this.levelBuilderEnabled = levelBuilderEnabled;
    }
}
