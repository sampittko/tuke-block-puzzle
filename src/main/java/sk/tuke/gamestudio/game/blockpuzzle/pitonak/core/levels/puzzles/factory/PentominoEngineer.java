package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory;

import org.slf4j.LoggerFactory;

public class PentominoEngineer extends AbstractPuzzleEngineer {
    /**
     * Constructor gives needed additional information to the Abstract Engineer.
     */
    public PentominoEngineer() {
        super(5, LoggerFactory.getLogger(PentominoEngineer.class));
    }
}