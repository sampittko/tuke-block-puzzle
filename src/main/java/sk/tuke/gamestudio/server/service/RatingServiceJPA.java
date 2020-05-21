package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
        try {
            getRating(rating.getGame(), rating.getPlayer());
        }
        catch (NoResultException e) {
            entityManager.persist(rating);
            return;
        }
        entityManager.createNamedQuery("Rating.updateRating")
            .setParameter("rating", rating.getRating())
            .setParameter("ratedon", rating.getRatedon())
            .setParameter("game", rating.getGame())
            .setParameter("player", rating.getPlayer()).executeUpdate();
    }

    @Override
    public int getAverageRating(String game) {
        Double averageRating;
        try {
            averageRating = (double) entityManager.createNamedQuery("Rating.getAverageRating")
                    .setParameter("game", game).getSingleResult();
        }
        catch (NullPointerException e) {
            return 0;
        }
        return Math.round(averageRating.floatValue());
    }

    @Override
    public int getRating(String game, String player) {
        return ((Rating) entityManager.createNamedQuery("Rating.getRating")
                .setParameter("game", game).setParameter("player", player).getSingleResult()).getRating();
    }
}
