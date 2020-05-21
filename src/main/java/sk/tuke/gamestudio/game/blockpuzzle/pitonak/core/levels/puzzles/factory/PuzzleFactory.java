package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory;

public interface PuzzleFactory {
    void addPuzzleToProductionLine(String puzzle);
    void buildPuzzles() throws Exception;
}
