package com.codeup.minitiendariwi.service;

import java.util.List;

public interface Repositorio<T> {
    boolean crear(T t);
    T buscarPorId(int id);
    List<T> buscarTodos();
    boolean actualizar(T t);
    boolean eliminar(int id);
}
