package com.codeup.minitiendariwi.db;

import com.codeup.minitiendariwi.domain.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private static final String INSERT_PRODUCTO = "INSERT INTO producto (nombre, precio, stock) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_PRODUCTOS = "SELECT id, nombre, precio, stock FROM producto";
    private static final String SELECT_BY_NAME = "SELECT id, nombre, precio, stock FROM producto WHERE nombre = ?";
    private static final String UPDATE_STOCK = "UPDATE producto SET stock = ? WHERE nombre = ?";
    // NUEVAS SENTENCIAS SQL:
    private static final String UPDATE_PRODUCTO = "UPDATE producto SET nombre = ?, precio = ? WHERE id = ?";
    private static final String DELETE_PRODUCTO = "DELETE FROM producto WHERE nombre = ?";


    /**
     * Guarda un nuevo producto en la base de datos.
     * @param producto El objeto Producto a guardar.
     * @return true si se guardó con éxito, false en caso contrario.
     */
    public boolean save(Producto producto) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            ps = conn.prepareStatement(INSERT_PRODUCTO);
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            DatabaseConnection.closeConnection(conn);
        }
    }

    /**
     * Obtiene todos los productos del inventario.
     * @return Una lista de objetos Producto.
     */
    public List<Producto> findAll() {
        List<Producto> productos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return productos;
            
            ps = conn.prepareStatement(SELECT_ALL_PRODUCTOS);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id"); 
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int stock = rs.getInt("stock");
                
                productos.add(new Producto(id, nombre, precio, stock));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            DatabaseConnection.closeConnection(conn);
        }
        return productos;
    }

    /**
     * Busca un producto por nombre.
     * @param nombre El nombre del producto a buscar.
     * @return El objeto Producto si se encuentra, o null.
     */
    public Producto findByName(String nombre) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return null;

            ps = conn.prepareStatement(SELECT_BY_NAME);
            ps.setString(1, nombre);
            rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id"); 
                String prodNombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int stock = rs.getInt("stock");
                
                return new Producto(id, prodNombre, precio, stock);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            DatabaseConnection.closeConnection(conn);
        }
        return null; // Producto no encontrado o error
    }

    /**
     * Actualiza solo el stock de un producto existente.
     * @param nombre El nombre del producto a actualizar.
     * @param nuevoStock La nueva cantidad de stock.
     * @return true si se actualizó con éxito, false en caso contrario.
     */
    public boolean updateStock(String nombre, int nuevoStock) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            ps = conn.prepareStatement(UPDATE_STOCK);
            ps.setInt(1, nuevoStock);
            ps.setString(2, nombre);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            DatabaseConnection.closeConnection(conn);
        }
    }

    // NUEVO MÉTODO: Actualiza nombre y precio de un producto
    /**
     * Actualiza el nombre y/o precio de un producto existente.
     * @param producto El objeto Producto con los nuevos datos (ID es necesario).
     * @return true si se actualizó con éxito, false en caso contrario.
     */
    public boolean update(Producto producto) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            ps = conn.prepareStatement(UPDATE_PRODUCTO);
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getId()); // Usamos el ID para asegurar la actualización

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            DatabaseConnection.closeConnection(conn);
        }
    }

    // NUEVO MÉTODO: Eliminar producto
    /**
     * Elimina un producto de la base de datos por su nombre.
     * @param nombre Nombre del producto a eliminar.
     * @return true si se eliminó con éxito, false en caso contrario.
     */
    public boolean deleteByName(String nombre) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            ps = conn.prepareStatement(DELETE_PRODUCTO);
            ps.setString(1, nombre);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            DatabaseConnection.closeConnection(conn);
        }
    }
}