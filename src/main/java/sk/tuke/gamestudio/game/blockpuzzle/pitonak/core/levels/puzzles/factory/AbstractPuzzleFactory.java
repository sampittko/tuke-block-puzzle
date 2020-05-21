package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory;

import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.Puzzle;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.Cleaner;

import java.util.ArrayList;

public abstract class AbstractPuzzleFactory implements PuzzleFactory, Cleaner {
    private ArrayList<String> productionLine;
    private ArrayList<Puzzle> producedPuzzlesList;

    /**
     * Constructor initializes needed resources.
     */
    public AbstractPuzzleFactory() {
        productionLine = new ArrayList<>();
        producedPuzzlesList = new ArrayList<>();
    }

    /**
     * Simple method adds puzzle piece to production line of factory.
     *
     * @param puzzle - string which is added to the collection of puzzles which are going to be produced
     */
    @Override
    public void addPuzzleToProductionLine(String puzzle) {
        productionLine.add(puzzle);
    }

    public ArrayList<String> getProductionLine() {
        return productionLine;
    }

    public ArrayList<Puzzle> getPuzzlesList() {
        return producedPuzzlesList;
    }

    @Override
    public abstract void buildPuzzles() throws Exception;

    /**
     * This method prepares instance for next level.
     */
    @Override
    public void clean() {
        productionLine = new ArrayList<>();
        producedPuzzlesList = new ArrayList<>();
    }
}
