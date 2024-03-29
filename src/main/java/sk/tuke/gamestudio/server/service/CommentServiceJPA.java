package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment) {
        entityManager.persist(comment);
    }

    @Override
    public List<Comment> getComments(String game) {
        return entityManager.createNamedQuery("Comment.getComments")
                .setParameter("game", game).setMaxResults(10).getResultList();
    }
}
