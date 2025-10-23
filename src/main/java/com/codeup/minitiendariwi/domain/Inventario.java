package com.codeup.minitiendariwi.domain;

import com.codeup.minitiendariwi.dao.ProductoDAO;
import com.codeup.minitiendariwi.exceptions.PersistenciaException;
import java.util.List;

/**
 * Clase que gestiona la lógica de negocio del inventario, 
 * comunicándose con la capa de datos (DAO).
 */
public class Inventario {

    private final ProductoDAO productoDAO;

    public Inventario() {
        this.productoDAO = new ProductoDAO() {
            @Override
            public void crear(Producto producto) throws PersistenciaException {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public Producto buscarPorId(int id) throws PersistenciaException {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public List<Producto> buscarTodos() throws PersistenciaException {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void actualizar(Producto producto) throws PersistenciaException {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void eliminar(int id) throws PersistenciaException {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public Producto buscarPorNombre(String nombre) throws PersistenciaException {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public boolean save(Producto nuevoProducto) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public Producto findByName(String nombre) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public List<Producto> findAll() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public boolean updateStock(String nombre, int nuevoStock) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public boolean update(Producto producto) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public boolean deleteByName(String nombre) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        };
    }

    // --- Métodos de CRUD (Create, Read, Update, Delete) ---
    
    // ACTUALIZADO: Acepta el objeto Producto ya creado (subclase)
    public boolean addProducto(Producto nuevoProducto) {
        return productoDAO.save(nuevoProducto);
    }

    public Producto getProductoPorNombre(String nombre) {
        return productoDAO.findByName(nombre);
    }
    
    public List<Producto> getTodosLosProductos() {
        return productoDAO.findAll();
    }
    
    public boolean actualizarStock(String nombre, int nuevoStock) {
        return productoDAO.updateStock(nombre, nuevoStock);
    }

    /**
     * Actualiza el nombre y precio de un producto.
     * @param producto Producto con ID y datos actualizados.
     * @return true si se actualizó con éxito.
     */
    public boolean updateProducto(Producto producto) {
        return productoDAO.update(producto);
    }
    
    /**
     * Elimina un producto del inventario.
     * @param nombre Nombre del producto a eliminar.
     * @return true si se eliminó con éxito.
     */
    public boolean deleteProducto(String nombre) {
        return productoDAO.deleteByName(nombre);
    }
    
    // --- Lógica adicional de negocio (para la UI) ---

    public double getPrecioMinimo() {
        List<Producto> productos = getTodosLosProductos();
        if (productos.isEmpty()) {
            return 0.0;
        }

        double min = Double.MAX_VALUE;
        for (Producto p : productos) {
            if (p.getPrecio() < min) {
                min = p.getPrecio();
            }
        }
        return min;
    }

    public double getPrecioMaximo() {
        List<Producto> productos = getTodosLosProductos();
        if (productos.isEmpty()) {
            return 0.0;
        }

        double max = Double.MIN_VALUE;
        for (Producto p : productos) {
            if (p.getPrecio() > max) {
                max = p.getPrecio();
            }
        }
        return max;
    }
    
    public boolean existeProducto(String nombre) {
        return getProductoPorNombre(nombre) != null;
    }
}