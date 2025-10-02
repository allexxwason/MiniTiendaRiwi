package com.codeup.minitiendariwi.ui;

import com.codeup.minitiendariwi.domain.Alimento;
import com.codeup.minitiendariwi.domain.Electrodomestico;
import com.codeup.minitiendariwi.domain.Inventario;
import com.codeup.minitiendariwi.domain.Producto;
import javax.swing.JOptionPane;
import javax.swing.JFrame; // ¡IMPORTANTE! Se añade para anclar las ventanas
import java.util.List;

/**
 * Clase principal de la aplicación.
 * Gestiona la interacción con el usuario a través de JOptionPane.
 */
public class MiniTiendaRiwi {

    private Inventario inventario;
    private double totalCompras;
    private JFrame parentFrame; // NUEVO: La ventana ancla para los JOptionPanes

    public MiniTiendaRiwi() {
        this.inventario = new Inventario();
        this.totalCompras = 0.0;
        
        // Inicializar la ventana ancla (invisible)
        this.parentFrame = new JFrame();
        this.parentFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.parentFrame.setVisible(false); 
    }

    public void mostrarMenu() {
        String opcion = "";
        do {
            // Se usa this.parentFrame como ancla
            opcion = JOptionPane.showInputDialog(this.parentFrame,
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
                    actualizarStock(); 
                    break;
                case "7":
                    actualizarProducto(); 
                    break;
                case "8":
                    eliminarProducto(); 
                    break;
                case "9":
                    mostrarTicketFinal();
                    // Se usa this.parentFrame como ancla
                    JOptionPane.showMessageDialog(this.parentFrame, "Saliendo del sistema.", "Adiós", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    // Se usa this.parentFrame como ancla
                    JOptionPane.showMessageDialog(this.parentFrame, "Opción inválida. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } while (!opcion.equals("9"));
    }
    
    // --- Métodos de Entrada de Usuario (Corregidos) ---

    private String getStringInput(String mensaje) {
        // Se usa this.parentFrame como ancla
        String input = JOptionPane.showInputDialog(this.parentFrame, mensaje);
        return (input == null || input.trim().isEmpty()) ? null : input.trim();
    }

    private int getIntInput(String mensaje) {
        // Se usa this.parentFrame como ancla
        String input = JOptionPane.showInputDialog(this.parentFrame, mensaje);
        if (input == null || input.trim().isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Entrada inválida. Ingrese un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    private double getDoubleInput(String mensaje) {
        // Se usa this.parentFrame como ancla
        String input = JOptionPane.showInputDialog(this.parentFrame, mensaje);
        if (input == null || input.trim().isEmpty()) {
            return -1.0;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Entrada inválida. Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1.0;
        }
    }
    
    // --- Lógica de la Aplicación (Funcionalidades) ---

    private void agregarProducto() {
        // 1. Pedir el tipo
        // Se usa this.parentFrame como ancla
        String tipoStr = JOptionPane.showInputDialog(this.parentFrame, 
                "Seleccione el tipo de producto:\n1. Alimento\n2. Electrodoméstico",
                "Tipo de Producto", JOptionPane.QUESTION_MESSAGE);
        
        if (tipoStr == null) return;
        
        String tipo;
        if (tipoStr.equals("1")) {
            tipo = "Alimento";
        } else if (tipoStr.equals("2")) {
            tipo = "Electrodomestico";
        } else {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Opción de tipo inválida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 2. Pedir datos y validar
        String nombre = getStringInput("Ingrese el nombre del producto:");
        if (nombre == null) return;

        if (inventario.existeProducto(nombre)) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "El producto '" + nombre + "' ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precio = getDoubleInput("Ingrese el precio del producto:");
        if (precio <= 0) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "El precio debe ser un valor positivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad = getIntInput("Ingrese el stock inicial:");
        if (cantidad < 0) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "El stock no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 3. Crear la subclase correcta (Polimorfismo)
        Producto nuevoProducto;
        if (tipo.equals("Alimento")) {
            nuevoProducto = new Alimento(nombre, precio, cantidad); 
        } else { 
            nuevoProducto = new Electrodomestico(nombre, precio, cantidad); 
        }

        // 4. Guardar
        if (inventario.addProducto(nuevoProducto)) { 
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Producto agregado correctamente.");
        } else {
             // Se usa this.parentFrame como ancla
             JOptionPane.showMessageDialog(this.parentFrame, "Error al agregar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void listarInventario() {
        StringBuilder sb = new StringBuilder("--- Inventario Actual ---\n");
        List<Producto> productos = inventario.getTodosLosProductos();

        if (productos.isEmpty()) {
            sb.append("El inventario está vacío.");
        } else {
            for (Producto producto : productos) {
                // Polimorfismo: llama a getDescripcion() de la subclase correcta
                sb.append("ID: ").append(producto.getId()).append("\n") 
                  .append("  Producto: ").append(producto.getNombre()).append("\n")
                  .append("  Precio: $").append(String.format("%.2f", producto.getPrecio())).append("\n")
                  .append("  Stock: ").append(producto.getStock()).append("\n")
                  .append("  Descripción: ").append(producto.getDescripcion()).append("\n") 
                  .append("------------------------\n");
            }
        }
        // Se usa this.parentFrame como ancla
        JOptionPane.showMessageDialog(this.parentFrame, sb.toString(), "Listado de Inventario", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void comprarProducto() {
        String nombre = getStringInput("Ingrese el nombre del producto a comprar:");
        if (nombre == null) return;

        Producto producto = inventario.getProductoPorNombre(nombre);
        
        if (producto == null) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Producto no encontrado.");
            return;
        }

        int cantidad = getIntInput("Ingrese la cantidad a comprar:");
        if (cantidad == -1 || cantidad <= 0) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "La cantidad debe ser mayor que cero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stockActual = producto.getStock();
        if (stockActual < cantidad) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Stock insuficiente. Cantidad disponible: " + stockActual);
        } else {
            int nuevoStock = stockActual - cantidad;
            
            if (inventario.actualizarStock(nombre, nuevoStock)) {
                double precioProducto = producto.getPrecio();
                totalCompras += precioProducto * cantidad;
                // Se usa this.parentFrame como ancla
                JOptionPane.showMessageDialog(this.parentFrame, "Compra realizada con éxito. Nuevo stock de " + nombre + ": " + nuevoStock);
            } else {
                 // Se usa this.parentFrame como ancla
                 JOptionPane.showMessageDialog(this.parentFrame, "Error al actualizar el stock en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void actualizarStock() {
        String nombre = getStringInput("Ingrese el nombre del producto para actualizar stock:");
        if (nombre == null) return;

        if (!inventario.existeProducto(nombre)) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int nuevoStock = getIntInput("Ingrese el NUEVO stock total (ej: 50):");
        if (nuevoStock == -1 || nuevoStock < 0) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Stock no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (inventario.actualizarStock(nombre, nuevoStock)) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Stock de " + nombre + " actualizado a: " + nuevoStock);
        } else {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Error al actualizar stock en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // CORRECCIÓN: Instancia la subclase correcta
    private void actualizarProducto() {
        String nombreActual = getStringInput("Ingrese el nombre del producto a actualizar:");
        if (nombreActual == null) return;

        Producto producto = inventario.getProductoPorNombre(nombreActual);
        if (producto == null) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevoNombre = getStringInput("Ingrese el nuevo nombre (" + producto.getNombre() + "):");
        double nuevoPrecio = getDoubleInput("Ingrese el nuevo precio (" + producto.getPrecio() + "):");

        if (nuevoNombre == null || nuevoPrecio == -1.0) {
             // Se usa this.parentFrame como ancla
             JOptionPane.showMessageDialog(this.parentFrame, "Operación cancelada o datos inválidos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (nuevoPrecio <= 0) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "El precio debe ser positivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Determinar la subclase correcta (Polimorfismo para mantener el tipo)
        Producto productoActualizado;
        if (producto instanceof Alimento) {
            productoActualizado = new Alimento(producto.getId(), nuevoNombre, nuevoPrecio, producto.getStock());
        } else if (producto instanceof Electrodomestico) {
            productoActualizado = new Electrodomestico(producto.getId(), nuevoNombre, nuevoPrecio, producto.getStock());
        } else {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Error interno: Tipo de producto desconocido para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (inventario.updateProducto(productoActualizado)) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Producto actualizado con éxito: " + nuevoNombre + " ($" + String.format("%.2f", nuevoPrecio) + ")");
        } else {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Error al actualizar el producto en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        String nombre = getStringInput("Ingrese el nombre del producto a ELIMINAR:");
        if (nombre == null) return;

        if (!inventario.existeProducto(nombre)) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Se usa this.parentFrame como ancla
        int confirmacion = JOptionPane.showConfirmDialog(this.parentFrame, 
                "¿Está seguro de que desea ELIMINAR " + nombre + " permanentemente?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (inventario.deleteProducto(nombre)) {
                // Se usa this.parentFrame como ancla
                JOptionPane.showMessageDialog(this.parentFrame, "Producto '" + nombre + "' eliminado correctamente.");
            } else {
                // Se usa this.parentFrame como ancla
                JOptionPane.showMessageDialog(this.parentFrame, "Error al intentar eliminar el producto de la BD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "Eliminación cancelada.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void mostrarEstadisticas() {
        List<Producto> productos = inventario.getTodosLosProductos();
        
        if (productos.isEmpty()) {
            // Se usa this.parentFrame como ancla
            JOptionPane.showMessageDialog(this.parentFrame, "El inventario está vacío. No hay estadísticas para mostrar.");
            return;
        }

        double precioMax = inventario.getPrecioMaximo();
        double precioMin = inventario.getPrecioMinimo();

        String mensaje = String.format("--- Estadísticas de Precios ---\n" +
                                     "Precio más barato: $%.2f\n" +
                                     "Precio más caro: $%.2f", precioMin, precioMax);

        // Se usa this.parentFrame como ancla
        JOptionPane.showMessageDialog(this.parentFrame, mensaje, "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
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
                          .append("  Descripción: ").append(producto.getDescripcion()).append("\n") // Usa polimorfismo
                          .append("------------------------\n");
                encontrado = true;
            }
        }

        if (!encontrado) {
            resultados.append("No se encontraron productos que coincidan con la búsqueda.");
        }

        // Se usa this.parentFrame como ancla
        JOptionPane.showMessageDialog(this.parentFrame, resultados.toString(), "Buscar Producto", JOptionPane.PLAIN_MESSAGE);
    }

    private void mostrarTicketFinal() {
        String mensaje = String.format("Gracias por tu visita.\n" +
                                     "Total de compras en esta sesión: $%.2f", totalCompras);
        // Se usa this.parentFrame como ancla
        JOptionPane.showMessageDialog(this.parentFrame, mensaje, "Ticket Final", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        MiniTiendaRiwi app = new MiniTiendaRiwi();
        app.mostrarMenu();
        // Cierre limpio de la aplicación.
        System.exit(0);
    }
}