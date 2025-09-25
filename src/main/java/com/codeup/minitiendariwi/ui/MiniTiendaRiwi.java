/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.codeup.minitiendariwi.ui;

import com.codeup.minitiendariwi.domain.Inventario;
import javax.swing.JOptionPane;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase principal de la aplicación.
 * Gestiona la interacción con el usuario a través de JOptionPane.
 */
public class MiniTiendaRiwi {

    private Inventario inventario;
    private double totalCompras;

    /**
     * Constructor para inicializar la aplicación y las estructuras de datos.
     */
    public MiniTiendaRiwi() {
        this.inventario = new Inventario();
        this.totalCompras = 0.0;
    }

    /**
     * Muestra el menú principal y maneja el bucle de la aplicación.
     * La ejecución se repite hasta que el usuario elija "Salir".
     */
    public void mostrarMenu() {
        String opcion = "";
        do {
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

            if (opcion == null) {
                opcion = "6"; // Trata el cierre de la ventana como la opción de Salir.
            }

            try {
                switch (opcion) {
                    case "1":
                        agregarProducto();
                        break;
                    case "2":
                        listarInventario();
                        break;
                    case "3":
                        comprarProducto();
                        break;
                    case "4":
                        mostrarEstadisticas();
                        break;
                    case "5":
                        buscarProducto();
                        break;
                    case "6":
                        mostrarTicketFinal();
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

    /**
     * Permite al usuario agregar un nuevo producto al inventario.
     * Valida que el producto no exista y que las entradas sean válidas.
     */
    private void agregarProducto() {
        try {
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto:");
            if (nombre == null || nombre.trim().isEmpty()) {
                return;
            }

            // Evita productos duplicados.
            if (inventario.indexOfNombre(nombre) != -1) {
                JOptionPane.showMessageDialog(null, "Este producto ya existe en el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double precio = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el precio del producto:"));
            int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad inicial:"));

            inventario.addProducto(nombre, precio, cantidad);
            JOptionPane.showMessageDialog(null, "Producto agregado correctamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El precio y la cantidad deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra la lista completa de productos, incluyendo nombre, precio y stock.
     */
    private void listarInventario() {
        StringBuilder sb = new StringBuilder("--- Inventario Actual ---\n");
        ArrayList<String> nombres = inventario.getNombres();
        double[] precios = inventario.getPrecios();
        HashMap<String, Integer> stock = inventario.getStock();

        if (nombres.isEmpty()) {
            sb.append("El inventario está vacío.");
        } else {
            for (int i = 0; i < inventario.getPreciosSize(); i++) {
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
     * Procesa la compra de un producto, valida el stock y actualiza las estructuras de datos.
     * El total de la compra se acumula.
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
            int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad a comprar:"));

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor que cero.");
                return;
            }

            int stockActual = inventario.getStock().get(nombre);
            if (stockActual < cantidad) {
                JOptionPane.showMessageDialog(null, "Stock insuficiente. Cantidad disponible: " + stockActual);
            } else {
                inventario.getStock().put(nombre, stockActual - cantidad);
                double precioProducto = inventario.getPrecios()[index];
                totalCompras += precioProducto * cantidad;
                JOptionPane.showMessageDialog(null, "Compra realizada con éxito. Nuevo stock de " + nombre + ": " + (stockActual - cantidad));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Encuentra y muestra el precio mínimo y máximo del inventario.
     */
    private void mostrarEstadisticas() {
        if (inventario.getNombres().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El inventario está vacío. No hay estadísticas para mostrar.");
            return;
        }

        double[] precios = inventario.getPrecios();
        double precioMax = precios[0];
        double precioMin = precios[0];

        // Recorre el array para encontrar los valores extremos.
        for (int i = 1; i < inventario.getPreciosSize(); i++) {
            if (precios[i] > precioMax) {
                precioMax = precios[i];
            }
            if (precios[i] < precioMin) {
                precioMin = precios[i];
            }
        }

        String mensaje = String.format("--- Estadísticas de Precios ---\n" +
                                     "Precio más barato: $%.2f\n" +
                                     "Precio más caro: $%.2f", precioMin, precioMax);

        JOptionPane.showMessageDialog(null, mensaje, "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Busca productos por coincidencias parciales en el nombre (insensible a mayúsculas/minúsculas).
     */
    private void buscarProducto() {
        String busqueda = JOptionPane.showInputDialog("Ingrese el nombre o parte del nombre del producto a buscar:");
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return;
        }

        StringBuilder resultados = new StringBuilder("--- Resultados de Búsqueda ---\n");
        boolean encontrado = false;

        for (int i = 0; i < inventario.getNombres().size(); i++) {
            String nombreProducto = inventario.getNombres().get(i);
            // Compara las cadenas sin importar mayúsculas/minúsculas.
            if (nombreProducto.toLowerCase().contains(busqueda.toLowerCase())) {
                resultados.append("Producto: ").append(nombreProducto).append("\n")
                          .append("  Precio: $").append(String.format("%.2f", inventario.getPrecios()[i])).append("\n")
                          .append("  Stock: ").append(inventario.getStock().get(nombreProducto)).append("\n")
                          .append("------------------------\n");
                encontrado = true;
            }
        }

        if (!encontrado) {
            resultados.append("No se encontraron productos que coincidan con la búsqueda.");
        }

        JOptionPane.showMessageDialog(null, resultados.toString(), "Buscar Producto", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Muestra el total acumulado de compras de la sesión al salir de la aplicación.
     */
    private void mostrarTicketFinal() {
        String mensaje = String.format("Gracias por tu visita.\n" +
                                     "Total de compras en esta sesión: $%.2f", totalCompras);
        JOptionPane.showMessageDialog(null, mensaje, "Ticket Final", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Punto de entrada principal de la aplicación.
     */
    public static void main(String[] args) {
        MiniTiendaRiwi app = new MiniTiendaRiwi();
        app.mostrarMenu();
    }
}
