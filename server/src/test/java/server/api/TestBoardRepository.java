/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.api;

import commons.Board;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import server.database.BoardRepository;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestBoardRepository implements BoardRepository {

    public final List<Board> boards = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(final String name) {
        calledMethods.add(name);
    }

    @Override
    public List<Board> findAll() {
        call("findAll");
        return boards;
    }

    @Override
    public List<Board> findAll(final Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Board> findAllById(final Iterable<Long> ids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Board> List<S> saveAll(final Iterable<S> entities) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    @Override
    public <S extends Board> S saveAndFlush(final S entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Board> List<S> saveAllAndFlush(final Iterable<S> entities) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteAllInBatch(final Iterable<Board> entities) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAllByIdInBatch(final Iterable<Long> ids) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAllInBatch() {
        // TODO Auto-generated method stub

    }

    @Override
    public Board getOne(final Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Board getById(final Long id) {
        call("getById");
        return findById(id).get();
    }

    @Override
    public <S extends Board> List<S> findAll(final Example<S> example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Board> List<S> findAll(final Example<S> example, final Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Board> findAll(final Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Board> S save(final S entity) {
        call("save");
        entity.id = (long) boards.size();
        boards.add(entity);
        return entity;
    }

    @Override
    public Optional<Board> findById(final Long id) {
        return boards.stream().filter(q -> q.id == id).findFirst();
    }

    @Override
    public boolean existsById(final Long id) {
        call("existsById");
        return findById(id).isPresent();
    }

    @Override
    public long count() {
        return boards.size();
    }

    @Override
    public void deleteById(final Long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(final Board entity) {
        this.boards.remove(entity);
        this.calledMethods.add("delete");
    }

    @Override
    public void deleteAllById(final Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll(final Iterable<? extends Board> entities) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub

    }

    @Override
    public <S extends Board> Optional<S> findOne(final Example<S> example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Board> Page<S> findAll(final Example<S> example, final Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Board> long count(final Example<S> example) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public <S extends Board> boolean exists(final Example<S> example) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <S extends Board, R> R findBy(final Example<S> example,
                                         final Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        return null;
    }
}