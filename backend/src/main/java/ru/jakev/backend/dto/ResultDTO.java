package ru.jakev.backend.dto;

import java.util.Map;

/**
 * @author evotintsev
 * @since 04.03.2024
 */
public class ResultDTO {
    private Map<AccountDTO, Integer> results;

    public ResultDTO(Map<AccountDTO, Integer> results) {
        this.results = results;
    }

    public Map<AccountDTO, Integer> getResults() {
        return results;
    }

    public void setResults(Map<AccountDTO, Integer> results) {
        this.results = results;
    }
}
