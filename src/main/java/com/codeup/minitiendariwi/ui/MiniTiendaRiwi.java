package com.codeup.minitiendariwi.ui;

import com.codeup.minitiendariwi.domain.Inventario;
import com.codeup.minitiendariwi.domain.Producto;
import javax.swing.JOptionPane;
import java.util.List;

/**
 * Clase principal de la aplicación.
 * Gestiona la interacción con el usuario a través de JOptionPane.
 */
public class MiniTiendaRiwi {

    private Inventario inventario;
    private double totalCompras;

    public MiniTiendaRiwi() {
        this.inventario = new Inventario();
        this.totalCompras = 0.0;
    }

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
                            "6. Actualizar stock \n" +
                            "7. Actualizar nombre\n" +
                            "8. Eliminar producto\n" +
                            "9. Salir",
                    "Mini Tienda Riwi",
                    JOptionPane.PLAIN_MESSAGE);

            if (opcion == null) {
                opcion = "9";
            }

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
                    actualizarStock(); // Renombrado para que se entienda mejor la diferencia
                    break;
                case "7":
                    actualizarProducto(); // NUEVO MÉTODO
                    break;
                case "8":
                    eliminarProducto(); // NUEVO MÉTODO
                    break;
                case "9":
                    mostrarTicketFinal();
                    JOptionPane.showMessageDialog(null, "Saliendo del sistema.", "Adiós", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } while (!opcion.equals("9"));
    }
    
    // --- Métodos de Entrada de Usuario (sin cambios, pero listados por comodidad) ---

    private String getStringInput(String mensaje) {
        String input = JOptionPane.showInputDialog(mensaje);
        return (input == null || input.trim().isEmpty()) ? null : input.trim();
    }

    private int getIntInput(String mensaje) {
        String input = JOptionPane.showInputDialog(mensaje);
        if (input == null || input.trim().isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Ingrese un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    private double getDoubleInput(String mensaje) {
        String input = JOptionPane.showInputDialog(mensaje);
        if (input == null || input.trim().isEmpty()) {
            return -1.0;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1.0;
        }
    }
    
    // --- Lógica de la Aplicación (Usando el Inventario persistente) ---

    private void agregarProducto() {
        String nombre = getStringInput("Ingrese el nombre del producto:");
        if (nombre == null) return;

        if (inventario.existeProducto(nombre)) {
            JOptionPane.showMessageDialog(null, "Este producto ya existe en el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precio = getDoubleInput("Ingrese el precio del producto:");
        if (precio == -1.0 || precio <= 0) {
            JOptionPane.showMessageDialog(null, "El precio debe ser positivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad = getIntInput("Ingrese la cantidad inicial:");
        if (cantidad == -1 || cantidad < 0) {
             JOptionPane.showMessageDialog(null, "La cantidad inicial no puede ser negativa.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (inventario.addProducto(nombre, precio, cantidad)) {
            JOptionPane.showMessageDialog(null, "Producto agregado correctamente a la base de datos.");
        } else {
             JOptionPane.showMessageDialog(null, "Error al agregar el producto a la BD. Revise la consola.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void listarInventario() {
        StringBuilder sb = new StringBuilder("--- Inventario Actual ---\n");
        List<Producto> productos = inventario.getTodosLosProductos();

        if (productos.isEmpty()) {
            sb.append("El inventario está vacío.");
        } else {
            for (Producto producto : productos) {
                sb.append("ID: ").append(producto.getId()).append("\n") // Mostramos el ID
                  .append("  Producto: ").append(producto.getNombre()).append("\n")
                  .append("  Precio: $").append(String.format("%.2f", producto.getPrecio())).append("\n")
                  .append("  Stock: ").append(producto.getStock()).append("\n")
                  .append("------------------------\n");
            }
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Inventario", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void comprarProducto() {
        String nombre = getStringInput("Ingrese el nombre del producto a comprar:");
        if (nombre == null) return;

        Producto producto = inventario.getProductoPorNombre(nombre);
        
        if (producto == null) {
            JOptionPane.showMessageDialog(null, "Producto no encontrado.");
            return;
        }

        int cantidad = getIntInput("Ingrese la cantidad a comprar:");
        if (cantidad == -1 || cantidad <= 0) {
            JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor que cero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stockActual = producto.getStock();
        if (stockActual < cantidad) {
            JOptionPane.showMessageDialog(null, "Stock insuficiente. Cantidad disponible: " + stockActual);
        } else {
            int nuevoStock = stockActual - cantidad;
            
            if (inventario.actualizarStock(nombre, nuevoStock)) {
                double precioProducto = producto.getPrecio();
                totalCompras += precioProducto * cantidad;
                JOptionPane.showMessageDialog(null, "Compra realizada con éxito. Nuevo stock de " + nombre + ": " + nuevoStock);
            } else {
                 JOptionPane.showMessageDialog(null, "Error al actualizar el stock en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void actualizarStock() {
        String nombre = getStringInput("Ingrese el nombre del producto para actualizar stock:");
        if (nombre == null) return;

        if (!inventario.existeProducto(nombre)) {
            JOptionPane.showMessageDialog(null, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int nuevoStock = getIntInput("Ingrese el NUEVO stock total (ej: 50):");
        if (nuevoStock == -1 || nuevoStock < 0) {
            JOptionPane.showMessageDialog(null, "Stock no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (inventario.actualizarStock(nombre, nuevoStock)) {
            JOptionPane.showMessageDialog(null, "Stock de " + nombre + " actualizado a: " + nuevoStock);
        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar stock en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // NUEVO MÉTODO: Lógica para actualizar nombre y precio
    private void actualizarProducto() {
        String nombreActual = getStringInput("Ingrese el nombre del producto a actualizar:");
        if (nombreActual == null) return;

        Producto producto = inventario.getProductoPorNombre(nombreActual);
        if (producto == null) {
            JOptionPane.showMessageDialog(null, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevoNombre = getStringInput("Ingrese el nuevo nombre (" + producto.getNombre() + "):");
        double nuevoPrecio = getDoubleInput("Ingrese el nuevo precio (" + producto.getPrecio() + "):");

        if (nuevoNombre == null || nuevoPrecio == -1.0) {
             JOptionPane.showMessageDialog(null, "Operación cancelada o datos inválidos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (nuevoPrecio <= 0) {
            JOptionPane.showMessageDialog(null, "El precio debe ser positivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Usamos el mismo ID, pero con el nuevo nombre y precio.
        Producto productoActualizado = new Producto(producto.getId(), nuevoNombre, nuevoPrecio, producto.getStock());

        if (inventario.updateProducto(productoActualizado)) {
            JOptionPane.showMessageDialog(null, "Producto actualizado con éxito: " + nuevoNombre + " ($" + String.format("%.2f", nuevoPrecio) + ")");
        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // NUEVO MÉTODO: Lógica para eliminar producto
    private void eliminarProducto() {
        String nombre = getStringInput("Ingrese el nombre del producto a ELIMINAR:");
        if (nombre == null) return;

        if (!inventario.existeProducto(nombre)) {
            JOptionPane.showMessageDialog(null, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(null, 
                "¿Está seguro de que desea ELIMINAR " + nombre + " permanentemente?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (inventario.deleteProducto(nombre)) {
                JOptionPane.showMessageDialog(null, "Producto '" + nombre + "' eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al intentar eliminar el producto de la BD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Eliminación cancelada.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void mostrarEstadisticas() {
        List<Producto> productos = inventario.getTodosLosProductos();
        
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El inventario está vacío. No hay estadísticas para mostrar.");
            return;
        }

        double precioMax = inventario.getPrecioMaximo();
        double precioMin = inventario.getPrecioMinimo();

        String mensaje = String.format("--- Estadísticas de Precios ---\n" +
                                     "Precio más barato: $%.2f\n" +
                                     "Precio más caro: $%.2f", precioMin, precioMax);

        JOptionPane.showMessageDialog(null, mensaje, "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void buscarProducto() {
        String busqueda = getStringInput("Ingrese el nombre o parte del nombre del producto a buscar:");
        if (busqueda == null) return;

        StringBuilder resultados = new StringBuilder("--- Resultados de Búsqueda ---\n");
        boolean encontrado = false;

        List<Producto> productos = inventario.getTodosLosProductos();
        
        for (Producto producto : productos) {
            String nombreProducto = producto.getNombre();
            if (nombreProducto.toLowerCase().contains(busqueda.toLowerCase())) {
                resultados.append("ID: ").append(producto.getId()).append("\n")
                          .append("  Producto: ").append(nombreProducto).append("\n")
                          .append("  Precio: $").append(String.format("%.2f", producto.getPrecio())).append("\n")
                          .append("  Stock: ").append(producto.getStock()).append("\n")
                          .append("------------------------\n");
                encontrado = true;
            }
        }

        if (!encontrado) {
            resultados.append("No se encontraron productos que coincidan con la búsqueda.");
        }

        JOptionPane.showMessageDialog(null, resultados.toString(), "Buscar Producto", JOptionPane.PLAIN_MESSAGE);
    }

    private void mostrarTicketFinal() {
        String mensaje = String.format("Gracias por tu visita.\n" +
                                     "Total de compras en esta sesión: $%.2f", totalCompras);
        JOptionPane.showMessageDialog(null, mensaje, "Ticket Final", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        MiniTiendaRiwi app = new MiniTiendaRiwi();
        app.mostrarMenu();
    }
}