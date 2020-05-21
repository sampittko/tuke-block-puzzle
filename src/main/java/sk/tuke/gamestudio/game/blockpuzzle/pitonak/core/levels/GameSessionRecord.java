package sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.levels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.GameConfig;
import sk.tuke.gamestudio.game.blockpuzzle.pitonak.core.Cleaner;

import java.util.Date;

public class GameSessionRecord implements Cleaner {
    @Autowired private GameConfig gameConfig;

    private final Logger LOGGER = LoggerFactory.getLogger(GameSessionRecord.class);

    private String playedBy;

    public String getPlayer() {
        return playedBy;
    }

    public void setPlayer(String playedBy) {
        this.playedBy = playedBy;
    }

    private int[] scores;

    /**
     * Method adds score to the list of scores of game session record.
     *
     * @param score - integer value which represents score gained in level
     */
    public void addScore(int score) {
        if (scores == null) {
            scores = new int[gameConfig.getAvailableLevelsNumber()];
            for (int i = 0; i < scores.length; i++)
                scores[i] = -1;
        }

        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == -1) {
                scores[i] = score;
                return;
            }
        }
    }

    private int totalScore = 0;

    /**
     * Method which sums scores from all levels played.
     *
     * @return totalScore - integer value which represents total score gained in current game session
     */
    public int getTotalScore() {
        for (int score : scores) {
            if (score != -1)
                totalScore += score;
        }
        return totalScore;
    }

    public boolean hasNoScores() {
        return scores == null;
    }

    private LevelState endGameState;

    public LevelState getEndGameState() {
        return endGameState;
    }

    public void setEndGameState(LevelState endGameState) {
        this.endGameState = endGameState;
    }

    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    private Date playedOn;

    public Date getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(Date playedOn) {
        this.playedOn = playedOn;
    }

    private Date commentedOn;

    public Date getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(Date commentedOn) {
        this.commentedOn = commentedOn;
    }

    private Date ratedOn;

    public Date getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn(Date ratedOn) {
        this.ratedOn = ratedOn;
    }

    /**
     * This method prepares {@code GameSessionRecord} instance for the upcoming level.
     */
    @Override
    public void clean() {
        scores = null;
        totalScore = 0;
        LOGGER.info("Prepared");
    }
}
