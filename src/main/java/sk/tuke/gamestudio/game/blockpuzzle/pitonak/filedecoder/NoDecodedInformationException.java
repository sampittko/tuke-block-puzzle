package sk.tuke.gamestudio.game.blockpuzzle.pitonak.filedecoder;

class NoDecodedInformationException extends Exception {
    NoDecodedInformationException() {
        super("Exception: Failed to parse file contents");
    }
}
