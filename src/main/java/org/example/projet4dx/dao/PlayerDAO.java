package org.example.projet4dx.dao;

import jakarta.persistence.EntityManager;
import org.example.projet4dx.model.Player;

import java.util.List;

public class PlayerDAO extends Dao<Player> {
    public PlayerDAO(EntityManager em) {
        super(em);
    }

    @Override
    public List<Player> getAll() {
        return this.em.createQuery("SELECT u FROM Player u", Player.class).getResultList();
    }

    public Player getByLogin(String login) {
        return this.em.createQuery("SELECT u FROM Player u WHERE u.login = :login", Player.class)
                .setParameter("login", login)
                .getSingleResult();
    }
}
