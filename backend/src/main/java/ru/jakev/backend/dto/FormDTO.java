package ru.jakev.backend.dto;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
public class FormDTO {
    private long playerId;
    private String content;

    public FormDTO() {
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
