package org.example.projet4dx.dao;

import jakarta.persistence.EntityManager;
import org.example.projet4dx.model.PlayerGame;

import java.util.List;

public class PlayerGameDao extends Dao<PlayerGame> {
    public PlayerGameDao(EntityManager em) {
        super(em);
    }

    @Override
    public List<PlayerGame> getAll() {
        return em.createQuery("SELECT pg from PlayerGame pg", PlayerGame.class).getResultList();
    }

    public List<PlayerGame> getByPlayerId(long id) {
        return em.createQuery(
                     "SELECT pg " +
                        "FROM PlayerGame pg " +
                        "where pg.id.playerId = :playerId",
                PlayerGame.class)
                .setParameter("playerId", id)
                .getResultList();
    }

    public List<PlayerGame> getByGameId(long id) {
        return em.createQuery(
                        "SELECT pg " +
                                "FROM PlayerGame pg " +
                                "where pg.id.gameId = :gameId",
                        PlayerGame.class)
                .setParameter("gameId", id)
                .getResultList();
    }
}
