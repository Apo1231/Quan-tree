package com.utez.fintree.integradora_final.model;

public class Categoria {
    private int id;
    private String nombre;
    private String color;
    private int idUsuario;

    // Constructor para crear
    public Categoria(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
    }

    // Constructor para leer desde la BD
    public Categoria(int id, String nombre, String color, int idUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public String toString() {
        return this.nombre;
    }
}