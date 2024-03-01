package ru.jakev.backend.messages;

import ru.jakev.backend.entities.Role;

/**
 * @author evotintsev
 * @since 22.02.2024
 */
public class RoleAssignedMessage {
    private final MessageType type;

    private final Role role;

    public RoleAssignedMessage(MessageType type, Role role) {
        this.type = type;
        this.role = role;
    }

    public MessageType getType() {
        return type;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "RoleAssignedMessage{" +
                "type=" + type +
                ", role=" + role +
                '}';
    }
}
