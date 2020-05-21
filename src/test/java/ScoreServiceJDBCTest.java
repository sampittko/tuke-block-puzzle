import org.junit.Before;
import org.junit.Test;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.JDBC;
import sk.tuke.gamestudio.server.service.ScoreService;
import sk.tuke.gamestudio.server.service.ScoreServiceJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;

public class ScoreServiceJDBCTest extends ScoreServiceTest {
    public static final String DELETE = "DELETE FROM score";

    public ScoreServiceJDBCTest() {
        super.scoreService = new ScoreServiceJDBC();
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
    public void testAddScore() throws Exception {
        super.testAddScore();
    }

    @Test
    public void testGetBestScores() throws Exception {
        super.testGetBestScores();
    }

    public static void main(String[] args) {
        ScoreService scoreService = new ScoreServiceJDBC();
        Score score = new Score("blockpuzzle", "SAMPITTKO", 4, new Date());
        scoreService.addScore(score);
        System.out.println(scoreService.getBestScores("blockpuzzle").get(0));
    }
}
