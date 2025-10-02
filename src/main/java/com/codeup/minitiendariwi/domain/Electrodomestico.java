package com.codeup.minitiendariwi.domain;

public class Electrodomestico extends Producto {

    // Constructores que llaman a la clase base
    public Electrodomestico(int id, String nombre, double precio, int stock) {
        super(id, nombre, precio, stock);
    }
    
    public Electrodomestico(String nombre, double precio, int stock) {
        super(nombre, precio, stock);
    }

    // Polimorfismo: Descripción específica
    @Override
    public String getDescripcion() {
        return "TIPO: Electrodoméstico - Requiere conexión eléctrica.";
    }
}