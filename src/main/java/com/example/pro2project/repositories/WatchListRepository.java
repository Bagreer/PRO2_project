package com.example.pro2project.repositories;

import com.example.pro2project.models.WatchList;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class WatchListRepository implements JpaRepository<WatchList, Long> {
    @Override
    public void flush() {

    }

    @Override
    public <S extends WatchList> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends WatchList> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<WatchList> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public WatchList getOne(Long aLong) {
        return null;
    }

    @Override
    public WatchList getById(Long aLong) {
        return null;
    }

    @Override
    public WatchList getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends WatchList> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends WatchList> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends WatchList> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends WatchList> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends WatchList> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends WatchList> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends WatchList, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends WatchList> S save(S entity) {
        return null;
    }

    @Override
    public <S extends WatchList> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<WatchList> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<WatchList> findAll() {
        return List.of();
    }

    @Override
    public List<WatchList> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(WatchList entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends WatchList> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<WatchList> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<WatchList> findAll(Pageable pageable) {
        return null;
    }
}
