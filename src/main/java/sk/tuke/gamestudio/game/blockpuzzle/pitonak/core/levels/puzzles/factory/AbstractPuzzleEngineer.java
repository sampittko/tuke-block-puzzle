package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.factory;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Tile;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.TileState;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.Puzzle;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.puzzles.PuzzleState;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.filedecoder.FileDecoder;

abstract class AbstractPuzzleEngineer implements PuzzleBuilder {
    @Autowired private FileDecoder fileDecoder;

    private final Logger LOGGER;
    private final char[] ORIENTATIONS = {'e', 'n', 'w', 's'};
    private final int MAX_FULL_TILES_COUNT;
    private Puzzle puzzle;
    private char defaultOrientation;

    /**
     * Constructor sets path to puzzles folder and file format which are both constant.
     *
     * @param MAX_FULL_TILES_COUNT - the number of full tiles which
     * @param LOGGER - concrete factory's logger
     */
    public AbstractPuzzleEngineer(final int MAX_FULL_TILES_COUNT, final Logger LOGGER) {
        this.MAX_FULL_TILES_COUNT = MAX_FULL_TILES_COUNT;
        this.LOGGER = LOGGER;
    }

    /**
     * This method is using other auxiliary methods to build the requested puzzle piece.
     *
     * @param puzzleToBuild - string object of puzzle in which all needed information is stored for Engineer to build it
     *
     * @return {@code Puzzle} - requested puzzle piece
     *
     * @throws Exception if there was a problem loading data from a puzzle file or in building process itself
     */
    @Override
    public Puzzle buildPuzzle(String puzzleToBuild) throws Exception {
        createDefaultPuzzle();
        String[] decodedPuzzleData = fileDecoder.parsePuzzleData(puzzleToBuild);
        setDefaultOrientation(decodedPuzzleData);
        buildPuzzleBase(decodedPuzzleData);
        buildPuzzleOrientation(puzzleToBuild);
        printFinalPuzzle();
        return puzzle;
    }

    /**
     * Method creates default puzzle instance and sets its state to {@code PuzzleState.IN_PRODUCTION}.
     */
    @Override
    public void createDefaultPuzzle() {
        puzzle = new Puzzle(MAX_FULL_TILES_COUNT);
        LOGGER.info("Puzzle's unique ID is " + puzzle.getID());
        puzzle.setState(PuzzleState.IN_PRODUCTION);
    }

    /**
     * This method sets puzzle's orientation which it has when just loaded from concrete puzzle data file according to parsed {@code decodedPuzzleData} string.
     *
     * @param decodedPuzzleData - parsed string containing puzzle data
     */
    @Override
    public void setDefaultOrientation(final String[] decodedPuzzleData) {
        defaultOrientation = decodedPuzzleData[decodedPuzzleData.length - 1].charAt(0);
    }

    /**
     * This method sets correct puzzles' tile states. When there is tile in a matrix which is occupied by puzzle, the state is set accordingly.
     *
     * @param decodedPuzzleData - array of strings which holds all the puzzle information which were decoded using {@code FileDecoder}
     *
     * @throws PuzzleNotValidException if the puzzle data do not contain correct number of tiles with {@code TileState.FULL} state
     */
    @Override
    public void buildPuzzleBase(String[] decodedPuzzleData) throws PuzzleNotValidException {
        Tile[][] puzzleTileField = puzzle.getTileField();
        int fullTilesCount = 0;
        int y = 0;

        for (String puzzleDataPart : decodedPuzzleData) {
            for (int x = 0; x < puzzleDataPart.length(); x++) {
                if (puzzleDataPart.charAt(x) == 'F') {
                    puzzleTileField[y][x].setState(TileState.FULL);
                    fullTilesCount++;
                }
            }
            y++;
        }
        checkPuzzleValidity(fullTilesCount);
        LOGGER.info("Puzzle base built");
        puzzle.updateTileField(puzzleTileField);
    }

