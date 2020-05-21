package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.Cleaner;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.Puzzle;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.PuzzleState;

import java.util.Formatter;

public class Field implements StateUpdater, Cleaner {
    private final Logger LOGGER = LoggerFactory.getLogger(Field.class);
    private Tile[][] tileField;
    private final int rowCount;
    private final int columnCount;
    private FieldState state;

    /**
     * Constructor initializes needed resources such as {@code tileField} and {@code state}.
     *
     * @param rowCount - the number of tiles on y axis
     * @param columnCount - the number of tiles on x axis
     */
    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        tileField = new Tile[this.rowCount][this.columnCount];
        fillTileFieldWithTiles();
        state = FieldState.EMPTY;
    }

    /**
     * Method fills null {@code tileField} with empty tiles and is called immediately inside construction of puzzle object.
     */
    private void fillTileFieldWithTiles() {
        for (int y = 0; y < rowCount; y++) {
            for (int x = 0; x < columnCount; x++){
                tileField[y][x] = new Tile();
            }
        }
    }

    /**
     * Method uses other auxiliary methods to insert {@code Puzzle} into the field, if evaluated as possible.
     *
     * @param puzzleToInsert - {@code Puzzle} piece which needs to be inserted into the {@code Field}
     * @param PUZZLE_ROW - y coordinate of puzzle
     * @param PUZZLE_COLUMN - x coordinate of puzzle
     * @param FIELD_ROW - y coordinate of the field
     * @param FIELD_COLUMN - x coordinate of the field
     *
     * @throws UnableToInsertPuzzleException if there was an error while evaluating whether the puzzle can or cannot be inserted into the {@code Field}.
     */
    public void insertPuzzle(Puzzle puzzleToInsert, final int PUZZLE_ROW, final int PUZZLE_COLUMN, final int FIELD_ROW, final int FIELD_COLUMN) throws UnableToInsertPuzzleException {
        insertEvaluatedData(puzzleToInsert, evaluateInsertion(puzzleToInsert, FIELD_ROW - PUZZLE_ROW, FIELD_COLUMN - PUZZLE_COLUMN));
        puzzleToInsert.setState(PuzzleState.PLACED);
        updateState();
    }

    /**
     * Method evaluates whether puzzle can or cannot be inserted into the field.
     *
     * @param puzzleToInsert - {@code Puzzle} piece which needs to be inserted into the {@code Field}
     * @param ROWS_DIFF - difference between field's and puzzle's y coordinates
     * @param COLUMNS_DIFF - difference between field's and puzzle's x coordinates
     *
     * @return {@code insertionMatrix} - matrix where temporary insertion data are going to be stored
     *
     * @throws UnableToInsertPuzzleException when puzzle cannot be placed into the field due to the fact that there is already puzzle or {@code puzzleToInsert} is outside of the field.
     */
    private int[][] evaluateInsertion(Puzzle puzzleToInsert, final int ROWS_DIFF, final int COLUMNS_DIFF) throws UnableToInsertPuzzleException {
        int[][] insertionMatrix = new int[rowCount][columnCount];
        Tile currentPuzzleTile, currentFieldTile;
        // evaluation algorithm
        for (int y = 0; y < puzzleToInsert.getMaxValue(); y++) {
            for (int x = 0; x < puzzleToInsert.getMaxValue(); x++) {
                currentPuzzleTile = puzzleToInsert.getTileField()[y][x];
                // validation inside the field
                try {
                    currentFieldTile = tileField[y + ROWS_DIFF][x + COLUMNS_DIFF];
                    // FULL & FULL = CANNOT INSERT PUZZLE
                    if (currentFieldTile.getState().equals(TileState.FULL) && currentPuzzleTile.getState().equals(TileState.FULL))
                        throw new UnableToInsertPuzzleException();
                    // EMPTY & FULL = INSERT PUZZLE
                    else if (currentFieldTile.getState().equals(TileState.EMPTY) && currentPuzzleTile.getState().equals(TileState.FULL))
                        insertionMatrix[y + ROWS_DIFF][x + COLUMNS_DIFF] = 1;
                }
                // validation outside of the field
                catch (ArrayIndexOutOfBoundsException e) {
                    // puzzle is outside of the field = CANNOT INSERT PUZZLE
                    if (currentPuzzleTile.getState().equals(TileState.FULL))
                        throw new UnableToInsertPuzzleException();
                }
            }
        }
        return insertionMatrix;
    }

    /**
     * Method inserts puzzle into the field.
     *
     * @param puzzleToInsert - {@code Puzzle} piece which needs to be inserted into the {@code Field}
     * @param insertionMatrix - matrix containing coordinates which tell where the puzzle needs to be inserted ([y,x][y,x]..)
     */
    private void insertEvaluatedData(Puzzle puzzleToInsert, int[][] insertionMatrix) {
        for (int y = 0; y < rowCount; y++) {
            for (int x = 0; x < columnCount; x++){
                if (insertionMatrix[y][x] == 1) {
                    tileField[y][x].setState(TileState.FULL);
                    tileField[y][x].setPuzzleID(puzzleToInsert.getID());
                }
            }
        }
    }

    /**
     * Method removes requested puzzle from the field.
     *
     * @param puzzleID - puzzle's unique identification number
     */
    public void takePuzzleBack(int puzzleID) {
        for (Tile[] tilesArray : tileField) {
            for (Tile tile : tilesArray) {
                if (tile.getPuzzleID() == puzzleID) {
                    tile.setPuzzleID(0);
                    tile.setState(TileState.EMPTY);
                }
            }
        }
        updateState();
    }

    /**
     * Method updates state of the field.
     */
    @Override
    public void updateState() {
        int emptyTiles = rowCount * columnCount;
        for (Tile[] tiles : tileField) {
            for (Tile tile : tiles) {
                if(tile.getState().equals(TileState.FULL))
                    emptyTiles--;
            }
        }
        if (emptyTiles == 0)
            state = FieldState.FULLY_OCCUPIED;
        else if (emptyTiles == rowCount * columnCount)
            state = FieldState.EMPTY;
        else
            state = FieldState.NOT_FULLY_OCCUPIED;
    }

    /**
     * Method checks wheter {@code FieldState} equals to {@code FieldState.FULLY_OCCUPIED}.
     *
     * @return - true or false
     */
    public boolean isFullyOccupied() {
        return state.equals(FieldState.FULLY_OCCUPIED);
    }

    /**
     * Overrided method returns formatted string object ready for output.
     *
     * @return - string object containing puzzle render
     */
    @Override
    public String toString() {
        Formatter f = new Formatter();
        f.format("%4s", "");
        for (int column = 0; column < columnCount; column++)
            f.format("%3s", column + 1);
        f.format("%n");
        for (int row = 0; row < rowCount; row++) {
            f.format("%4s", (char)(row + 'A'));
            for (int column = 0; column < columnCount; column++) {
                final Tile tile = getTile(row, column);
                if (tile.toString().equals("-"))
                    f.format("%3s", tile);
                else
                    f.format("%s", " " + tile);
            }
            f.format("%n");
        }
        return f.toString();
    }

    private Tile getTile(int row, int column) {
        return tileField[row][column];
    }

    /**
     * This method prepares {@code Field} instance for the upcoming level.
     */
    @Override
    public void clean() {
        tileField = new Tile[this.rowCount][this.columnCount];
        fillTileFieldWithTiles();
        state = FieldState.EMPTY;
        LOGGER.info("Prepared");
    }

    public Tile[][] getTileField() {
        return tileField;
    }
}
