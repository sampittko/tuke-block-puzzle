package sk.tuke.gamestudio.game.blockpuzzle.pitonak.webui;

class WebActionNotPermittedException extends Exception {
    WebActionNotPermittedException() {
        super("Exception: This action is not permitted");
    }
}
