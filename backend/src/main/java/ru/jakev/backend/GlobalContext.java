package ru.jakev.backend;

import org.springframework.stereotype.Component;
import ru.jakev.backend.services.CriteriaService;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Component
public class GlobalContext {
    private Integer playersCount;

    private final CriteriaService criteriaService;

    public GlobalContext(CriteriaService criteriaService) {
        this.criteriaService = criteriaService;
    }

    public Integer getPlayersNumber() {
        if (playersCount == null) {
            playersCount = criteriaService.getPayersNumber();
        }
        return playersCount;
    }
}
