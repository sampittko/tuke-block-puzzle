package sk.tuke.gamestudio.game.blockpuzzle.pitonak.filedecoder;

class BadContentsException extends Exception {
    BadContentsException() {
        super("Exception: Puzzle of bad format or no puzzle found");
    }
}