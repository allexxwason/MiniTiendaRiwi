package com.codeup.minitiendariwi.db;

import com.codeup.minitiendariwi.domain.Alimento;
import com.codeup.minitiendariwi.domain.Electrodomestico;
import com.codeup.minitiendariwi.domain.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // CAMBIO: Se agrega 'tipo' a las sentencias SQL
    private static final String INSERT_PRODUCTO = "INSERT INTO producto (nombre, precio, stock, tipo) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_PRODUCTOS = "SELECT id, nombre, precio, stock, tipo FROM producto";
    private static final String SELECT_BY_NAME = "SELECT id, nombre, precio, stock, tipo FROM producto WHERE nombre = ?";
    private static final String UPDATE_STOCK = "UPDATE producto SET stock = ? WHERE nombre = ? ";
    private static final String UPDATE_PRODUCTO = "UPDATE producto SET nombre = ?, precio = ? WHERE id = ?";
    private static final String DELETE_PRODUCTO = "DELETE FROM producto WHERE nombre = ?";


    /**
     * Guarda un nuevo producto en la base de datos, guardando el tipo.
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
            
            // Lógica para obtener el tipo de la subclase
            String tipo;
            if (producto instanceof Alimento) {
                tipo = "Alimento";
            } else if (producto instanceof Electrodomestico) {
                tipo = "Electrodomestico";
            } else {
                return false; 
            }
            
            ps.setString(4, tipo); // Establecer el tipo
            
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
     * Busca un producto por nombre y lo convierte a la subclase correcta.
     */
    public Producto findByName(String nombre) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Producto producto = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return null;

            ps = conn.prepareStatement(SELECT_BY_NAME);
            ps.setString(1, nombre);
            rs = ps.executeQuery();

            if (rs.next()) {
                producto = buildProducto(rs); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            DatabaseConnection.closeConnection(conn);
        }
        return producto;
    }
    
    /**
     * Obtiene todos los productos y los convierte a la subclase correcta.
     */
    public List<Producto> findAll() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Producto> productos = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return productos;

            ps = conn.prepareStatement(SELECT_ALL_PRODUCTOS);
            rs = ps.executeQuery();

            while (rs.next()) {
                Producto producto = buildProducto(rs); 
                if (producto != null) {
                    productos.add(producto);
                }
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
    
    // MÉTODO AUXILIAR: Construye la subclase correcta (Factory Pattern simple)
    private Producto buildProducto(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        double precio = rs.getDouble("precio");
        int stock = rs.getInt("stock");
        String tipo = rs.getString("tipo"); // Leemos la nueva columna 'tipo'

        switch (tipo) {
            case "Alimento":
                return new Alimento(id, nombre, precio, stock);
            case "Electrodomestico":
                return new Electrodomestico(id, nombre, precio, stock);
            default:
                // Manejo de error si hay un tipo de producto desconocido en la BD
                return null;
        }
    }
    
    // updateStock, update y deleteByName permanecen iguales
    
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
    
    public boolean update(Producto producto) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            ps = conn.prepareStatement(UPDATE_PRODUCTO);
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getId()); 

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