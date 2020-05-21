import org.junit.Before;
import org.junit.Test;
import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.service.CommentService;
import sk.tuke.gamestudio.server.service.CommentServiceJDBC;
import sk.tuke.gamestudio.server.service.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;

public class CommentServiceJDBCTest extends CommentServiceTest {
    public static final String DELETE = "DELETE FROM comment";

    public CommentServiceJDBCTest() {
        super.commentService = new CommentServiceJDBC();
    }

    @Before
    public void setUp() throws Exception {
        Connection c = DriverManager.getConnection(JDBC.URL, JDBC.USER, JDBC.PASSWORD);
        Statement s = c.createStatement();
        s.execute(DELETE);
    }

    @Test
    public void testDbInit() throws Exception {
        super.testDbInit();
    }

    @Test
    public void testAddComment() throws Exception {
        super.testAddComment();
    }

    @Test
    public void testGetComments() throws Exception {
        super.testGetComments();
    }

    public static void main(String[] args) throws Exception {
        CommentService commentService = new CommentServiceJDBC();
        Comment comment = new Comment("SAMPITTKO", "blockpuzzle", "super hra", new Date());
        commentService.addComment(comment);
        System.out.println(commentService.getComments("blockpuzzle").get(0));
    }
}
