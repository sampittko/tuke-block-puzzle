package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers;

class PuzzlePieceNotYetAvailableException extends Exception {
    PuzzlePieceNotYetAvailableException() {
        super("Exception: Trying to access content which is not yet available");
    }
}
