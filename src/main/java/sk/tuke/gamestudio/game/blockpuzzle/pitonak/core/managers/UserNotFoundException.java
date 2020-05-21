package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers;

class UserNotFoundException extends Exception {
    UserNotFoundException() {
        super("Exception: Cannot save score for non-existing player");
    }
}
