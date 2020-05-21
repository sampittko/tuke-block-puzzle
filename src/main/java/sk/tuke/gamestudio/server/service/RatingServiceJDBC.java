package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.Rating;

import java.sql.*;
import java.util.Date;

public class RatingServiceJDBC implements RatingService {
    public static final String INSERT_RATING =
            "INSERT INTO rating (player, game, rating, ratedon) VALUES (?, ?, ?, ?)";

    public static final String SELECT_AVERAGE_RATING =
            "SELECT AVG(rating) FROM rating WHERE game = ?";

    public static final String SELECT_RATING =
            "SELECT rating FROM rating WHERE game = ? AND player = ?";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(JDBC.URL, JDBC.USER, JDBC.PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(INSERT_RATING)){
                ps.setString(1, rating.getPlayer());
                ps.setString(2, rating.getGame());
                ps.setInt(3, rating.getRating());
                ps.setTimestamp(4, new Timestamp(new Date().getTime()));

                ps.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new RatingException("Error saving rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        int rating = 0;
        try (Connection connection = DriverManager.getConnection(JDBC.URL, JDBC.USER, JDBC.PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_AVERAGE_RATING)){
                ps.setString(1, game);
                try(ResultSet rs = ps.executeQuery()) {
                    if (rs.next())
                        rating = rs.getInt(1);
                }
            }
        }
        catch (SQLException e) {
            throw new RatingException("Error loading average rating", e);
        }
        return rating;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        int rating = 0;
        try (Connection connection = DriverManager.getConnection(JDBC.URL, JDBC.USER, JDBC.PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_RATING)){
                ps.setString(1, game);
                ps.setString(2, player);
                try(ResultSet rs = ps.executeQuery()) {
                    if (rs.next())
                        rating = rs.getInt(1);
                }
            }
        }
        catch (SQLException e) {
            throw new RatingException("Error loading rating", e);
        }
        return rating;
    }
}
