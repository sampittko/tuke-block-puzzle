package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory;

import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.Puzzle;

public interface PuzzleBuilder {
    Puzzle buildPuzzle(String puzzleToBuild) throws Exception;
    void createDefaultPuzzle();
    void setDefaultOrientation(final String[] decodedPuzzleData);
    int calculateNumberOfRotations(String puzzleToBuild);
    void rotatePuzzleField();
    void buildPuzzleBase(String[] decodedPuzzlesData) throws Exception;
    void buildPuzzleOrientation(final String puzzleToBuild);
}
