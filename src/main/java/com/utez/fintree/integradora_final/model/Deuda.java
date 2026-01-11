package com.utez.fintree.integradora_final.model;

import java.time.LocalDate;

public class Deuda {
    private int id;
    private String concepto;
    private double montoInicial;
    private double montoPagado;
    private LocalDate fechaInicio;
    private LocalDate fechaLimite;
    private String categoria;
    private String notas;
    private String estado;
    private int idUsuario;

    // Constructor para crear una nueva deuda
    public Deuda(String concepto, double montoInicial, LocalDate fechaInicio, LocalDate fechaLimite, String categoria, String notas) {
        this.concepto = concepto;
        this.montoInicial = montoInicial;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        this.categoria = categoria;
        this.notas = notas;
        this.montoPagado = 0.0;
        this.estado = "Activa";
    }

    // Constructor para leer una deuda desde la base de datos
    public Deuda(int id, String concepto, double montoInicial, double montoPagado, LocalDate fechaInicio, LocalDate fechaLimite, String categoria, String notas, String estado, int idUsuario) {
        this.id = id;
        this.concepto = concepto;
        this.montoInicial = montoInicial;
        this.montoPagado = montoPagado;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        this.categoria = categoria;
        this.notas = notas;
        this.estado = estado;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }
    public double getMontoInicial() { return montoInicial; }
    public void setMontoInicial(double montoInicial) { this.montoInicial = montoInicial; }
    public double getMontoPagado() { return montoPagado; }
    public void setMontoPagado(double montoPagado) { this.montoPagado = montoPagado; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    // Método de utilidad
    public double getMontoRestante() {
        return montoInicial - montoPagado;
    }
}