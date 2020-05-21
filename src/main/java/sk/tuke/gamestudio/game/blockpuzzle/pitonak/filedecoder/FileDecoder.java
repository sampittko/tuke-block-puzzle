package sk.tuke.gamestudio.game.blockpuzzle.pitonak.filedecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDecoder implements Parser {
    @Autowired private GameConfig gameConfig;

    private final Logger LOGGER = LoggerFactory.getLogger(FileDecoder.class);
    private final Pattern LEVEL_PUZZLES_PATTERN = Pattern.compile("(([234567])([ABCDEFILPSTX])([012])([hvenwsx]),){5,}");
    private final Pattern PUZZLE_PATTERN = Pattern.compile("(([FE]{2,7}),){2,7}([hvenwsx],)");
    private final String DATA_SEPARATOR = ",";
    private final String ENTIRE_FILE_DELIMITER = "\\Z";
    private StringBuilder pathSB;
    private Matcher dataMatcher;
    private File dataFile;

    /**
     * Constructor initializes needed resources.
     */
    public FileDecoder() {
        pathSB = new StringBuilder(100);
    }

    /**
     * STAGE 3
     * This method is called before production stage executes in order to parse data from level dataFile.
     *
     * @param CURRENT_LEVEL - level which is going to be played
     *
     * @return {@code decodedLevelData} - a string containing decoded data of puzzles
     *
     * @throws Exception if some exception concerning loading of level or decoding of puzzles occurs.
     */
    @Override
    public String[] parseLevelData(final int CURRENT_LEVEL) throws Exception {
        String[] decodedLevelData;
        try {
            setFile(gameConfig.getLevelsFolderPath(), CURRENT_LEVEL, gameConfig.getLevelsSuffix());
            String levelDataToDecode = loadPuzzlesFromFile();
            decodedLevelData = detectPuzzlesAndDecode(levelDataToDecode);
        }
        catch(BadContentsException e) {
            LOGGER.error(e.getMessage());
            throw new NoDecodedInformationException();
        }
        return decodedLevelData;
    }

    /**
     * STAGE 3
     * This method builds concrete level data file path and sets it according to {@code currentLevel} parameter.
     *
     * @param LEVELS_FOLDER_PATH - path to the folder in which all the levels are stored
     * @param CURRENT_LEVEL - level which is going to be played
     * @param SUFFIX - format of level data file
     */
    private void setFile(final String LEVELS_FOLDER_PATH, final int CURRENT_LEVEL, final String SUFFIX) {
        if (CURRENT_LEVEL == 0)
            pathSB.append(LEVELS_FOLDER_PATH).append("builder").append(SUFFIX);
        else
            pathSB.append(LEVELS_FOLDER_PATH).append(CURRENT_LEVEL).append(SUFFIX);
        dataFile = new File(pathSB.toString());
        LOGGER.info("Level file set (location: " + pathSB.toString() + ")");
        pathSB.delete(0, pathSB.toString().length());
    }

    /**
     * STAGE 3
     * Simple method reads the level file contents.
     *
     * @return {@code levelDataToDecode} - a string containing contents of level file which needs to be parsed
     *
     * @throws FileNotFoundException if there is no name for such file in levels folder.
     */
    private String loadPuzzlesFromFile() throws FileNotFoundException {
        String levelDataToDecode = new Scanner(dataFile).useDelimiter(ENTIRE_FILE_DELIMITER).next();
        LOGGER.info("Read from file: " + levelDataToDecode);
        return levelDataToDecode;
    }

    /**
     * STAGE 3
     * {@code FileDecoder} analyzes puzzles data read from a file.
     *
     * @return {@code decodedLevelData} - array of strings containing puzzle information in format ready for processing in production
     *
     * @throws BadContentsException if {@code FileDecoder} was not able to differentiate all puzzle pieces in the level dataFile.
     */
    private String[] detectPuzzlesAndDecode(final String PUZZLES_DATA_TO_DECODE) throws BadContentsException {
        String[] decodedLevelData;
        dataMatcher = LEVEL_PUZZLES_PATTERN.matcher(PUZZLES_DATA_TO_DECODE);
        if (dataMatcher.matches()) {
            decodedLevelData = PUZZLES_DATA_TO_DECODE.split(DATA_SEPARATOR);
            LOGGER.info(dataMatcher.groupCount() + " puzzles decoded");
            printDetectedPuzzles(decodedLevelData);
        }
        else
            throw new BadContentsException();
        return decodedLevelData;
    }

    /**
     * STAGE 3
     * Simple method to print decoded data in console.
     */
    private void printDetectedPuzzles(final String[] DECODED_PUZZLES_DATA) {
        int counter = 1;
        for(String puzzle : DECODED_PUZZLES_DATA) {
            LOGGER.info(counter + ". " + puzzle + " ");
            counter++;
        }
    }

    /**
     * STAGE 4
     * This method is called before production stage executes in order to parse data from level dataFile.
     *
     * @param puzzle - string containing the concrete puzzle piece which is going to be loaded from file and decoded
     *
     * @return {@code decodedPuzzleData} - a string containing decoded data of puzzle piece
     *
     * @throws Exception if some exception concerning loading of puzzle or decoding it occurs.
     */
    @Override
    public String[] parsePuzzleData(String puzzle) throws Exception {
        String puzzleDataToDecode;
        String[] decodedPuzzleData;
        try {
            setFile(gameConfig.getPuzzlesFolderPath(), puzzle, gameConfig.getPuzzlesSuffix());
            puzzleDataToDecode = loadPuzzleFromFile();
            decodedPuzzleData = detectPuzzleAndDecode(puzzleDataToDecode);
        }
        catch(Exception e) {
            LOGGER.info(e.getMessage());
            throw new NoDecodedInformationException();
        }
        return decodedPuzzleData;
    }

    /**
     * STAGE 4
     * This method builds concrete puzzle data file path and sets it according to {@code puzzle} parameter.
     *
     * @param PUZZLES_FOLDER_PATH - path to the folder in which all the levels are stored
     * @param PUZZLE - string containing the concrete puzzle piece which is going to be loaded from file and decoded
     * @param SUFFIX - format of the data file
     */
    private void setFile(final String PUZZLES_FOLDER_PATH, final String PUZZLE, final String SUFFIX) {
        pathSB.append(PUZZLES_FOLDER_PATH).append(PUZZLE.substring(0, 3)).append(SUFFIX);
        dataFile = new File(pathSB.toString());
        LOGGER.info("Puzzle file set (location: " + pathSB.toString() + ")");
        pathSB.delete(0, pathSB.toString().length());
    }

    /**
     * STAGE 4
     * Simple method reads the puzzle file contents.
     *
     * @return {@code puzzleDataToDecode} - a string containing puzzle data which needs to be decoded
     *
     * @throws FileNotFoundException if there is no name for such file in puzzles folder.
     */
    private String loadPuzzleFromFile() throws FileNotFoundException {
        String puzzleDataToDecode = new Scanner(dataFile).useDelimiter(ENTIRE_FILE_DELIMITER).next();
        pathSB.delete(0, pathSB.toString().length());
        return puzzleDataToDecode;
    }

    /**
     * STAGE 4
     * {@code FileDecoder} analyzes puzzle data read from a file.
     *
     * @return {@code decodedPuzzleData} - a string containing decoded data of puzzle piece
     *
     * @throws BadContentsException if {@code FileDecoder} was not able to differentiate all puzzle data in the puzzle file.
     */
    private String[] detectPuzzleAndDecode(String puzzleDataToDecode) throws BadContentsException {
        String[] decodedPuzzleData;
        dataMatcher = PUZZLE_PATTERN.matcher(puzzleDataToDecode);
        if (dataMatcher.matches()) {
            LOGGER.info("Puzzle data format correct (data: " + puzzleDataToDecode + ")");
            decodedPuzzleData = puzzleDataToDecode.split(DATA_SEPARATOR);
        }
        else
            throw new BadContentsException();
        return decodedPuzzleData;
    }
}
