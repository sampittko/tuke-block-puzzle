package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers;

import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.Puzzle;

import java.util.ArrayList;

public interface ProductionManagement {
    void manageFactories(final String[] decodedPuzzlesData) throws PuzzlePieceNotYetAvailableException;
    void beginProduction() throws Exception;
    ArrayList<Puzzle> getPuzzles();
}
