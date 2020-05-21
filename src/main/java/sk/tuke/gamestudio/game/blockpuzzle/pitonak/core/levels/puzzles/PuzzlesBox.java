package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PuzzlesBox {
    private static Logger LOGGER = LoggerFactory.getLogger(PuzzlesBox.class);

    private ArrayList<Puzzle> puzzlesList;

    /**
     * Method takes the puzzle from {@code PuzzlesBox}.
     *
     * @param puzzleID - unique puzzle's identification number
     *
     * @return {@code puzzleToTake} - this is the concrete puzzle piece which is requested to be placed into the {@code Field}
     *
     * @throws PuzzleAlreadyInFieldException when, as the name has already said, requested puzzle is already inside the {@code Field}.
     */
    public Puzzle takePuzzle(int puzzleID) throws PuzzleAlreadyInFieldException {
        Puzzle puzzleToTake = null;
        for (Puzzle puzzle : puzzlesList) {
            if (puzzle.getID() == puzzleID)
                puzzleToTake = puzzle;
        }
        if (puzzleToTake.getState().equals(PuzzleState.PLACED))
            throw new PuzzleAlreadyInFieldException();
        return puzzleToTake;
    }

    /**
     * Method makes removed puzzle piece from the {@code Field} available in the {@code PuzzlesBox} again.
     *
     * @param puzzleID - unique puzzle's identification number
     *
     * @throws PuzzleAlreadyInPuzzlesBoxException when, as the name has already said, requested puzzle is already in the {@code PuzzlesBox}.
     */
    public void setAvailable(int puzzleID) throws PuzzleAlreadyInPuzzlesBoxException {
        Puzzle puzzleToSetAvailable = null;
        for (Puzzle puzzle : puzzlesList) {
            if (puzzle.getID() == puzzleID)
                puzzleToSetAvailable = puzzle;
        }
        if (puzzleToSetAvailable.getState().equals(PuzzleState.PLACED))
            puzzleToSetAvailable.setState(PuzzleState.NOT_PLACED);
        else
            throw new PuzzleAlreadyInPuzzlesBoxException();
    }

    /**
     * Method is called in the case when Builder custom level is to be launched so only selected puzzles will remain inside {@code PuzzlesBox} instance. IDs are reset.
     */
    public void savePuzzlesUsed() {
        LOGGER.info("Saving process begun");
        ArrayList<Puzzle> newPuzzlesList = new ArrayList<>();
        int counter = 1;
        for (Puzzle puzzleToSave : puzzlesList) {
            if (puzzleToSave.getState().equals(PuzzleState.PLACED)) {
                puzzleToSave.setState(PuzzleState.NOT_PLACED);
                puzzleToSave.setID(counter);
                newPuzzlesList.add(puzzleToSave);
                LOGGER.info("Used puzzle saved with ID " + counter);
                counter++;
            }
        }
        puzzlesList = newPuzzlesList;
        LOGGER.info("Puzzles saved");
    }

    /**
     * Method is called in the case when Builder custom level is to be re-launched so all puzzles will be made available.
     */
    public void makeAllAvailable() {
        for (Puzzle puzzle : puzzlesList)
            puzzle.setState(PuzzleState.NOT_PLACED);
    }

    /**
     * Overrided method returns string object containing all puzzle's renders.
     *
     * @return - string object containing puzzle render
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        int numberOfPuzzlesToRender = 0;
        for (Puzzle puzzle : puzzlesList) {
            if (puzzle.getState().equals(PuzzleState.NOT_PLACED)) {
                sb.append(puzzle.toString()).append("\n");
                numberOfPuzzlesToRender++;
            }
        }
        return numberOfPuzzlesToRender == 0 ? "" : sb.toString();
    }

    public void setPuzzlesList(ArrayList<Puzzle> puzzlesList) {
        this.puzzlesList = puzzlesList;
        for (Puzzle puzzle : puzzlesList)
            puzzle.setState(PuzzleState.NOT_PLACED);
    }

    public ArrayList<Puzzle> getPuzzlesList() {
        return puzzlesList;
    }
}