    /**
     * If there are more than {@code MAX_FULL_TILES_COUNT} tiles with {@code TileState.FULL} state, {@code PuzzleNotValidException} is thrown and the game cannot be started.
     *
     * @param fullTilesCount - tiles with {@code TileState.FULL} state in the current's puzzle piece data
     *
     * @throws PuzzleNotValidException if puzzle data contains too little or too many tiles with {@code PuzzleState.FULL} state
     */
    private void checkPuzzleValidity(int fullTilesCount) throws PuzzleNotValidException {
        if (fullTilesCount != MAX_FULL_TILES_COUNT) {
            throw new PuzzleNotValidException();
        }
        LOGGER.info("Full tiles count correct");
    }

    /**
     * Method sets correct puzzle's orientation. It also contains auxiliary methods.
     *
     * @param puzzleToBuild - constant string object containing puzzle identification data
     */
    @Override
    public void buildPuzzleOrientation(final String puzzleToBuild) {
        int numberOfRotations = calculateNumberOfRotations(puzzleToBuild);
        if (numberOfRotations > 0) {
            numberOfRotations -= 4;
            LOGGER.info(Math.abs(numberOfRotations) + " rotation/s to reach the requested one");
        }
        else
            LOGGER.info("Puzzle already has correct orientation");
        while(numberOfRotations != 0) {
            rotatePuzzleField();
            numberOfRotations++;
        }
        LOGGER.info("Puzzle's orientation set");
    }

    /**
     * Method returns the number of 90 degree rotations of {@code puzzle} field which are needed to be performed in order to reach the requested orientation by the level.
     *
     * @param puzzleToBuild - constant string object containing puzzle data
     *
     * @return - integer value which represents the number of rotations needed for puzzle to make from default orientation in order to reach the requested orientation
     */
    @Override
    public int calculateNumberOfRotations(String puzzleToBuild) {
        char requestedOrientation = puzzleToBuild.charAt(3);
        if (requestedOrientation == defaultOrientation) return 0;
        else if (requestedOrientation != 'v' && requestedOrientation != 'h') {
            for(int i = 0; i < ORIENTATIONS.length; i++) {
                if (ORIENTATIONS[i] == defaultOrientation) {
                    for(int j = 0; j < ORIENTATIONS.length; j++) {
                        if (ORIENTATIONS[j] == requestedOrientation) {
                            return i - j;
                        }
                    }
                }
            }
        }
        return 1;
    }

    /**
     * This method performs the rotation of puzzle's field in anti-clockwise direction and each time when the algorithm is over, puzzle's field is updated.
     */
    @Override
    public void rotatePuzzleField() {
        Tile[][] puzzleTileField = puzzle.getTileField();
        Tile top, rightSide, bottom, leftSide;
        int first, last, offset;
        int fieldSize = puzzleTileField.length;
        int layerCount = fieldSize/2;

        for (int layer = 0; layer < layerCount; layer++) {
            first = layer;
            last = fieldSize - first - 1;
            for (int element = first; element < last; element++) {
                offset = element - first;
                top = puzzleTileField[element][first];
                rightSide = puzzleTileField[last][element];
                bottom = puzzleTileField[last-offset][last];
                leftSide = puzzleTileField[first][last-offset];

                puzzleTileField[element][first] = leftSide;
                puzzleTileField[last][element] = top;
                puzzleTileField[last-offset][last] = rightSide;
                puzzleTileField[first][last-offset] = bottom;
            }
        }
        puzzle.updateTileField(puzzleTileField);
    }

    /**
     * This is just informal method which prints newly produced tile to the output stream.
     */
    private void printFinalPuzzle() {
        puzzle.setState(PuzzleState.BUILT);
        LOGGER.info("Puzzle built successfully");
        for (Tile[] tilesArray : puzzle.getTileField()) {
            StringBuilder sb = new StringBuilder(20);
            for (Tile tile : tilesArray) {
                if (tile.getState().equals(TileState.EMPTY))
                    sb.append("- ");
                else
                    sb.append("* ");
            }
            LOGGER.info(sb.toString());
        }
    }
}
