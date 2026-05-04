package com.example.pro2project.repositories;

import com.example.pro2project.models.AnalysisReport;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AnalysisReportRepository implements JpaRepository<AnalysisReport, Long> {
    @Override
    public void flush() {

    }

    @Override
    public <S extends AnalysisReport> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends AnalysisReport> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<AnalysisReport> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public AnalysisReport getOne(Long aLong) {
        return null;
    }

    @Override
    public AnalysisReport getById(Long aLong) {
        return null;
    }

    @Override
    public AnalysisReport getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends AnalysisReport> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends AnalysisReport> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends AnalysisReport> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends AnalysisReport> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AnalysisReport> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends AnalysisReport> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends AnalysisReport, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends AnalysisReport> S save(S entity) {
        return null;
    }

    @Override
    public <S extends AnalysisReport> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<AnalysisReport> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<AnalysisReport> findAll() {
        return List.of();
    }

    @Override
    public List<AnalysisReport> findAllById(Iterable<Long> longs) {
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
    public void delete(AnalysisReport entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends AnalysisReport> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<AnalysisReport> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<AnalysisReport> findAll(Pageable pageable) {
        return null;
    }
}
