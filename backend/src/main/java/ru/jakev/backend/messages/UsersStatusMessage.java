package ru.jakev.backend.messages;

import ru.jakev.backend.dto.AccountDTO;

import java.util.List;

/**
 * @author evotintsev
 * @since 21.02.2024
 */
public class UsersStatusMessage {
    private final MessageType type;
    private final List<AccountDTO> connectedUsers;

    public UsersStatusMessage(List<AccountDTO> connectedUsers) {
        this.connectedUsers = connectedUsers;
        type = MessageType.USERS_STATUS_MESSAGE;
    }

    public MessageType getType() {
        return type;
    }


    public List<AccountDTO> getConnectedUsers() {
        return connectedUsers;
    }

    @Override
    public String toString() {
        return "UsersStatusMessage{" +
                "type=" + type +
                ", connectedUsers=" + connectedUsers +
                '}';
    }
}
