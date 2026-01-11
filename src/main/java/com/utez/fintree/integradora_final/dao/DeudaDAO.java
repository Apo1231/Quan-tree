package com.utez.fintree.integradora_final.dao;

import com.utez.fintree.integradora_final.db.ConexionDB;
import com.utez.fintree.integradora_final.model.Deuda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeudaDAO {

    public List<Deuda> obtenerPorUsuario(int idUsuario) {
        List<Deuda> deudas = new ArrayList<>();
        String sql = "SELECT * FROM deudas WHERE id_usuario = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Deuda deuda = new Deuda(
                        rs.getInt("id"),
                        rs.getString("concepto"),
                        rs.getDouble("monto_inicial"),
                        rs.getDouble("monto_pagado"),
                        LocalDate.parse(rs.getString("fecha_inicio")),
                        rs.getString("fecha_limite") != null ? LocalDate.parse(rs.getString("fecha_limite")) : null,
                        rs.getString("categoria"),
                        rs.getString("notas"),
                        rs.getString("estado"),
                        rs.getInt("id_usuario")
                );
                deudas.add(deuda);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las deudas: " + e.getMessage());
        }
        return deudas;
    }

    public boolean agregar(Deuda deuda, int idUsuario) {
        String sql = "INSERT INTO deudas(concepto, monto_inicial, fecha_inicio, fecha_limite, categoria, notas, id_usuario) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, deuda.getConcepto());
            pstmt.setDouble(2, deuda.getMontoInicial());
            pstmt.setString(3, deuda.getFechaInicio().toString());
            pstmt.setString(4, deuda.getFechaLimite() != null ? deuda.getFechaLimite().toString() : null);
            pstmt.setString(5, deuda.getCategoria());
            pstmt.setString(6, deuda.getNotas());
            pstmt.setInt(7, idUsuario);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al agregar la deuda: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Deuda deuda, int idUsuario) {
        String sql = "UPDATE deudas SET concepto = ?, monto_inicial = ?, monto_pagado = ?, fecha_inicio = ?, fecha_limite = ?, categoria = ?, notas = ?, estado = ? WHERE id = ? AND id_usuario = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, deuda.getConcepto());
            pstmt.setDouble(2, deuda.getMontoInicial());
            pstmt.setDouble(3, deuda.getMontoPagado());
            pstmt.setString(4, deuda.getFechaInicio().toString());
            pstmt.setString(5, deuda.getFechaLimite() != null ? deuda.getFechaLimite().toString() : null);
            pstmt.setString(6, deuda.getCategoria());
            pstmt.setString(7, deuda.getNotas());
            pstmt.setString(8, deuda.getEstado());
            pstmt.setInt(9, deuda.getId());
            pstmt.setInt(10, idUsuario);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la deuda: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idDeuda, int idUsuario) {
        String sql = "DELETE FROM deudas WHERE id = ? AND id_usuario = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idDeuda);
            pstmt.setInt(2, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar la deuda: " + e.getMessage());
            return false;
        }
    }
}