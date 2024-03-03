package ru.jakev.backend.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.jakev.backend.entities.Role;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * @author evotintsev
 * @since 02.03.2024
 */
@Component
public class PhaseManager {
    //todo: add save to db to protect from restarts
    //todo: добавить защиту от преждевременной смены фазы(перескок через несколько фаз)?
    private final List<GamePhase> phaseSequence = Arrays.stream(GamePhase.values()).toList();
    private GamePhase phase = GamePhase.ROLES_DISTRIBUTION;

    private final static Logger LOG = LoggerFactory.getLogger(PhaseManager.class);

    public void changePhase(GamePhase gamePhase) {
        this.phase = gamePhase;
        LOG.info("Phase changed to: " + gamePhase.getPhase());
    }

    public GamePhase getCurrentPhase() {
        return phase;
    }

    //todo: по идее бы добавить акшоны и их связь с фазами
    public boolean isActionNotPermitted(GamePhase actionPhase) {
        boolean isPermitted = phase == actionPhase;
        if (!isPermitted) {
            LOG.info("Action is not permitted. Current phase: " + phase.getPhase() + " required phase: " + actionPhase.getPhase());
        }
        return !isPermitted;
    }

    public boolean isActionNotPermitted(EnumSet<GamePhase> phases) {
        boolean isPermitted = phases.contains(phase);
        if (!isPermitted) {
            LOG.info("Action is not permitted. Current phase: " + phase.getPhase() + " required phase is one of: {}",
                    phases);
        }
        return !isPermitted;
    }

    public Role getPermittedRoleByPhase() {
        return switch (phase) {
            case LUNCH_MAKING -> Role.WORKER;
            case LUNCH_EATING -> Role.PLAYER;
            default -> null;
        };
    }

    public void startNextPhase() {
        int currentPhaseIndex = phaseSequence.indexOf(phase);
        int nextPhaseIndex = currentPhaseIndex + 1;
        if (nextPhaseIndex < phaseSequence.size()) {
            changePhase(phaseSequence.get(nextPhaseIndex));
        } else {
            LOG.info("Game ended.");
        }
    }
}
