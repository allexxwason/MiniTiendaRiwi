package com.codeup.minitiendariwi.service.impl;

import com.codeup.minitiendariwi.dao.ProductoDAO;
import com.codeup.minitiendariwi.dao.impl.ProductoDAOImpl;
import com.codeup.minitiendariwi.domain.Producto;
import com.codeup.minitiendariwi.exceptions.DatoInvalidoException;
import com.codeup.minitiendariwi.exceptions.DuplicadoException;
import com.codeup.minitiendariwi.exceptions.PersistenciaException;
import com.codeup.minitiendariwi.service.InventarioServiceLocal;

import java.util.List;

/**
 * Capa de negocio (Service)
 * Aplica validaciones y coordina el flujo entre la UI y el DAO.
 * Lanza excepciones personalizadas cuando hay errores de validación o persistencia.
 */
public class InventarioServiceImpl implements InventarioServiceLocal {

    private final ProductoDAO productoDAO;

    public InventarioServiceImpl() {
        this.productoDAO = new ProductoDAOImpl();
    }

    @Override
    public void agregarProducto(Producto p) throws DatoInvalidoException, DuplicadoException, PersistenciaException {
        // Validaciones de negocio
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new DatoInvalidoException("❌ El nombre no puede estar vacío.");
        }
        if (p.getPrecio() <= 0) {
            throw new DatoInvalidoException("❌ El precio debe ser mayor que 0.");
        }
        if (p.getStock() < 0) {
            throw new DatoInvalidoException("❌ El stock no puede ser negativo.");
        }

        // Validar duplicados
        Producto existente = productoDAO.buscarPorNombre(p.getNombre());
        if (existente != null) {
            throw new DuplicadoException("⚠️ Ya existe un producto con el nombre '" + p.getNombre() + "'.");
        }

        // Insertar en BD
        productoDAO.crear(p);
    }

    @Override
    public List<Producto> listar() throws PersistenciaException {
        return productoDAO.buscarTodos();
    }

    @Override
    public void actualizar(Producto p) throws DatoInvalidoException, PersistenciaException {
        if (p.getPrecio() <= 0) {
            throw new DatoInvalidoException("❌ El precio debe ser positivo.");
        }
        if (p.getStock() < 0) {
            throw new DatoInvalidoException("❌ El stock no puede ser negativo.");
        }
        productoDAO.actualizar(p);
    }

    @Override
    public void eliminar(int id) throws PersistenciaException {
        productoDAO.eliminar(id);
    }

    @Override
    public Producto buscarPorNombre(String nombre) throws PersistenciaException {
        return productoDAO.buscarPorNombre(nombre);
    }
}
