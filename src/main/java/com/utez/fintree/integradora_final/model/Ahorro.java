package com.utez.fintree.integradora_final.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ahorro {
    private int id;
    private String concepto;
    private double monto;
    private LocalDate fecha;
    private String meta;
    private int idUsuario;

    // Constructor para crear un nuevo ahorro (sin id, sin idUsuario)
    public Ahorro(String concepto, double monto, LocalDate fecha, String meta) {
        this.concepto = concepto;
        this.monto = monto;
        this.fecha = fecha;
        this.meta = meta;
    }

    // Constructor para leer un ahorro desde la BD (con todos los campos)
    public Ahorro(int id, String concepto, double monto, LocalDate fecha, String meta, int idUsuario) {
        this.id = id;
        this.concepto = concepto;
        this.monto = monto;
        this.fecha = fecha;
        this.meta = meta;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getMeta() { return meta; }
    public void setMeta(String meta) { this.meta = meta; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("%s - $%.2f (%s)", concepto, monto, fecha.format(formatter));
    }
}