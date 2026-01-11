package com.utez.fintree.integradora_final.dao;

import com.utez.fintree.integradora_final.db.ConexionDB;
import com.utez.fintree.integradora_final.model.Presupuesto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PresupuestoDAO {

    public List<Presupuesto> obtenerPorUsuario(int idUsuario) {
        List<Presupuesto> presupuestos = new ArrayList<>();
        String sql = "SELECT * FROM presupuestos WHERE id_usuario = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Presupuesto presupuesto = new Presupuesto(
                        rs.getInt("id"),
                        rs.getDouble("monto"),
                        rs.getInt("mes"),
                        rs.getInt("anio"),
                        rs.getInt("id_usuario")
                );
                presupuestos.add(presupuesto);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los presupuestos: " + e.getMessage());
        }
        return presupuestos;
    }

    public boolean agregar(Presupuesto presupuesto, int idUsuario) {
        String sql = "INSERT INTO presupuestos(monto, mes, anio, id_usuario) VALUES(?,?,?,?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, presupuesto.getMonto());
            pstmt.setInt(2, presupuesto.getMes());
            pstmt.setInt(3, presupuesto.getAnio());
            pstmt.setInt(4, idUsuario);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar el presupuesto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Presupuesto presupuesto, int idUsuario) {
        String sql = "UPDATE presupuestos SET monto = ? WHERE mes = ? AND anio = ? AND id_usuario = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, presupuesto.getMonto());
            pstmt.setInt(2, presupuesto.getMes());
            pstmt.setInt(3, presupuesto.getAnio());
            pstmt.setInt(4, idUsuario);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el presupuesto: " + e.getMessage());
            return false;
        }
    }
}