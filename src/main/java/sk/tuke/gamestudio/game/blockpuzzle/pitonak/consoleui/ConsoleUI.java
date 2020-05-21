package sk.tuke.gamestudio.game.blockpuzzle.pitonak.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.GameSessionRecord;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.Level;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels.LevelState;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig;
import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// CONSOLE GAMEPLAY IMPROVEMENTS
// TODO choice to show concrete player's rating in database menu
// TODO quit the game choice in the first menu
// TODO show database choice after quitting game (already updated)
// TODO choice to start new game after quitting one

public class ConsoleUI {
    @Autowired private GameConfig gameConfig;
    @Autowired private Level level;
    @Autowired private GameSessionRecord gameSessionRecord;
    @Autowired private ScoreService scoreService;
    @Autowired private CommentService commentService;
    @Autowired private RatingService ratingService;

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    public static final String YELLOW = "\u001B[33m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";

    private final Pattern INPUT_PATTERN = Pattern.compile("((P):([1-5]),([A-E])([1-5]),([A-E])([1-5]))|((T):([1-5]))");
    private final Pattern RATING_ANSWER_PATTERN = Pattern.compile("[12345]");
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private Matcher m;
    private int messageID;

    /**
     * The main {@code ConsoleUI} method responsible for everything what is happening throughout the gameplay.
     */
    public void run() {
        printTitle();
        showDatabase();
        play();
        updateGameSessionRecord();
    }

    /**
     * Intro message.
     */
    private void printTitle() {
        System.out.println(YELLOW);
        System.out.println("_|_|_|     _|                         _|             _|_|_|                                      _|" + CYAN);
        System.out.println("_|    _|   _|     _|_|       _|_|_|   _|  _|         _|    _|   _|    _|   _|_|_|_|   _|_|_|_|   _|     _|_|" + GREEN);
        System.out.println("_|_|_|     _|   _|    _|   _|         _|_|           _|_|_|     _|    _|       _|         _|     _|   _|_|_|_|" + RED);
        System.out.println("_|    _|   _|   _|    _|   _|         _|  _|         _|         _|    _|     _|         _|       _|   _|" + BLUE);
        System.out.println("_|_|_|     _|     _|_|       _|_|_|   _|    _|       _|           _|_|_|   _|_|_|_|   _|_|_|_|   _|     _|_|_|" + RESET);
        System.out.println();
        System.out.println(GREEN + "Level " + level.getCurrentLevelNumber() + " is ready to be played.");
    }

    /**
     * Player's database view navigation menu.
     */
    private void showDatabase() {
        if (!askQuestion(4)) {
            String answer;
            do {
                printDBCommandsList();
                System.out.print(BLACK + "Insert command: " + RESET);
                answer = readInput();
                if (!answer.equals("X")) {
                    try {
                        switch (answer) {
                            case "1": showDBTop10(); break;
                            case "2": showDBComments(); break;
                            case "3": showDBAverageRating(); break;
                            default: break;
                        }
                    }
                    catch (Exception e) {
                        System.out.println(RED + "Communication with database was not successful." + RESET);
                    }
                }
            }
            while (!answer.equals("X"));
        }
    }

    /**
     * Method which prints all possible actions which can player take through available commands in database view.
     */
    private void printDBCommandsList() {
        System.out.print(YELLOW + "Available commands:\n" +
                CYAN + "             SHOW TOP 10: " + RESET + "1   |   " +
                CYAN + "SHOW PLAYER'S COMMENTS: " + RESET + "2   |   " +
                CYAN +"SHOW GAME'S AVERAGE RATING: " + RESET + "3   |   " +
                CYAN +"EXIT MENU: " + RESET + "X\n");
    }

    /**
     * Method renders top 10 player's scores in the console.
     */
    private void showDBTop10() {
        List<Score> top10 = scoreService.getBestScores(gameConfig.getGameName());
        int counter = 0;
        System.out.println(GREEN + "Top 10 players:\n");
        for (Score score : top10) {
            counter++;
            System.out.println(new Formatter().format("%s%14d%s%1s%3s%s%3s%3s%s%s%s%1s%d%3s%3s%s%s%s%1s%ta%1s%tb%1s%td%1s%tT%1s%tY%n",
                    CYAN, counter, ".", "", score.getPlayer(), RESET, "|", "", CYAN, "POINTS:", RESET,
                    "", score.getPoints(), "|", "", CYAN, "PLAYED ON:", RESET, "", score.getPlayedOn(), "",
                    score.getPlayedOn(), "", score.getPlayedOn(), "", score.getPlayedOn(), "", score.getPlayedOn()).toString());
        }
    }

