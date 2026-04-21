/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author L E N O V O
 */
public class GenericRepository<T> {

    protected final Map<String, T> storage = new HashMap<>();

    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    public T getById(String id) {
        return storage.get(id);
    }

    public boolean exists(String id) {
        return storage.containsKey(id);
    }

    public void add(String id, T entity) {
        storage.put(id, entity);
    }

    public void update(String id, T entity) {
        storage.put(id, entity);
    }

    public void delete(String id) {
        storage.remove(id);
    }
}
