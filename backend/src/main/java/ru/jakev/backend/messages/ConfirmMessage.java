package ru.jakev.backend.messages;

import ru.jakev.backend.dto.CriteriaDTO;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
//    example
//    {
//        "confirm" : true,
//        "criteria" : {
//            "playersNumber" : 3,
//            "criteria" : "criteria",
//            "gameId" : 1
//        },
//        "declineReason" : "declineReason"
//    }
public class ConfirmMessage {
    private boolean confirm;

    private CriteriaDTO criteria;
    private String declineReason;

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
    }

    public CriteriaDTO getCriteria() {
        return criteria;
    }

    public void setCriteria(CriteriaDTO criteria) {
        this.criteria = criteria;
    }

    @Override
    public String toString() {
        return "ConfirmMessage{" +
                "confirm=" + confirm +
                ", criteria=" + criteria +
                ", declineReason='" + declineReason + '\'' +
                '}';
    }
}
