package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers;

class LevelNotAvailableException extends Exception {
    LevelNotAvailableException() {
        super("Exception: Level is not available at the current stage of the game");
    }
}
