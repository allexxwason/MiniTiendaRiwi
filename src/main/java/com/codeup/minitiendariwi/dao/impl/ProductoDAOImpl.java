package com.codeup.minitiendariwi.dao.impl;

import com.codeup.minitiendariwi.dao.ProductoDAO;
import com.codeup.minitiendariwi.db.DatabaseConnection;
import com.codeup.minitiendariwi.domain.Alimento;
import com.codeup.minitiendariwi.domain.Electrodomestico;
import com.codeup.minitiendariwi.domain.Producto;
import com.codeup.minitiendariwi.exceptions.PersistenciaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public void crear(Producto producto) throws PersistenciaException {
        String sql = "INSERT INTO productos(nombre, precio, stock, tipo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());

            String tipo = (producto instanceof Alimento) ? "Alimento" :
                          (producto instanceof Electrodomestico) ? "Electrodomestico" : "Desconocido";
            stmt.setString(4, tipo);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al insertar producto: " + e.getMessage());
        }
    }

    @Override
    public Producto buscarPorId(int id) throws PersistenciaException {
        String sql = "SELECT * FROM productos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProducto(rs);
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar producto: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Producto> buscarTodos() throws PersistenciaException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar productos: " + e.getMessage());
        }
        return productos;
    }

    @Override
    public void actualizar(Producto producto) throws PersistenciaException {
        String sql = "UPDATE productos SET nombre=?, precio=?, stock=?, tipo=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());

            String tipo = (producto instanceof Alimento) ? "Alimento" :
                          (producto instanceof Electrodomestico) ? "Electrodomestico" : "Desconocido";
            stmt.setString(4, tipo);

            stmt.setInt(5, producto.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar producto: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) throws PersistenciaException {
        String sql = "DELETE FROM productos WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar producto: " + e.getMessage());
        }
    }

    @Override
    public Producto buscarPorNombre(String nombre) throws PersistenciaException {
        String sql = "SELECT * FROM productos WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProducto(rs);
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar producto por nombre: " + e.getMessage());
        }
        return null;
    }

    /**
     * Convierte un ResultSet en el tipo de Producto correcto (Alimento o Electrodoméstico)
     */
    private Producto mapResultSetToProducto(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");

        if ("Alimento".equalsIgnoreCase(tipo)) {
            return new Alimento(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock"));
        } else if ("Electrodomestico".equalsIgnoreCase(tipo)) {
            return new Electrodomestico(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock"));
        } else {
            return new Producto(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock")) {
                @Override
                public String getDescripcion() {
                    return "Producto genérico sin tipo definido";
                }
            };
        }
    }

    // Métodos heredados no usados (no los borres por compatibilidad)
    @Override public boolean save(Producto nuevoProducto) { throw new UnsupportedOperationException(); }
    @Override public Producto findByName(String nombre) { throw new UnsupportedOperationException(); }
    @Override public List<Producto> findAll() { throw new UnsupportedOperationException(); }
    @Override public boolean updateStock(String nombre, int nuevoStock) { throw new UnsupportedOperationException(); }
    @Override public boolean update(Producto producto) { throw new UnsupportedOperationException(); }
    @Override public boolean deleteByName(String nombre) { throw new UnsupportedOperationException(); }
}
