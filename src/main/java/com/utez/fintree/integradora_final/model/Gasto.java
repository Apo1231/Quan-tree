package com.utez.fintree.integradora_final.model;

import java.time.LocalDate;

public class Gasto {
    private int id;
    private String concepto;
    private double monto;
    private String categoria;
    private LocalDate fecha;
    private String metodoPago;
    private String notas;
    private int idUsuario;

    // Constructor para crear un nuevo gasto
    public Gasto(String concepto, double monto, String categoria, LocalDate fecha, String metodoPago, String notas) {
        this.concepto = concepto;
        this.monto = monto;
        this.categoria = categoria;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.notas = notas;
    }

    // Constructor para leer un gasto desde la base de datos
    public Gasto(int id, String concepto, double monto, String categoria, LocalDate fecha, String metodoPago, String notas, int idUsuario) {
        this.id = id;
        this.concepto = concepto;
        this.monto = monto;
        this.categoria = categoria;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.notas = notas;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
}