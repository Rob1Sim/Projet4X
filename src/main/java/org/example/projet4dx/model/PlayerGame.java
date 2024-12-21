package org.example.projet4dx.model;

import jakarta.persistence.*;

import java.io.Serializable;
@Entity
@Table(name="PlayerGame")
public class PlayerGame implements Serializable {


    @EmbeddedId
    private PlayerGameId id;

    @ManyToOne
    @MapsId("playerId")
    private Player player;

    @ManyToOne
    @MapsId("gameId")
    private Game game;

    @Column(nullable = false)
    private int score = 0;
    @Column(nullable = false)
    private int productionPoints = 0;

    public PlayerGame() {}

    public PlayerGame(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.id = new PlayerGameId(player.getId(), game.getId());
    }

    public PlayerGameId getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public int getProductionPoints() {
        return productionPoints;
    }

    public void setProductionPoints(int productionPoints) {
        this.productionPoints = productionPoints;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
