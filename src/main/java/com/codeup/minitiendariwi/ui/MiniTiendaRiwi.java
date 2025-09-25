/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.codeup.minitiendariwi.ui;

import com.codeup.minitiendariwi.domain.inventario;
import javax.swing.JOptionPane;
import java.util.InputMismatchException;

/**
 * Clase principal de la aplicación.
 * <p>
 * Se encarga de la interacción con el usuario a través de JOptionPane
 * y de gestionar el flujo del programa.
 */

public class MiniTiendaRiwi {

    private inventario inventario;

    /**
     * Constructor para inicializar la aplicación.
     */
    public MiniTiendaRiwi() {
        this.inventario = new inventario();
    }

    /**
     * Muestra el menú principal y gestiona las opciones del usuario.
     */
    public void mostrarMenu() {
        String opcion = "";
        do {
            // Muestra el menú principal al usuario
            opcion = JOptionPane.showInputDialog(null,
                    "--- Menú de Inventario ---\n" +
                            "1. Agregar producto\n" +
                            "2. Listar inventario\n" +
                            "3. Comprar producto\n" +
                            "4. Mostrar estadísticas\n" +
                            "5. Buscar producto por nombre\n" +
                            "6. Salir",
                    "Mini Tienda Riwi",
                    JOptionPane.PLAIN_MESSAGE);

            // Maneja la opción seleccionada por el usuario
            if (opcion == null) {
                // Si el usuario presiona 'Cancelar' o cierra la ventana
                opcion = "6";
            }

            try {
                switch (opcion) {
                    case "1":
                        // Lógica para agregar un producto
                        agregarProducto();
                        break;
                    case "2":
                        // Lógica para listar el inventario
                        listarInventario();
                        break;
                    case "3":
                        // Lógica para comprar un producto
                        comprarProducto();
                        break;
                    case "4":
                        // Lógica para mostrar estadísticas
                        mostrarEstadisticas();
                        break;
                    case "5":
                        // Lógica para buscar un producto
                        buscarProducto();
                        break;
                    case "6":
                        // Lógica para salir
                        JOptionPane.showMessageDialog(null, "Saliendo del sistema.", "Adiós", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opción inválida. Por favor, intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, ingrese un número.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (!opcion.equals("6"));
    }

    
    private void agregarProducto() {
        try {
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto:");
            if (nombre == null || nombre.trim().isEmpty()) {
                return; // Si se cancela o no se ingresa nada
            }

            String precioStr = JOptionPane.showInputDialog("Ingrese el precio del producto:");
            if (precioStr == null || precioStr.trim().isEmpty()) {
                return;
            }
            double precio = Double.parseDouble(precioStr);

            String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad inicial:");
            if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
                return;
            }
            int cantidad = Integer.parseInt(cantidadStr);

            inventario.addProducto(nombre, precio, cantidad);
            JOptionPane.showMessageDialog(null, "Producto agregado correctamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El precio y la cantidad deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra la lista completa del inventario.
     */
    private void listarInventario() {
        StringBuilder sb = new StringBuilder("--- Inventario Actual ---\n");
        ArrayList<String> nombres = inventario.getNombres();
        double[] precios = inventario.getPrecios();
        HashMap<String, Integer> stock = inventario.getStock();

        if (nombres.isEmpty()) {
            sb.append("El inventario está vacío.");
        } else {
            for (int i = 0; i < nombres.size(); i++) {
                String nombre = nombres.get(i);
                sb.append("Producto: ").append(nombre).append("\n")
                  .append("  Precio: $").append(String.format("%.2f", precios[i])).append("\n")
                  .append("  Stock: ").append(stock.get(nombre)).append("\n")
                  .append("------------------------\n");
            }
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Inventario", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Lógica para comprar un producto.
     */
    private void comprarProducto() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto a comprar:");
        if (nombre == null || nombre.trim().isEmpty()) {
            return;
        }

        int index = inventario.indexOfNombre(nombre);
        if (index == -1) {
            JOptionPane.showMessageDialog(null, "Producto no encontrado.");
            return;
        }

        try {
            String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a comprar:");
            if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
                return;
            }
            int cantidad = Integer.parseInt(cantidadStr);

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor que cero.");
                return;
            }

            int stockActual = inventario.getStock().get(nombre);
            if (stockActual < cantidad) {
                JOptionPane.showMessageDialog(null, "Stock insuficiente. Cantidad disponible: " + stockActual);
            } else {
                inventario.getStock().put(nombre, stockActual - cantidad);
                JOptionPane.showMessageDialog(null, "Compra realizada con éxito. Nuevo stock de " + nombre + ": " + (stockActual - cantidad));
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lógica para mostrar estadísticas.
     */
    private void mostrarEstadisticas() {
        // Implementación pendiente para las siguientes tareas
        JOptionPane.showMessageDialog(null, "Funcionalidad de estadísticas en desarrollo.");
    }

    /**
     * Lógica para buscar un producto.
     */
    private void buscarProducto() {
        // Implementación pendiente para las siguientes tareas
        JOptionPane.showMessageDialog(null, "Funcionalidad de búsqueda en desarrollo.");
    }

    /**
     * Punto de entrada principal de la aplicación.
     */
    public static void main(String[] args) {
        MiniTiendaRiwi app = new MiniTiendaRiwi();
        app.mostrarMenu();
    }
}
