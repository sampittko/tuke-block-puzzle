package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.Score;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScoreServiceJDBC implements ScoreService {
    public static final String INSERT_SCORE =
            "INSERT INTO score (game, player, points, playedon) VALUES (?, ?, ?, ?)";

    public static final String SELECT_SCORE =
            "SELECT game, player, points, playedon FROM score WHERE game = ? ORDER BY points DESC LIMIT 10";


    @Override
    public void addScore(Score score) throws ScoreException {
        try (Connection connection = DriverManager.getConnection(JDBC.URL, JDBC.USER, JDBC.PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_SCORE)) {
                ps.setString(1, score.getGame());
                ps.setString(2, score.getPlayer());
                ps.setInt(3, score.getPoints());
                ps.setTimestamp(4, new Timestamp(new Date().getTime()));

                ps.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new ScoreException("Error saving score", e);
        }
    }

    @Override
    public List<Score> getBestScores(String game) throws ScoreException {
        List<Score> scores = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC.URL, JDBC.USER, JDBC.PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_SCORE)) {
                ps.setString(1, game);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Score score = new Score(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getInt(3),
                                rs.getTimestamp(4)
                        );
                        scores.add(score);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new ScoreException("Error loading score", e);
        }
        return scores;
    }
}
