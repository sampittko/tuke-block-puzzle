package sk.tuke.gamestudio.game.blockpuzzle.pitonak.filedecoder;

public interface Parser {
    String[] parseLevelData(final int currentLevelNumber) throws Exception;
    String[] parsePuzzleData(String puzzleDataToDecode) throws Exception;
}
