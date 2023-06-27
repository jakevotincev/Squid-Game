package ru.jakev.backend.messages;

import ru.jakev.backend.dto.CriteriaDTO;

/**
 * @author evotintsev
 * @since 26.06.2023
 */
public class CriteriaMessage {
    private CriteriaDTO criteria;

    public CriteriaDTO getCriteria() {
        return criteria;
    }

    public void setCriteria(CriteriaDTO criteria) {
        this.criteria = criteria;
    }
}
