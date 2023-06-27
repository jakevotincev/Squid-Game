package ru.jakev.backend.dto;

import lombok.NoArgsConstructor;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@NoArgsConstructor
//todo: make immutable
public class CriteriaDTO {
    private int playersNumber;
    private String criteria;
    private long gameId;

    public int getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }
}
