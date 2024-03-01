package ru.jakev.backend.messages;

/**
 * @author evotintsev
 * @since 02.10.2023
 */
public class KillPlayerMessage {
    private int playerId;
    private String playerName;
    private int score;
    private int soldierId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
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

    public int getSoldierId() {
        return soldierId;
    }

    public void setSoldierId(int solierId) {
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
