package com.codeup.minitiendariwi.ui;

import com.codeup.minitiendariwi.domain.Alimento;
import com.codeup.minitiendariwi.domain.Electrodomestico;
import com.codeup.minitiendariwi.domain.Producto;
import com.codeup.minitiendariwi.service.impl.InventarioServiceImpl;
import com.codeup.minitiendariwi.exceptions.*;
import javax.swing.*;
import java.util.List;

/**
 * =============================
 *   CAPA DE PRESENTACIÓN (UI)
 * =============================
 * 
 * Interfaz principal de la aplicación Mini Tienda Riwi.
 * Se comunica ÚNICAMENTE con la capa de servicio (Service),
 * sin acceder directamente a la base de datos.
 * 
 * Aquí se gestionan:
 *  - Interacciones con el usuario (JOptionPane)
 *  - Llamadas a la capa de negocio (Service)
 *  - Manejo de excepciones personalizadas
 *  - Control del flujo general del programa
 */
public class MiniTiendaRiwi {

    private InventarioServiceImpl servicio;
    private double totalCompras;
    private JFrame parentFrame;

    public MiniTiendaRiwi() {
        this.servicio = new InventarioServiceImpl();
        this.totalCompras = 0.0;
        this.parentFrame = new JFrame();
        this.parentFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.parentFrame.setVisible(false);
    }

