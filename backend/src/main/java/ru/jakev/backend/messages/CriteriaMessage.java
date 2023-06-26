package ru.jakev.backend.messages;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
public class CriteriaMessage {
    private int playersNumber;
    private String criteria;

    public CriteriaMessage(int playersNumber, String criteria) {
        this.playersNumber = playersNumber;
        this.criteria = criteria;
    }

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
}
