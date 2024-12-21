package org.example.projet4dx.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a composite key for Player and Game entities.
 */
@Embeddable
public class PlayerGameId implements Serializable {
    private long playerId;
    private long gameId;

    public PlayerGameId() {}

    public PlayerGameId(long playerId, long gameId) {
        this.playerId = playerId;
        this.gameId = gameId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public long getGameId() {
        return gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerGameId that = (PlayerGameId) o;
        return playerId == that.playerId && gameId == that.gameId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, gameId);
    }
}
