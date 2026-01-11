package com.utez.fintree.integradora_final.model;

public class Presupuesto {
    private int id;
    private double monto;
    private int mes;
    private int anio;
    private int idUsuario;

    // Constructor para crear un nuevo presupuesto
    public Presupuesto(double monto, int mes, int anio) {
        this.monto = monto;
        this.mes = mes;
        this.anio = anio;
    }

    // Constructor para leer desde la BD
    public Presupuesto(int id, double monto, int mes, int anio, int idUsuario) {
        this.id = id;
        this.monto = monto;
        this.mes = mes;
        this.anio = anio;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public int getMes() { return mes; }
    public void setMes(int mes) { this.mes = mes; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
}