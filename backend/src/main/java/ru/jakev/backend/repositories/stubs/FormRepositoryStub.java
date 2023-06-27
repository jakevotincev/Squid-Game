package ru.jakev.backend.repositories.stubs;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.jakev.backend.entities.Form;
import ru.jakev.backend.repositories.FormRepository;

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
public class FormRepositoryStub implements FormRepository {

    private final Map<Long, Form> forms;
    private long idCounter = 0;

    {
        this.forms = new HashMap<>();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Form> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Form> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Form> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Form getOne(Long aLong) {
        return null;
    }

    @Override
    public Form getById(Long aLong) {
        return null;
    }

    @Override
    public Form getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Form> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Form> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Form> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Form> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Form> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Form> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Form, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Form> S save(S entity) {
        return (S) forms.put(++idCounter,entity);
    }

    @Override
    public <S extends Form> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Form> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Form> findAll() {
        return null;
    }

    @Override
    public List<Form> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return forms.size();
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Form entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Form> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Form> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Form> findAll(Pageable pageable) {
        return null;
    }
}
