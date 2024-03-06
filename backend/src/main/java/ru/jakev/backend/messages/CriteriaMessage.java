package ru.jakev.backend.messages;

import ru.jakev.backend.dto.CriteriaDTO;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
// example
//    {
//        "criteria" : {
//            "playersNumber" : 3,
//            "criteria" : "criteria",
//            "gameId" : 1
//        }
//    }
public class CriteriaMessage {

    private CriteriaDTO criteria;
    private NotificationMessageType type;

    public CriteriaMessage() {
        this.type = NotificationMessageType.CRITERIA_MESSAGE;
    }

    public CriteriaDTO getCriteria() {
        return criteria;
    }

    public void setCriteria(CriteriaDTO criteria) {
        this.criteria = criteria;
    }

    public NotificationMessageType getType() {
        return type;
    }

    public void setType(NotificationMessageType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CriteriaMessage{" +
                "criteria=" + criteria +
                '}';
    }
}
