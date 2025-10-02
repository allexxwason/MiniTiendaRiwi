package com.codeup.minitiendariwi.domain;

/**
 * Clase abstracta base para todos los productos en el inventario.
 * Implementa encapsulamiento y define la estructura común de los productos.
 */
public abstract class Producto {

    private int id; 
    private String nombre;
    private double precio;
    private int stock;

    // Constructor con ID (para recuperar de la BD)
    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    // Constructor sin ID (para crear un nuevo producto)
    public Producto(String nombre, double precio, int stock) {
        this(0, nombre, precio, stock); 
    }

    // --- Método Abstracto (Polimorfismo) ---
    public abstract String getDescripcion(); 

    // --- Encapsulamiento: Getters y Setters ---
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    @Override
    public String toString() {
        return "ID: " + id + ", Nombre: '" + nombre + "', Precio: $" + String.format("%.2f", precio) + ", Stock: " + stock;
    }
}