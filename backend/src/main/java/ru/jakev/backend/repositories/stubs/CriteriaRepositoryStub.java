package ru.jakev.backend.repositories.stubs;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;
import ru.jakev.backend.entities.Criteria;
import ru.jakev.backend.repositories.CriteriaRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author evotintsev
 * @since 27.06.2023
 */
@Component
public class CriteriaRepositoryStub implements CriteriaRepository {

    private final Map<Long, Criteria> criteria;
    private long idCounter = 0;

    {
        this.criteria = new HashMap<>();
    }


    @Override
    public void flush() {

    }

    @Override
    public <S extends Criteria> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Criteria> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Criteria> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Criteria getOne(Long aLong) {
        return null;
    }

    @Override
    public Criteria getById(Long aLong) {
        return null;
    }

    @Override
    public Criteria getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Criteria> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Criteria> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Criteria> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Criteria> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Criteria> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Criteria> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Criteria, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Criteria> S save(S entity) {
        return (S) criteria.put(++idCounter, entity);
    }

    @Override
    public <S extends Criteria> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Criteria> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Criteria> findAll() {
        return null;
    }

    @Override
    public List<Criteria> findAllById(Iterable<Long> longs) {
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
    public void delete(Criteria entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Criteria> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Criteria> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Criteria> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Criteria> findByGameId(Long id) {
        return Optional.of(criteria.get(1L));
    }
}
