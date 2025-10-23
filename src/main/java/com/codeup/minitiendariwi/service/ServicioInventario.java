package com.codeup.minitiendariwi.service;

import com.codeup.minitiendariwi.domain.Producto;
import java.util.List;

public interface ServicioInventario {
    boolean agregarProducto(Producto producto);
    boolean actualizarPrecio(int id, double nuevoPrecio);
    boolean actualizarStock(int id, int nuevoStock);
    boolean eliminarProducto(int id);
    List<Producto> buscarPorNombre(String nombre);
}
