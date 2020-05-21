package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.managers;

import sk.tuke.gamestudio.game.blockpuzzle.pitonak.webui.WebAction;

public interface WebGameManagement {
    void loadLevel(WebAction action) throws Exception;
    void cancelGameSession(String username) throws Exception;
}
