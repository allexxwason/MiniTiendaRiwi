package com.codeup.minitiendariwi.db;

import com.codeup.minitiendariwi.domain.Alimento;
import com.codeup.minitiendariwi.domain.Electrodomestico;
import com.codeup.minitiendariwi.domain.Producto;
import com.codeup.minitiendariwi.interfaces.Repositorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements Repositorio<Producto> {

    private static final String INSERT_PRODUCTO = "INSERT INTO producto (nombre, precio, stock, tipo) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_PRODUCTOS = "SELECT id, nombre, precio, stock, tipo FROM producto";
    private static final String SELECT_BY_ID = "SELECT id, nombre, precio, stock, tipo FROM producto WHERE id = ?";
    private static final String SELECT_BY_NAME = "SELECT id, nombre, precio, stock, tipo FROM producto WHERE nombre = ?";
    private static final String UPDATE_STOCK = "UPDATE producto SET stock = ? WHERE nombre = ? ";
    private static final String UPDATE_PRODUCTO = "UPDATE producto SET nombre = ?, precio = ? WHERE id = ?";
    private static final String DELETE_PRODUCTO = "DELETE FROM producto WHERE id = ?";

    // 🔹 Crear un nuevo producto
    @Override
    public boolean crear(Producto producto) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            ps = conn.prepareStatement(INSERT_PRODUCTO);
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());

            String tipo;
            if (producto instanceof Alimento) {
                tipo = "Alimento";
            } else if (producto instanceof Electrodomestico) {
                tipo = "Electrodomestico";
            } else {
                return false;
            }

            ps.setString(4, tipo);

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

    // 🔹 Buscar producto por ID
    @Override
    public Producto buscarPorId(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Producto producto = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return null;

            ps = conn.prepareStatement(SELECT_BY_ID);
            ps.setInt(1, id);
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

    // 🔹 Buscar todos los productos
    @Override
    public List<Producto> buscarTodos() {
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

    // 🔹 Actualizar producto (nombre y precio)
    @Override
    public boolean actualizar(Producto producto) {
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

    // 🔹 Eliminar producto por ID
    @Override
    public boolean eliminar(int id) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            ps = conn.prepareStatement(DELETE_PRODUCTO);
            ps.setInt(1, id);

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

    // 🔹 Buscar producto por nombre (para Inventario)
    public Producto buscarPorNombre(String nombre) {
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

    // 🔹 Actualizar stock por nombre (usado por Inventario)
    public boolean actualizarStock(String nombre, int nuevoStock) {
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

    // 🔹 Método auxiliar para crear subclases (Factory)
    private Producto buildProducto(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        double precio = rs.getDouble("precio");
        int stock = rs.getInt("stock");
        String tipo = rs.getString("tipo");

        switch (tipo) {
            case "Alimento":
                return new Alimento(id, nombre, precio, stock);
            case "Electrodomestico":
                return new Electrodomestico(id, nombre, precio, stock);
            default:
                return null;
        }
    }
}
