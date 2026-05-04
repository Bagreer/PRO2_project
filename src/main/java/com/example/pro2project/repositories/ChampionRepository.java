package com.example.pro2project.repositories;

import com.example.pro2project.models.Champion;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ChampionRepository implements JpaRepository<Champion, Long> {
    @Override
    public void flush() {

    }

    @Override
    public <S extends Champion> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Champion> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Champion> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Champion getOne(Long aLong) {
        return null;
    }

    @Override
    public Champion getById(Long aLong) {
        return null;
    }

    @Override
    public Champion getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Champion> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Champion> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Champion> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Champion> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Champion> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Champion> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Champion, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Champion> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Champion> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Champion> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Champion> findAll() {
        return List.of();
    }

    @Override
    public List<Champion> findAllById(Iterable<Long> longs) {
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
    public void delete(Champion entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Champion> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Champion> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Champion> findAll(Pageable pageable) {
        return null;
    }
}
