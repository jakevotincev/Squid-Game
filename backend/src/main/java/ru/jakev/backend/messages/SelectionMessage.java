package ru.jakev.backend.messages;

import ru.jakev.backend.dto.CriteriaDTO;
import ru.jakev.backend.dto.FormDTO;

import java.util.List;

/**
 * @author evotintsev
 * @since 25.09.2023
 */
public class SelectionMessage {
    private CriteriaDTO criteria;

    private List<FormDTO> forms;

    private int acceptedFormsCount;

    public CriteriaDTO getCriteria() {
        return criteria;
    }

    public void setCriteria(CriteriaDTO criteria) {
        this.criteria = criteria;
    }

    public List<FormDTO> getForms() {
        return forms;
    }

    public void setForms(List<FormDTO> forms) {
        this.forms = forms;
    }

    public int getAcceptedFormsCount() {
        return acceptedFormsCount;
    }

    public void setAcceptedFormsCount(int acceptedFormsCount) {
        this.acceptedFormsCount = acceptedFormsCount;
    }

    @Override
    public String toString() {
        return "SelectionMessage{" +
                "criteria=" + criteria +
                ", forms=" + forms +
                ", acceptedFormsCount=" + acceptedFormsCount +
                '}';
    }
}
