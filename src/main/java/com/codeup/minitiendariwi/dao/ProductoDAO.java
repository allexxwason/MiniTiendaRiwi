package com.codeup.minitiendariwi.dao;

import com.codeup.minitiendariwi.domain.Producto;
import com.codeup.minitiendariwi.exceptions.PersistenciaException;
import java.util.List;

public interface ProductoDAO {
    void crear(Producto producto) throws PersistenciaException;
    Producto buscarPorId(int id) throws PersistenciaException;
    List<Producto> buscarTodos() throws PersistenciaException;
    void actualizar(Producto producto) throws PersistenciaException;
    void eliminar(int id) throws PersistenciaException;
    Producto buscarPorNombre(String nombre) throws PersistenciaException;

    public boolean save(Producto nuevoProducto);

    public Producto findByName(String nombre);

    public List<Producto> findAll();

    public boolean updateStock(String nombre, int nuevoStock);

    public boolean update(Producto producto);

    public boolean deleteByName(String nombre);
}