    /**
     * Menú principal de la aplicación.
     * Controla las opciones del usuario y gestiona el flujo del programa.
     */
    public void mostrarMenu() {
        String opcion = "";
        do {
            opcion = JOptionPane.showInputDialog(this.parentFrame,
                    "--- Menú de Inventario ---\n" +
                            "1. Agregar producto\n" +
                            "2. Listar inventario\n" +
                            "3. Comprar producto\n" +
                            "4. Mostrar estadísticas\n" +
                            "5. Buscar producto\n" +
                            "6. Actualizar producto\n" +
                            "7. Eliminar producto\n" +
                            "8. Salir",
                    "Mini Tienda Riwi",
                    JOptionPane.PLAIN_MESSAGE);

            if (opcion == null) opcion = "8";

            try {
                switch (opcion) {
                    case "1" -> agregarProducto();
                    case "2" -> listarInventario();
                    case "3" -> comprarProducto();
                    case "4" -> mostrarEstadisticas();
                    case "5" -> buscarProducto();
                    case "6" -> actualizarProducto();
                    case "7" -> eliminarProducto();
                    case "8" -> mostrarTicketFinal();
                    default -> JOptionPane.showMessageDialog(this.parentFrame, "Opción inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DatoInvalidoException | DuplicadoException e) {
                JOptionPane.showMessageDialog(this.parentFrame, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
            } catch (PersistenciaException e) {
                JOptionPane.showMessageDialog(this.parentFrame, e.getMessage(), "Error en base de datos", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this.parentFrame, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } while (!opcion.equals("8"));
    }

    /**
     * Opción 1: Agregar un nuevo producto al inventario.
     */
    private void agregarProducto() throws DatoInvalidoException, DuplicadoException, PersistenciaException {
        String tipoStr = JOptionPane.showInputDialog(this.parentFrame,
                "Seleccione tipo de producto:\n1. Alimento\n2. Electrodoméstico",
                "Tipo de Producto", JOptionPane.QUESTION_MESSAGE);
        if (tipoStr == null) return;

        String tipo = tipoStr.equals("1") ? "Alimento" : tipoStr.equals("2") ? "Electrodomestico" : null;
        if (tipo == null) {
            JOptionPane.showMessageDialog(this.parentFrame, "Tipo inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombre = JOptionPane.showInputDialog(this.parentFrame, "Nombre:");
        if (nombre == null || nombre.trim().isEmpty()) throw new DatoInvalidoException("El nombre no puede estar vacío.");

        double precio = getDoubleInput("Ingrese el precio del producto:");
        if (precio <= 0) throw new DatoInvalidoException("El precio debe ser mayor a 0.");

        int stock = getIntInput("Ingrese el stock inicial:");
        if (stock < 0) throw new DatoInvalidoException("El stock no puede ser negativo.");

        Producto nuevoProducto = tipo.equals("Alimento")
                ? new Alimento(nombre, precio, stock)
                : new Electrodomestico(nombre, precio, stock);

        servicio.agregarProducto(nuevoProducto);
        JOptionPane.showMessageDialog(this.parentFrame, "✅ Producto agregado correctamente.");
    }

    /**
     * Opción 2: Muestra el listado completo de productos registrados.
     */
    private void listarInventario() throws PersistenciaException {
        List<Producto> productos = servicio.listar();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this.parentFrame, "Inventario vacío.");
            return;
        }

        StringBuilder sb = new StringBuilder("--- Inventario ---\n");
        for (Producto p : productos) {
            sb.append(String.format("ID: %d\nNombre: %s\nPrecio: $%.2f\nStock: %d\n------------------\n",
                    p.getId(), p.getNombre(), p.getPrecio(), p.getStock()));
        }
        JOptionPane.showMessageDialog(this.parentFrame, sb.toString());
    }

    /**
     * Opción 3: Permite comprar productos restando stock.
     */
    private void comprarProducto() throws PersistenciaException, DatoInvalidoException {
        String nombre = JOptionPane.showInputDialog(this.parentFrame, "Nombre del producto a comprar:");
        if (nombre == null || nombre.isBlank()) return;

        Producto p = servicio.buscarPorNombre(nombre);
        if (p == null) {
            JOptionPane.showMessageDialog(this.parentFrame, "Producto no encontrado.");
            return;
        }

        int cantidad = getIntInput("Ingrese cantidad a comprar:");
        if (cantidad <= 0) throw new DatoInvalidoException("Cantidad inválida.");

        if (p.getStock() < cantidad) {
            JOptionPane.showMessageDialog(this.parentFrame, "Stock insuficiente. Disponible: " + p.getStock());
            return;
        }

        p.setStock(p.getStock() - cantidad);
        servicio.actualizar(p);
        totalCompras += p.getPrecio() * cantidad;

        JOptionPane.showMessageDialog(this.parentFrame, "Compra realizada con éxito.");
    }

    /**
     * Opción 4: Muestra estadísticas básicas (precio más alto y más bajo).
     */
    private void mostrarEstadisticas() throws PersistenciaException {
        List<Producto> productos = servicio.listar();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this.parentFrame, "No hay productos registrados.");
            return;
        }

        double max = productos.stream().mapToDouble(Producto::getPrecio).max().orElse(0);
        double min = productos.stream().mapToDouble(Producto::getPrecio).min().orElse(0);

        JOptionPane.showMessageDialog(this.parentFrame,
                String.format("💰 Precio más alto: $%.2f\n🪙 Precio más bajo: $%.2f", max, min));
    }

    /**
     * Opción 5: Buscar un producto específico por nombre.
     */
    private void buscarProducto() throws PersistenciaException {
        String nombre = JOptionPane.showInputDialog(this.parentFrame, "Nombre del producto a buscar:");
        if (nombre == null || nombre.isBlank()) return;

        Producto p = servicio.buscarPorNombre(nombre);
        if (p == null) {
            JOptionPane.showMessageDialog(this.parentFrame, "Producto no encontrado.");
            return;
        }

        JOptionPane.showMessageDialog(this.parentFrame,
                String.format("ID: %d\nNombre: %s\nPrecio: $%.2f\nStock: %d",
                        p.getId(), p.getNombre(), p.getPrecio(), p.getStock()));
    }

    /**
     * Opción 6: Actualiza precio y stock de un producto.
     */
    private void actualizarProducto() throws DatoInvalidoException, PersistenciaException {
        String nombre = JOptionPane.showInputDialog(this.parentFrame, "Nombre del producto a actualizar:");
        if (nombre == null || nombre.isBlank()) return;

        Producto p = servicio.buscarPorNombre(nombre);
        if (p == null) throw new DatoInvalidoException("Producto no encontrado.");

        double nuevoPrecio = getDoubleInput("Nuevo precio:");
        int nuevoStock = getIntInput("Nuevo stock:");
        if (nuevoPrecio <= 0 || nuevoStock < 0) throw new DatoInvalidoException("Datos inválidos.");

        p.setPrecio(nuevoPrecio);
        p.setStock(nuevoStock);
        servicio.actualizar(p);

        JOptionPane.showMessageDialog(this.parentFrame, "✅ Producto actualizado correctamente.");
    }

    /**
     * Opción 7: Elimina un producto del inventario.
     */
    private void eliminarProducto() throws PersistenciaException {
        String nombre = JOptionPane.showInputDialog(this.parentFrame, "Nombre del producto a eliminar:");
        if (nombre == null || nombre.isBlank()) return;

        Producto p = servicio.buscarPorNombre(nombre);
        if (p == null) {
            JOptionPane.showMessageDialog(this.parentFrame, "Producto no encontrado.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this.parentFrame,
                "¿Eliminar " + p.getNombre() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            servicio.eliminar(p.getId());
            JOptionPane.showMessageDialog(this.parentFrame, "🗑️ Producto eliminado correctamente.");
        }
    }

    /**
     * Opción 8: Muestra el resumen de la sesión (total gastado).
     */
    private void mostrarTicketFinal() {
        JOptionPane.showMessageDialog(this.parentFrame,
                String.format("Gracias por su compra.\nTotal gastado: $%.2f", totalCompras),
                "Resumen", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==== MÉTODOS AUXILIARES ====

    private int getIntInput(String msg) {
        try {
            String input = JOptionPane.showInputDialog(this.parentFrame, msg);
            return (input == null || input.isBlank()) ? -1 : Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this.parentFrame, "Ingrese un número válido.");
            return -1;
        }
    }

    private double getDoubleInput(String msg) {
        try {
            String input = JOptionPane.showInputDialog(this.parentFrame, msg);
            return (input == null || input.isBlank()) ? -1 : Double.parseDouble(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this.parentFrame, "Ingrese un valor válido.");
            return -1;
        }
    }

    // ==== MÉTODO PRINCIPAL ====
    public static void main(String[] args) {
        MiniTiendaRiwi app = new MiniTiendaRiwi();
        app.mostrarMenu();
        System.exit(0);
    }
}
