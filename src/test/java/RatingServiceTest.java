import org.junit.Test;
import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.service.RatingService;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class RatingServiceTest {
    protected RatingService ratingService;
    private final String GAME_NAME = "blockpuzzle";

    @Test
    public void testDbInit() throws Exception {
        assertEquals(0, ratingService.getAverageRating(GAME_NAME));
    }

    @Test
    public void testSetRating() throws Exception {
        Rating rating = new Rating("SAMPITTKO", GAME_NAME, 1, new Date());
        ratingService.setRating(rating);
        assertEquals(1, ratingService.getRating(GAME_NAME, "SAMPITTKO"));
    }

    @Test
    public void testGetAverageRating() throws Exception {
        Rating r1 = new Rating("Patrik", GAME_NAME, 5, new Date());
        Rating r2 = new Rating("Peter", GAME_NAME, 4, new Date());
        Rating r3 = new Rating("SAMPITTKO", GAME_NAME, 3, new Date());

        ratingService.setRating(r1);
        ratingService.setRating(r2);
        ratingService.setRating(r3);

        assertEquals(4, ratingService.getAverageRating(GAME_NAME));
    }

    @Test
    public void testGetRating() throws Exception {
        Rating rating = new Rating("SAMPITTKO", GAME_NAME, 3, new Date());
        ratingService.setRating(rating);
        assertEquals(3, ratingService.getRating(GAME_NAME, "SAMPITTKO"));
    }
}
