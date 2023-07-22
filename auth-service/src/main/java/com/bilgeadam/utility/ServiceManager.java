package com.bilgeadam.utility;

import com.bilgeadam.model.entity.Base;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public abstract class ServiceManager<T extends Base, ID> implements IService<T, ID>{
    private final JpaRepository<T, ID> repository;
    @Override
    public T save(T t) {
        return repository.save(t);
    }

    @Override
    public Iterable<T> saveAll(Iterable<T> t) {
        return repository.saveAll(t);
    }

    @Override
    public T update(T t) {
        return repository.save(t);
    }

    @Override
    public void delete(T t) {
        repository.delete(t);
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }
}
