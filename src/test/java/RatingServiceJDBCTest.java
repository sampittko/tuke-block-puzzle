import org.junit.Before;
import org.junit.Test;
import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.service.JDBC;
import sk.tuke.gamestudio.server.service.RatingService;
import sk.tuke.gamestudio.server.service.RatingServiceJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;

public class RatingServiceJDBCTest extends RatingServiceTest {
    public static final String DELETE = "DELETE FROM rating";

    public RatingServiceJDBCTest() {
        super.ratingService = new RatingServiceJDBC();
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
    public void testSetRating() throws Exception {
        super.testSetRating();
    }

    @Test
    public void testGetAverageRating() throws Exception {
        super.testGetAverageRating();
    }

    @Test
    public void testGetRating() throws Exception {
        super.testGetRating();
    }

    public static void main(String[] args) throws Exception {
        RatingService ratingService = new RatingServiceJDBC();
        Rating rating = new Rating("SAMPITTKO", "blockpuzzle", 4, new Date());
        ratingService.setRating(rating);
        System.out.println(ratingService.getRating("blockpuzzle", "SAMPITTKO"));
    }
}
