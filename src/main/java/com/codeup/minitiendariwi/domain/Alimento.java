package com.codeup.minitiendariwi.domain;

public class Alimento extends Producto {

    // Constructores que llaman a la clase base
    public Alimento(int id, String nombre, double precio, int stock) {
        super(id, nombre, precio, stock);
    }
    
    public Alimento(String nombre, double precio, int stock) {
        super(nombre, precio, stock);
    }

    // Polimorfismo: Descripción específica
    @Override
    public String getDescripcion() {
        return "TIPO: Alimento - Listo para el consumo o preparación.";
    }
}