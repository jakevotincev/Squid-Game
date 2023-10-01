package ru.jakev.backend.messages;

/**
 * @author evotintsev
 * @since 02.10.2023
 */
public class KillPlayerMessage {
    private long playerId;
    private String playerName;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public String toString() {
        return "KillPlayerMessage{" +
                "playerId=" + playerId +
                ", playerName='" + playerName + '\'' +
                '}';
    }
}
