package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentServiceJDBC implements CommentService {
    private static final String INSERT_COMMENT =
            "INSERT INTO comment (player, game, comment, commentedon) VALUES (?, ?, ?, ?)";

    private static final String SELECT_COMMENT =
            "SELECT player, game, comment, commentedon FROM comment WHERE game = ? ORDER BY commentedon DESC LIMIT 10";

    @Override
    public void addComment(Comment comment) throws CommentException {
        try (Connection connection = DriverManager.getConnection(JDBC.URL, JDBC.USER, JDBC.PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(INSERT_COMMENT)){
                ps.setString(1, comment.getPlayer());
                ps.setString(2, comment.getGame());
                ps.setString(3, comment.getComment());
                ps.setTimestamp(4, new Timestamp(new Date().getTime()));

                ps.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new CommentException("Error saving comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC.URL, JDBC.USER, JDBC.PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_COMMENT)){
                ps.setString(1, game);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()) {
                        Comment comment = new Comment(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getTimestamp(4)
                        );
                        comments.add(comment);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new CommentException("Error loading comment", e);
        }
        return comments;
    }
}
