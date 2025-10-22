package com.codeup.minitiendariwi.domain;

import com.codeup.minitiendariwi.db.ProductoDAO;
import com.codeup.minitiendariwi.interfaces.Repositorio;
import java.util.List;

/**
 * Clase que gestiona la lógica de negocio del inventario, 
 * comunicándose con la capa de datos mediante la interfaz Repositorio.
 */
public class Inventario {

    private Repositorio<Producto> repositorio;

    public Inventario() {
        // Desacoplamiento: se puede cambiar la implementación sin tocar la lógica
        this.repositorio = new ProductoDAO();
    }

    // --- Métodos de CRUD (Create, Read, Update, Delete) ---

    public boolean addProducto(Producto nuevoProducto) {
        return repositorio.crear(nuevoProducto);
    }

    public Producto getProductoPorId(int id) {
        return repositorio.buscarPorId(id);
    }

    public Producto getProductoPorNombre(String nombre) {
        // Este método no está en la interfaz, pero el DAO lo tiene como extra
        if (repositorio instanceof ProductoDAO dao) {
            return dao.buscarPorNombre(nombre);
        }
        return null;
    }

    public List<Producto> getTodosLosProductos() {
        return repositorio.buscarTodos();
    }

    public boolean actualizarStock(String nombre, int nuevoStock) {
        // Este método también usa una función específica del DAO
        if (repositorio instanceof ProductoDAO dao) {
            return dao.actualizarStock(nombre, nuevoStock);
        }
        return false;
    }

    public boolean updateProducto(Producto producto) {
        return repositorio.actualizar(producto);
    }

    public boolean deleteProducto(int id) {
        return repositorio.eliminar(id);
    }

    public boolean deleteProducto(String nombre) {
        if (repositorio instanceof ProductoDAO dao) {
            Producto p = dao.buscarPorNombre(nombre);
            if (p != null) {
                return dao.eliminar(p.getId());
            }
        }
        return false;
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
