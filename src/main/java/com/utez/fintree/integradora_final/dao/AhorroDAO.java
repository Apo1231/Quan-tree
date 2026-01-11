package com.utez.fintree.integradora_final.dao;

import com.utez.fintree.integradora_final.db.ConexionDB;
import com.utez.fintree.integradora_final.model.Ahorro;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AhorroDAO {

    public List<Ahorro> obtenerPorUsuario(int idUsuario) {
        List<Ahorro> ahorros = new ArrayList<>();
        String sql = "SELECT * FROM ahorros WHERE id_usuario = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Ahorro ahorro = new Ahorro(
                        rs.getInt("id"),
                        rs.getString("concepto"),
                        rs.getDouble("monto"),
                        LocalDate.parse(rs.getString("fecha")),
                        rs.getString("meta"),
                        rs.getInt("id_usuario")
                );
                ahorros.add(ahorro);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los ahorros: " + e.getMessage());
        }
        return ahorros;
    }

    public boolean agregar(Ahorro ahorro, int idUsuario) {
        String sql = "INSERT INTO ahorros(concepto, monto, fecha, meta, id_usuario) VALUES(?,?,?,?,?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ahorro.getConcepto());
            pstmt.setDouble(2, ahorro.getMonto());
            pstmt.setString(3, ahorro.getFecha().toString());
            pstmt.setString(4, ahorro.getMeta());
            pstmt.setInt(5, idUsuario);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar el ahorro: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Ahorro ahorro, int idUsuario) {
        String sql = "UPDATE ahorros SET concepto = ?, monto = ?, fecha = ?, meta = ? WHERE id = ? AND id_usuario = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ahorro.getConcepto());
            pstmt.setDouble(2, ahorro.getMonto());
            pstmt.setString(3, ahorro.getFecha().toString());
            pstmt.setString(4, ahorro.getMeta());
            pstmt.setInt(5, ahorro.getId());
            pstmt.setInt(6, idUsuario);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el ahorro: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idAhorro, int idUsuario) {
        String sql = "DELETE FROM ahorros WHERE id = ? AND id_usuario = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAhorro);
            pstmt.setInt(2, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el ahorro: " + e.getMessage());
            return false;
        }
    }
}