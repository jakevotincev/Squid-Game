package ru.jakev.backend.repositories.stubs;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.jakev.backend.entities.Game;
import ru.jakev.backend.repositories.GameRepository;

import java.util.*;
import java.util.function.Function;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Component
public class GameRepositoryStub implements GameRepository {
    private final Map<Long, Game> games;

    {
        games = new HashMap<>();
        games.put(1L, new Game(1L, null));
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Game> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Game> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Game> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Game getOne(Long aLong) {
        return null;
    }

    @Override
    public Game getById(Long aLong) {
        return null;
    }

    @Override
    public Game getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Game> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Game> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Game> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Game> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Game> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Game> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Game, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Game> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Game> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Game> findById(Long aLong) {
        return Optional.ofNullable(games.get(aLong));
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Game> findAll() {
        return null;
    }

    @Override
    public List<Game> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Game entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Game> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Game> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Game> findAll(Pageable pageable) {
        return null;
    }
}
