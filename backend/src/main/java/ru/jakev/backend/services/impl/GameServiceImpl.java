package ru.jakev.backend.services.impl;

import org.springframework.stereotype.Service;
import ru.jakev.backend.entities.Game;
import ru.jakev.backend.repositories.GameRepository;
import ru.jakev.backend.services.GameService;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }


    //todo: change to dto
    @Override
    public Game getGame() {
        return gameRepository.findById(1L).orElse(null);
    }
}
