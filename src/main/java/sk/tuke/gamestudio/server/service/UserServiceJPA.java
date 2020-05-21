package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class UserServiceJPA implements UserService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User login(String username, String passwd) {
        try {
            return (User) entityManager.createNamedQuery("User.login")
                    .setParameter("username", username).setParameter("passwd", passwd).getSingleResult();
        }
        catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public User register(String username, String passwd) {
        User user = new User(username, passwd);
        entityManager.persist(user);
        return user;
    }

    @Override
    public User checkRegistration(String username) {
        try {
            return (User) entityManager.createNamedQuery("User.check")
                    .setParameter("username", username).getSingleResult();
        }
        catch(NoResultException e) {
            return null;
        }
    }
}