    /**
     * Method renders 10 latest comments from players in console.
     */
    private void showDBComments() throws CommentException {
        List<Comment> comments = commentService.getComments(gameConfig.getGameName());
        int counter = 0;
        System.out.println(GREEN + "Comments:\n");
        for (Comment comment : comments) {
            counter++;
            System.out.println(new Formatter().format("%s%14d%s%1s%3s%s%3s%3s%s%s%s%1s%s%3s%3s%s%s%s%1s%ta%1s%tb%1s%td%1s%tT%1s%tY%n",
                    CYAN, counter, ".", "", comment.getPlayer(), RESET, "|", "", CYAN, "COMMENT:", RESET,
                    "", comment.getComment(), "|", "", CYAN, "COMMENTED ON:", RESET, "", comment.getCommentedOn(), "",
                    comment.getCommentedOn(), "", comment.getCommentedOn(), "", comment.getCommentedOn(), "", comment.getCommentedOn()).toString());
        }
    }

    /**
     * Method shows average rating of the game.
     */
    private void showDBAverageRating() throws RatingException {
        System.out.println(GREEN + "Average game rating:\n");
        System.out.println(new Formatter().format("%18s%s%d%1s%n", CYAN, "Block puzzle has ", ratingService.getAverageRating(gameConfig.getGameName()), " out of 5 points in user's feedback.").toString());
    }

    /**
     * Method which contains while loop which is under one condition: {@code LevelState} equals {@code LevelState.PLAYING}
     */
    private void play() {
        pressKeyWhenReady();
        do {
            renderUI();
            processInput();
        }
        while(level.getState().equals(LevelState.PLAYING));
    }

    private void pressKeyWhenReady() {
        level.updateState();
        printGameCommandsList();
        System.out.print(BLACK + "Press any key to start playing: " + RESET);
        try {
            System.in.read();
        }
        catch (Exception e)
        { }
        level.setStartMillis(System.currentTimeMillis());
        level.updateState();
    }

    /**
     * Method which prints all possible actions which can player take through available commands in-game.
     */
    private void printGameCommandsList() {
        System.out.print(YELLOW + "Available commands:\n" +
                CYAN + "             PLACE PUZZLE: " + RESET + "P:[PUZZLE_ID],[PUZZLES_BOX_COORDINATES],[FIELD_COORDINATES]   |   " +
                CYAN + "TAKE PUZZLE BACK: " + RESET + "T:[PUZZLE_ID]   |   " +
                CYAN +"CANCEL GAME: " + RESET + "X\n");
    }

    /**
     * Method renders {@code ConsoleUI}. (prints {@code PuzzlesBox}, {@code Field}, message about last action and score with time played.)
     */
    private void renderUI() {
        System.out.println(level);
        printMessage();
        printGameInfo();
    }

    /**
     * Method prints all kinds of messages on standard output.
     */
    private void printMessage() {
        switch(messageID) {
            case 104: System.out.println(GREEN + "Puzzle was placed into the field."); break;
            case 105: System.out.println(GREEN + "Puzzle was placed back into the box."); break;
            case 400: System.out.println(RED + "Cannot insert selected puzzle to the specified coordinates."); break;
            case 404: System.out.println(RED + "Cannot take selected puzzle back."); break;
            case 406: System.out.println(RED + "Bad input format. Try again."); break;
            default: break;
        }
    }

    /**
     * Method prints information about playtime and score.
     */
    private void printGameInfo() {
        System.out.printf(YELLOW + "Game info:%n" + CYAN +
                "             PLAYTIME: " + RESET + "%d %s   |   " + CYAN +
                "SCORE: " + RESET + "%d %s%n", level.getPlayingTime(), "seconds", level.getCurrentScore(), "points");
    }

    /**
     * This method reads user's input and processes it for further use.
     */
    private void processInput() {
        printGameCommandsList();
        System.out.print(BLACK + "Insert command: " + RESET);
        String input = readInput();
        // game canceled by user
        if (input.equals("X")) {
            level.setState(LevelState.CANCELED);
            System.out.println(RED + "Game canceled." + RESET);
            return;
        }
        m = INPUT_PATTERN.matcher(input);
        if (m.matches()) {
            try {
                // PLACE action
                if (m.group(2).equals("P")) {
                    try {
                        level.insertPuzzle(Integer.parseInt(m.group(3)), m.group(4).charAt(0) - 65, Integer.parseInt(m.group(5)) - 1, m.group(6).charAt(0) - 65, Integer.parseInt(m.group(7)) - 1);
                        messageID = 104;
                    }
                    catch(Exception e) {
                        messageID = 400;
                    }
                }
            }
            // TAKE action
            catch (NullPointerException e) {
                try {
                    level.takePuzzleBack(Integer.parseInt(m.group(10)));
                    messageID = 105;
                }
                catch (Exception f) {
                    messageID = 404;
                }
            }
        }
        else messageID = 406;
    }

