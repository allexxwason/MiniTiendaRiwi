/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.minitiendariwi.domain;

import java.util.ArrayList;
import java.util.HashMap;


public class Inventario {

    // Utiliza un ArrayList para los nombres de los productos, permitiendo
    // un tamaño dinámico.
    private ArrayList<String> nombres;

    // Utiliza un Array primitivo para los precios.
    // Su tamaño se gestionará dinámicamente.
    private double[] precios;
    private int preciosSize; 

    // Utiliza un HashMap para el stock, asociando el nombre del producto
    // con su cantidad.
    private HashMap<String, Integer> stock;

    /**
     * Constructor para inicializar las estructuras de datos.
     */
    public Inventario() {
        this.nombres = new ArrayList<>();
        this.stock = new HashMap<>();
        // Inicia el array de precios con una capacidad inicial.
        // Se expandirá si es necesario.
        this.precios = new double[10];
        this.preciosSize = 0;
    }

    /**
     * Agrega un nuevo producto al inventario.
     *
     * @param nombre Nombre del producto.
     * @param precio Precio del producto.
     * @param cantidad Cantidad inicial en stock.
     */
    public void addProducto(String nombre, double precio, int cantidad) {
        
        if (preciosSize >= precios.length) {
            expandPrecios();
        }

        // Agrega el nombre del producto al ArrayList.
        nombres.add(nombre);
        // Agrega el precio al array en la siguiente posición disponible.
        precios[preciosSize++] = precio;
        // Agrega el nombre y la cantidad al HashMap.
        stock.put(nombre, cantidad);
    }

  
    private void expandPrecios() {
        // Crea un nuevo array con el doble de capacidad.
        double[] newPrecios = new double[precios.length * 2];
        // Copia todos los elementos del array viejo al nuevo.
        System.arraycopy(precios, 0, newPrecios, 0, precios.length);
        // Asigna el nuevo array a la variable de precios.
        precios = newPrecios;
    }

 
    
    public int indexOfNombre(String nombre) {
        return nombres.indexOf(nombre);
    }

    // --- Métodos de acceso (getters) para usar los datos en otras clases ---

    public ArrayList<String> getNombres() {
        return nombres;
    }

    public double[] getPrecios() {
        return precios;
    }

    public int getPreciosSize() {
        return preciosSize;
    }

    public HashMap<String, Integer> getStock() {
        return stock;
    }

   
}