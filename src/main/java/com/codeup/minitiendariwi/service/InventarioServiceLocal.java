package com.codeup.minitiendariwi.service;

import com.codeup.minitiendariwi.domain.Producto;
import com.codeup.minitiendariwi.exceptions.DatoInvalidoException;
import com.codeup.minitiendariwi.exceptions.DuplicadoException;
import com.codeup.minitiendariwi.exceptions.PersistenciaException;
import java.util.List;

public interface InventarioServiceLocal {
    void agregarProducto(Producto p) throws DatoInvalidoException, DuplicadoException, PersistenciaException;
    List<Producto> listar() throws PersistenciaException;
    void actualizar(Producto p) throws DatoInvalidoException, PersistenciaException;
    void eliminar(int id) throws PersistenciaException;
    Producto buscarPorNombre(String nombre) throws PersistenciaException;
}
