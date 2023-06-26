package ru.jakev.backend.messages;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
public class ConfirmMessage {
    private boolean confirm;

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }
}
