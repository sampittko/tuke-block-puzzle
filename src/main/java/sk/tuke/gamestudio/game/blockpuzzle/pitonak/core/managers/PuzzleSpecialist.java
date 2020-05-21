package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.Cleaner;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.Puzzle;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory.AbstractPuzzleFactory;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory.PentominoFactory;

import java.util.ArrayList;

public class PuzzleSpecialist implements ProductionManagement, Cleaner {
    @Autowired PentominoFactory pentominoFactory;

    private final Logger LOGGER = LoggerFactory.getLogger(PuzzleSpecialist.class);
    private ArrayList<AbstractPuzzleFactory> factoriesList;
    private ArrayList<Puzzle> puzzlesList;

    /**
     * Constructor initializes needed resources.
     */
    public PuzzleSpecialist() {
        factoriesList = new ArrayList<>();
        puzzlesList = new ArrayList<>();
    }

    /**
     * STAGE 3
     * This method determines which factory is going to be used to produce certain puzzle piece.
     *
     * @param decodedPuzzlesData - array of strings which holds all the puzzles information which were decoded using {@code FileDecoder}
     *
     * @throws PuzzlePieceNotYetAvailableException when there was a request to build factory which is not yet available in the game.
     */
    @Override
    public void manageFactories(final String[] decodedPuzzlesData) throws PuzzlePieceNotYetAvailableException {
        for (String puzzle : decodedPuzzlesData) {
            switch(puzzle.charAt(0)) {
                case '2': throw new PuzzlePieceNotYetAvailableException();
                case '3': throw new PuzzlePieceNotYetAvailableException();
                case '4': throw new PuzzlePieceNotYetAvailableException();
                case '5': usePentominoFactory(puzzle); break;
                case '6': throw new PuzzlePieceNotYetAvailableException();
                case '7': throw new PuzzlePieceNotYetAvailableException();
                default: throw new PuzzlePieceNotYetAvailableException();
            }
        }
        printProductionLines();
    }

    /**
     * STAGE 3
     * This method creates Pentomino factory if it does not already exist. It also assigns correct puzzle piece to it's production line.
     *
     * @param puzzle - string in which concrete and correct puzzle piece for factory is stored
     */
    private void usePentominoFactory(String puzzle) {
        for (AbstractPuzzleFactory puzzleFactory : factoriesList) {
            if (puzzleFactory instanceof PentominoFactory) {
                puzzleFactory.addPuzzleToProductionLine(puzzle);
                LOGGER.info("Puzzle added to existing Pentomino factory's production line");
                return;
            }
        }
        factoriesList.add(pentominoFactory);
        factoriesList.get(factoriesList.size() - 1).addPuzzleToProductionLine(puzzle);
        LOGGER.info("Pentomino factory was built and puzzle was added to it's production line");
    }

    /**
     * STAGE 3
     * Each factory in {@code factoriesList} has it's own production line which is printed into the console one-by-one just to have a little bit insight into the production process.
     */
    private void printProductionLines() {
        LOGGER.info("Production of puzzles was divided into specialized factories.");
        int counter = 1;
        for (AbstractPuzzleFactory puzzleFactory : factoriesList) {
            LOGGER.info(counter + ". factory: " + puzzleFactory.getProductionLine());
            counter++;
        }
    }

    /**
     * STAGE 4
     * Requests are sent to all factory's engineers to produce their puzzles.
     *
     * @throws Exception if there was a problem in production of puzzle in any factory.
     */
    @Override
    public void beginProduction() throws Exception {
        for(AbstractPuzzleFactory puzzleFactory : factoriesList)
            puzzleFactory.buildPuzzles();
    }

    /**
     * STAGE 4
     * Auxiliary method which sends all produced puzzles to {@code ConsoleManager}.
     *
     * @return {@code puzzlesList} - list containing all produced puzzles
     */
    @Override
    public ArrayList<Puzzle> getPuzzles() {
        for (AbstractPuzzleFactory puzzleFactory : factoriesList)
            puzzlesList.addAll(puzzleFactory.getPuzzlesList());
        return puzzlesList;
    }

    /**
     * This method prepares instance for next level.
     */
    @Override
    public void clean() {
        for (AbstractPuzzleFactory factory : factoriesList)
            factory.clean();
        puzzlesList = new ArrayList<>();
        LOGGER.info("Prepared");
    }
}
