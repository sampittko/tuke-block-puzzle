package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles;

import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Tile;

import java.util.Formatter;

public class Puzzle {
    private final int MAX_VALUE;
    private Tile[][] tileField;
    private static int puzzleID = 0;
    private int ID;
    private PuzzleState state;

    /**
     * Constructor creates puzzle tile field and fills it with empty tiles. It also sets unique ID to every new puzzle piece.
     *
     * @param MAX_VALUE - this constant integer depends on the puzzle type (Pentomino - 5)
     */
    public Puzzle(final int MAX_VALUE) {
        this.MAX_VALUE = MAX_VALUE;
        tileField = new Tile[MAX_VALUE][MAX_VALUE];
        ID = ++puzzleID;
        fillTileFieldWithTiles();
    }

    /**
     * Method fills null {@code tileField} with empty tiles and is called immediately inside construction of puzzle object.
     */
    private void fillTileFieldWithTiles() {
        for (int y = 0; y < MAX_VALUE; y++) {
            for (int x = 0; x < MAX_VALUE; x++){
                tileField[y][x] = new Tile();
                tileField[y][x].setPuzzleID(ID);
            }
        }
    }

    /**
     * Setter method which is called from factory by an Engineer while puzzle is being produced.
     *
     * @param tileField - reference to the puzzle tile field 2D array
     */
    public void updateTileField(Tile[][] tileField) {
        this.tileField = tileField;
    }

    /**
     * Overrided method returns formatted string object ready for output.
     *
     * @return {@code f.toString()} - string object containing puzzle render
     */
    @Override
    public String toString() {
        Formatter f = new Formatter();
        f.format("%4s", "");
        // header
        for (int column = 0; column < MAX_VALUE; column++)
            f.format("%2s", column + 1);
        f.format("%n");
        // body
        for (int row = 0; row < MAX_VALUE; row++) {
            f.format("%4s", (char)(row + 'A'));
            for (int column = 0; column < MAX_VALUE; column++) {
                final Tile tile = getTile(row, column);
                f.format("%2s", tile);
            }
            f.format("%n");
        }
        return f.toString();
    }

    /**
     * Method which is called when the game is in preparation stage.
     */
    public static void resetPuzzleID() {
        Puzzle.puzzleID = 0;
    }

    private Tile getTile(int row, int column) {
        return tileField[row][column];
    }

    public Tile[][] getTileField() {
        return tileField;
    }

    void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public int getMaxValue() {
        return MAX_VALUE;
    }

    public PuzzleState getState() {
        return state;
    }

    public void setState(PuzzleState puzzleState) {
        this.state = puzzleState;
    }
}