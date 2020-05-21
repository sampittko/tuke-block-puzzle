import org.junit.Test;
import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.service.CommentService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommentServiceTest {
    private final String GAME_NAME = "blockpuzzle";
    protected CommentService commentService;

    @Test
    public void testDbInit() throws Exception {
        assertEquals(0, commentService.getComments(GAME_NAME).size());
    }

    @Test
    public void testAddComment() throws Exception {
        Comment comment = new Comment("SAMPITTKO", GAME_NAME, "super, hra", new Date());
        commentService.addComment(comment);
        assertEquals(1, commentService.getComments(GAME_NAME).size());
    }

    @Test
    public void testGetComments() throws Exception {
        Comment c1 = new Comment("SAMPITTKO", GAME_NAME, "super, hra", new Date());
        Comment c2 = new Comment("Hrasko", GAME_NAME, "ja neviem us", new Date());

        commentService.addComment(c1);
        commentService.addComment(c2);

        List<Comment> comments = commentService.getComments(GAME_NAME);
        assertEquals(c1.getComment(), comments.get(1).getComment());
        assertEquals(c2.getComment(), comments.get(0).getComment());
    }
}
