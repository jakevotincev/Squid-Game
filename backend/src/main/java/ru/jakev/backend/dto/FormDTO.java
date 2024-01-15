package ru.jakev.backend.dto;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public class FormDTO {
    private int playerId;
    private String content;

    public FormDTO() {
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "FormDTO{" +
                "playerId=" + playerId +
                ", content='" + content + '\'' +
                '}';
    }
}
