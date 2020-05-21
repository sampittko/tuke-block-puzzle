package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory;

class PuzzleNotValidException extends Exception {
    PuzzleNotValidException() {
        super("Exception: Puzzle cannot be build because number of tiles needed was not reached");
    }
}
