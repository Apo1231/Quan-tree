package com.utez.fintree.integradora_final.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {

    private static final String URL = "jdbc:sqlite:quantree.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void inicializarDB() {
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    nombre_usuario TEXT NOT NULL UNIQUE,\n"
                + "    contrasena TEXT NOT NULL\n"
                + ");";

        String sqlGastos = "CREATE TABLE IF NOT EXISTS gastos (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    concepto TEXT NOT NULL,\n"
                + "    monto REAL NOT NULL,\n"
                + "    categoria TEXT,\n"
                + "    fecha TEXT NOT NULL,\n"
                + "    metodo_pago TEXT,\n"
                + "    notas TEXT,\n"
                + "    id_usuario INTEGER,\n"
                + "    FOREIGN KEY(id_usuario) REFERENCES usuarios(id)\n"
                + ");";

        String sqlAhorros = "CREATE TABLE IF NOT EXISTS ahorros (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    concepto TEXT NOT NULL,\n"
                + "    monto REAL NOT NULL,\n"
                + "    fecha TEXT NOT NULL,\n"
                + "    meta TEXT,\n"
                + "    id_usuario INTEGER,\n"
                + "    FOREIGN KEY(id_usuario) REFERENCES usuarios(id)\n"
                + ");";

        String sqlCategorias = "CREATE TABLE IF NOT EXISTS categorias (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    nombre TEXT NOT NULL,\n" // Se quita UNIQUE para que cada usuario tenga sus propias categorías
                + "    color TEXT,\n"
                + "    id_usuario INTEGER,\n"
                + "    FOREIGN KEY(id_usuario) REFERENCES usuarios(id)\n"
                + ");";

        String sqlPresupuestos = "CREATE TABLE IF NOT EXISTS presupuestos (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    monto REAL NOT NULL,\n"
                + "    mes INTEGER NOT NULL,\n"
                + "    anio INTEGER NOT NULL,\n"
                + "    id_usuario INTEGER,\n"
                + "    FOREIGN KEY(id_usuario) REFERENCES usuarios(id)\n"
                + ");";

        String sqlDeudas = "CREATE TABLE IF NOT EXISTS deudas (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    concepto TEXT NOT NULL,\n"
                + "    monto_inicial REAL NOT NULL,\n"
                + "    monto_pagado REAL DEFAULT 0.0,\n"
                + "    fecha_inicio TEXT NOT NULL,\n"
                + "    fecha_limite TEXT,\n"
                + "    categoria TEXT,\n"
                + "    notas TEXT,\n"
                + "    estado TEXT DEFAULT 'Activa',\n"
                + "    id_usuario INTEGER,\n"
                + "    FOREIGN KEY(id_usuario) REFERENCES usuarios(id)\n"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlUsuarios);
            stmt.execute(sqlGastos);
            stmt.execute(sqlAhorros);
            stmt.execute(sqlCategorias);
            stmt.execute(sqlPresupuestos);
            stmt.execute(sqlDeudas);

            System.out.println("Base de datos multiusuario inicializada y tablas creadas.");

        } catch (SQLException e) {
            System.out.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}