    /**
     * Method reads input line.
     *
     * @return {@code br} - buffer containing line entered by player
     */
    private String readInput() {
        try {
            return br.readLine();
        }
        catch (IOException e) {
            System.err.println(RED + "Input reader failed to read your input. Try again, please.");
            return "";
        }
    }

    /**
     * Method updates {@code GameSessionRecord} instance according to final {@code LevelState} and it also lets player to enter nickname, comment and rating.
     */
    public void updateGameSessionRecord() {
        String nickname = "", comment = "", rating = "0";
        gameSessionRecord.setEndGameState(level.getState());
        // null exception fix
        if (gameSessionRecord.getPlayer() == null)
            gameSessionRecord.setPlayer(nickname);
        if (gameSessionRecord.getComment() == null)
            gameSessionRecord.setComment(comment);
        // canceled while first level running (means that no score will be added to database)
        if (gameSessionRecord.getEndGameState().equals(LevelState.CANCELED) && level.getCurrentLevelNumber() == 1)
            return;
        // player solved current level
        else if (gameSessionRecord.getEndGameState().equals(LevelState.SOLVED)) {
            printWinningMessage();
            if (askQuestion(1)) {
                gameSessionRecord.addScore(level.getTotalScore());
                return;
            }
        }
        // player did not want to play next level
        // or
        // player canceled the game while he was playing level with number higher than 1 (means that there could be some score/s left which need to be added to database from previous level/s)
        // or
        // player wanted to play next level but it was not available
        if (level.getCurrentLevelNumber() != gameConfig.getAvailableLevelsNumber() + 1)
            gameSessionRecord.addScore(level.getTotalScore());
        else
            System.out.println(RED + "Sorry, but this level is not available at current stage of the game.\nPlease, enter your details below." + RESET);
        System.out.print(BLACK + "Enter your nickname: " + RESET);
        nickname = readInput();
        gameSessionRecord.setPlayer(nickname);
        // ask for comment
        if (askQuestion(2)) {
            do {
                System.out.print("Write comment: ");
                comment = readInput();
            }
            while (comment.length() > 64);
            gameSessionRecord.setCommentedOn(new Date());
            gameSessionRecord.setComment(comment);
        }
        // ask for rating
        if (askQuestion(3)) {
            System.out.println(RED + "How many will you give? (1-5)" + RESET);
            do {
                System.out.print(BLACK + "Answer: " + RESET);
                rating = readInput();
                m = RATING_ANSWER_PATTERN.matcher(rating);
            }
            while (!m.matches());
            gameSessionRecord.setRatedOn(new Date());
            gameSessionRecord.setRating(Integer.parseInt(rating));
        }
    }

    /**
     * Method prints player's simple message concerning successful end of level to standard output.
     */
    private void printWinningMessage() {
        System.out.println(level.toString());
        System.out.println(GREEN + "Congratulations! You have just successfully completed level number " + level.getCurrentLevelNumber() + "!");
        printScore();
    }

    /**
     * Method prints final score to the standard output.
     */
    private void printScore() {
        gameSessionRecord.setPlayedOn(new Date());
        System.out.println(GREEN + "You got " + level.getTotalScore() + " points in " + level.getPlayingTime() + " seconds.");
    }

    /**
     * Method prints chosen question to the standard output and asks for answer.
     *
     * @param questionID - question identification number
     *
     * @return true or false, depending on player's input
     */
    private boolean askQuestion(int questionID) {
        switch(questionID) {
            case 1: System.out.println(RED + "Do you wish to play next level? (Y/N)" + RESET); break;
            case 2: System.out.println(RED + "Do you want to leave a comment? (Y/N)" + RESET); break;
            case 3: System.out.println(RED + "Would you like to rate my game? (Y/N)" + RESET); break;
            case 4: System.out.println(RED + "Would you like to play the game or show database? (1 - play the game, 2 - show database)" + RESET); break;
            case 5: System.out.println(RED + "Would you like to play the game now? (Y/N)" + RESET); break;
            default: break;
        }
        String answer;
        if (questionID == 4) {
            do {
                System.out.print(BLACK + "Answer: " + RESET);
                answer = readInput();
            }
            while (!answer.equals("1") && !answer.equals("2"));
            return answer.equals("1");
        }
        else {
            do {
                System.out.print(BLACK + "Answer: " + RESET);
                answer = readInput();
            }
            while (!answer.equals("Y") && !answer.equals("N"));
            return answer.equals("Y");
        }
    }
}
