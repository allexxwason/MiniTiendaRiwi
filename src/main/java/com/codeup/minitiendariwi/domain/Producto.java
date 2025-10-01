package com.codeup.minitiendariwi.domain;

/**
 * Entidad que representa un Producto en el inventario,
 * incluyendo la llave primaria (ID).
 */
public class Producto {
    
    private int id; 
    private String nombre;
    private double precio;
    private int stock;

    /**
     * Constructor para productos recuperados de la BD (con ID).
     */
    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    /**
     * Constructor para crear un nuevo producto (sin ID, lo genera la BD).
     */
    public Producto(String nombre, double precio, int stock) {
        // Inicializa el ID a 0 (o -1) para indicar que es un nuevo registro
        this(0, nombre, precio, stock); 
    }

    // --- Getters y Setters ---
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id + 
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                '}';
    }
}