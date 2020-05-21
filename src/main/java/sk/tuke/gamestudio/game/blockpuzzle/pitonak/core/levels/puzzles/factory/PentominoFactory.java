package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PentominoFactory extends AbstractPuzzleFactory {
    @Autowired private PentominoEngineer pentominoEngineer;

    private final Logger LOGGER = LoggerFactory.getLogger(PentominoFactory.class);

    /**
     * Factory sends request to it's Engineer so he can start the production of puzzle piece. Collection of {@code Puzzle} objects is filled when puzzle is built.
     *
     * @throws Exception when Engineer was not able to produce requested puzzle piece.
     */
    @Override
    public void buildPuzzles() throws Exception {
        for(String puzzleToBuild : getProductionLine()) {
            LOGGER.info("Puzzle " + puzzleToBuild + " is in production");
            getPuzzlesList().add(pentominoEngineer.buildPuzzle(puzzleToBuild));
        }
    }

    /**
     * This method prepares instance for next level.
     */
    @Override
    public void clean() {
        super.clean();
        LOGGER.info("Prepared");
    }
}
