package ru.jakev.backend.messages;

/**
 * @author evotintsev
 * @since 02.10.2023
 */
public class KillPlayerMessage {
    private long playerId;
    private String playerName;
    private int score;
    private long soldierId;

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getSoldierId() {
        return soldierId;
    }

    public void setSoldierId(long solierId) {
        this.soldierId = solierId;
    }

    @Override
    public String toString() {
        return "KillPlayerMessage{" +
                "playerId=" + playerId +
                ", playerName='" + playerName + '\'' +
                ", score=" + score +
                ", soldierId=" + soldierId +
                '}';
    }
}
