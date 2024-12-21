package org.example.projet4dx.model.dao;

import jakarta.persistence.EntityManager;
import org.example.projet4dx.model.Game;

import java.util.List;

public class GameDAO extends Dao<Game> {
    public GameDAO(EntityManager em) {
        super(em);
    }

    @Override
    public List<Game> getAll() {
        return List.of();
    }
}
