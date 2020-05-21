package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels;

import sk.tuke.gamestudio.game.blockpuzzle.pitonak.consoleui.ConsoleUI;

public class Tile {
    private int puzzleID;
    private TileState state = TileState.EMPTY;

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public void setPuzzleID(int puzzleID) {
        this.puzzleID = puzzleID;
    }

    public int getPuzzleID() {
        return puzzleID;
    }

    /**
     * Overrided method returns formatted string object ready for output.
     *
     * @return - string object containing puzzle render
     */
    @Override
    public String toString() {
        return state == TileState.FULL ? getColored() : "-";
    }

    /**
     * Method sets color for tile character on request.
     *
     * @return {@code colored} - a string object containing colored tile character
     */
    private String getColored() {
        String colored = " ";
        switch (puzzleID) {
            case 1: colored += ConsoleUI.GREEN + puzzleID + ConsoleUI.RESET; break;
            case 2: colored += ConsoleUI.BLUE + puzzleID + ConsoleUI.RESET; break;
            case 3: colored += ConsoleUI.RED + puzzleID + ConsoleUI.RESET; break;
            case 4: colored += ConsoleUI.PURPLE + puzzleID + ConsoleUI.RESET; break;
            case 5: colored += ConsoleUI.YELLOW + puzzleID + ConsoleUI.RESET; break;
            default: colored += Integer.toString(puzzleID);
        }
        return colored;
    }
}
