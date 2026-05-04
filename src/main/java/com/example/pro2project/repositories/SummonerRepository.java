package com.example.pro2project.repositories;

import com.example.pro2project.models.Summoner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class SummonerRepository implements JpaRepository<Summoner, Long> {
    @Override
    public void flush() {

    }

    @Override
    public <S extends Summoner> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Summoner> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Summoner> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Summoner getOne(Long aLong) {
        return null;
    }

    @Override
    public Summoner getById(Long aLong) {
        return null;
    }

    @Override
    public Summoner getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Summoner> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Summoner> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Summoner> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Summoner> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Summoner> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Summoner> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Summoner, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Summoner> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Summoner> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Summoner> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Summoner> findAll() {
        return List.of();
    }

    @Override
    public List<Summoner> findAllById(Iterable<Long> longs) {
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
    public void delete(Summoner entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Summoner> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Summoner> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Summoner> findAll(Pageable pageable) {
        return null;
    }
}
