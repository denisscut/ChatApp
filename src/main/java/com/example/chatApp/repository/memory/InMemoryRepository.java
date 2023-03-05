package com.example.chatApp.repository.memory;

import com.example.chatApp.domain.Entity;
import com.example.chatApp.repository.Repository;
import java.util.*;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
    Map<ID,E> entities;

    public InMemoryRepository() {
        entities=new HashMap<ID,E>();
    }

    @Override
    public E findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }

    public Iterable<ID> findAllIds() {
        return entities.keySet();
    }

    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        if(entities.get(entity.getId()) != null) {
            return entity;
        }
        else entities.put(entity.getId(),entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        return entities.remove(id);
    }

    @Override
    public E update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return entity;
        }
        return null;
    }
}
